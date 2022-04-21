package com.example.soulaid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soulaid.ArticleDetailActivity;
import com.example.soulaid.R;
import com.example.soulaid.dao.UserFavoriteDao;
import com.example.soulaid.entity.ArticleDetail;
import com.example.soulaid.util.IOUtil;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter {

    private int TYPE_ITEM = 1;
    private int TYPE_FOOT = -1;

    private Context context;
    private List<ArticleDetail> contents;
    private String username;
    private int from=1;  //from=0表示为用户收藏

    public static class ItemHolder extends RecyclerView.ViewHolder {
        public TextView name, title, content, time;
        public boolean isFavorited = false;
        public ImageView favorite;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            favorite = itemView.findViewById(R.id.img_favorite);
        }
    }

    public static class FootHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public FootHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }

    //构造方法
    public ArticlesAdapter(Context context, List<ArticleDetail> contents, int from) {
        this.context = context;
        this.contents = contents;
        this.from=from;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOT;
        }
        return TYPE_ITEM;
    }

    //create方法
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new ItemHolder(LayoutInflater.from(this.context).inflate(R.layout.item_article, parent, false));
        } else {
            return new FootHolder(LayoutInflater.from(this.context).inflate(R.layout.item_foot, parent, false));
        }
    }

    //赋值
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemHolder) {
            if (contents == null) {
                ((ItemHolder) holder).name.setText("名称");
                ((ItemHolder) holder).title.setText("标题");
                ((ItemHolder) holder).content.setText("内容");
                ((ItemHolder) holder).time.setText("发布时间");

            } else {

                username = IOUtil.getUserInfo(context)[0];

                ArticleDetail articleDetail = contents.get(position);
                ((ItemHolder) holder).name.setText(articleDetail.getName());
                ((ItemHolder) holder).title.setText(articleDetail.getTitle());
                ((ItemHolder) holder).content.setText(articleDetail.getContent());
                ((ItemHolder) holder).time.setText(articleDetail.getDatetime().toString());
                ((ItemHolder) holder).isFavorited = articleDetail.getIsFavorited();

                if (((ItemHolder) holder).isFavorited == true) {
                    Drawable background = context.getDrawable(R.drawable.ic_star);
                    ((ItemHolder) holder).favorite.setBackground(background);
                }

                final boolean isFavorited = ((ItemHolder) holder).isFavorited;
                final String title = ((ItemHolder) holder).title.getText().toString();
                //为item中的button添加点击事件
                ((ItemHolder) holder).favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                UserFavoriteDao userFavoriteDao = new UserFavoriteDao();
                                Bundle bundle = new Bundle();
                                if (isFavorited == true) {
                                    int state = userFavoriteDao.deleteFromFavorite(username, title);
                                    handler.sendEmptyMessage(state);

                                } else {
                                    int state = userFavoriteDao.addToFavorite(username, title);
                                    handler.sendEmptyMessage(state);
                                }
                            }
                        }).start();
                    }
                });

                //为item整体添加点击事件
                setOnClick(((ItemHolder) holder));
            }
        } else {
            if(from==1){
                ((FootHolder) holder).textView.setText("正在加载中");
            }else {
            ((FootHolder) holder).textView.setText("无更多内容");}
        }
    }

    //获取数量
    @Override
    public int getItemCount() {
        if (contents == null) {
            return 1;
        }
        return contents.size() + 1;//+1表示添加footViewHolder
    }

    //自定义接口实现功能：点击item进行跳转页面
    public void setOnClick(ArticlesAdapter.ItemHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1.创建intent
                Intent intent = new Intent(view.getContext(), ArticleDetailActivity.class);
                //2.传数据(还没传图片imageView)
                intent.putExtra("name", ((TextView) view.findViewById(R.id.name)).getText().toString());
                intent.putExtra("title", ((TextView) view.findViewById(R.id.title)).getText().toString());
                intent.putExtra("content", ((TextView) view.findViewById(R.id.content)).getText().toString());
                intent.putExtra("time", ((TextView) view.findViewById(R.id.time)).getText().toString());

                //3.start intent
                view.getContext().startActivity(intent);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            String hint = "";
            switch (message.what) {
                //UserFavoriteDao.addToFavorite()
                case -1:
                    hint = "添加失败";
                    break;
                case 0:
                    hint = "已在收藏中";
                    break;
                case 1:
                    hint = "添加成功";
                    break;
                //UserFavoriteDao.deleteFromFavorite
                case 2:
                    hint = "未完成取消收藏操作";
                    break;
                case 3:
                    hint = "已取消收藏";
                    break;
            }
            Toast.makeText(context, hint, Toast.LENGTH_SHORT).show();
        }
    };
}