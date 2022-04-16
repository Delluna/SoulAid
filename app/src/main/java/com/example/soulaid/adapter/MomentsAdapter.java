package com.example.soulaid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soulaid.R;
import com.example.soulaid.dao.MomentsDao;
import com.example.soulaid.entity.MomentDetail;
import com.example.soulaid.user.ui.society.moments.MomentDetailActivity;

import java.sql.Timestamp;
import java.util.List;

public class MomentsAdapter extends RecyclerView.Adapter {

    private int TYPE_ITEM = 1;
    private int TYPE_FOOT = -1;
    private boolean state=true;

    private Context context;
    private List<MomentDetail> contents;

    //ItemViewHolder
    public static class ItemHolder extends RecyclerView.ViewHolder {
        public int mid;//moment的id
        public TextView name, title, content, time,likedCount;
        public ImageView like;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            likedCount=itemView.findViewById(R.id.likedCount);
            like = itemView.findViewById(R.id.like);
        }
    }

    //FootViewHolder
    public static class FootHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public FootHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }

    //构造函数
    public MomentsAdapter(Context context, List<MomentDetail> contents) {
        this.context = context;
        this.contents = contents;
    }

    //为每个view设置type
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOT;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new MomentsAdapter.ItemHolder(LayoutInflater.from(this.context).inflate(R.layout.item_moment, parent, false));
        } else {
            return new MomentsAdapter.FootHolder(LayoutInflater.from(this.context).inflate(R.layout.item_foot, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof FootHolder){
            if(state){
                ((FootHolder) holder).textView.setText("正在加载中");
            }else {
                ((FootHolder) holder).textView.setText("没有更多了");
            }

        }else {
            if (contents == null) {
                ((ItemHolder) holder).name.setText("名称");
                ((ItemHolder) holder).title.setText("标题");
                ((ItemHolder) holder).content.setText("内容");
                ((ItemHolder) holder).time.setText("发布时间");
                ((ItemHolder) holder).likedCount.setText("0");
            } else {
                ((ItemHolder) holder).mid=contents.get(position).getId();
                ((ItemHolder) holder).name.setText(contents.get(position).getUname());
                ((ItemHolder) holder).title.setText(contents.get(position).getTitle());
                ((ItemHolder) holder).content.setText(contents.get(position).getContent());
                ((ItemHolder) holder).time.setText(contents.get(position).getDatetime().toString());
                ((ItemHolder) holder).likedCount.setText(Integer.toString(contents.get(position).getLiked()));

                //为button设置点击事件,目前还无法实现取消点赞
                ((ItemHolder) holder).like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MomentsDao momentsDao=new MomentsDao();
                                boolean state= momentsDao.addLikedCount(((ItemHolder) holder).mid);
                                if(state){
                                    Bundle bundle=new Bundle();
                                    bundle.putInt("pos",((ItemHolder) holder).getAdapterPosition());
                                    Message message=Message.obtain();
                                    message.setData(bundle);
                                    message.what=1;
                                    handler.sendMessage(message);
                                }else {
                                    handler.sendEmptyMessage(0);
                                }
                            }
                        }).start();

                    }
                });
            }
            //为整个item设置点击事件
            this.setOnItemClick((ItemHolder)holder);
        }

    }

    @Override
    public int getItemCount() {
        if (contents == null) {
            return 1;
        }
        return contents.size()+1;  //+1表示添加footViewHolder
    }

    private void setOnItemClick(final ItemHolder itemHolder){
        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MomentDetailActivity.class);
                MomentDetail momentDetail=new MomentDetail();
                momentDetail.setId(itemHolder.mid);
                momentDetail.setUname(itemHolder.name.getText().toString());
                momentDetail.setContent(itemHolder.content.getText().toString());
                momentDetail.setTitle(itemHolder.title.getText().toString());
                momentDetail.setDatetime(Timestamp.valueOf(itemHolder.time.getText().toString()));
                momentDetail.setLiked(Integer.parseInt(itemHolder.likedCount.getText().toString()));

                Bundle bundle=new Bundle();
                bundle.putSerializable("moment",momentDetail);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
    }

    public void hasMore(boolean state){
        this.state=state;
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            //reTimes++;
            switch (message.what) {
                //点赞失败
                case 0:
                    Toast.makeText(context,"点赞失败",Toast.LENGTH_SHORT).show();
                    break;
                    //点赞成功
                case 1:
                    Toast.makeText(context,"点赞成功",Toast.LENGTH_SHORT).show();
                    int pos = message.getData().getInt("pos");
                    contents.get(pos).setLiked(contents.get(pos).getLiked()+1);
                    notifyDataSetChanged();
                    break;
            }
        }
    };
}
