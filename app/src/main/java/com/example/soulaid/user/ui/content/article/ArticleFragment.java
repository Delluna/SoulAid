package com.example.soulaid.user.ui.content.article;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soulaid.R;
import com.example.soulaid.dao.ArticlesDao;
import com.example.soulaid.entity.ArticleDetail;
import com.example.soulaid.adapter.ArticlesAdapter;

import java.util.ArrayList;
import java.util.List;

public class ArticleFragment extends Fragment{
    private View view;

    private ArticlesAdapter adapter;

    //private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private int reTimes = 0;//更新次数
    private int number = 5;//每次更新文章的个数
    private List<ArticleDetail> Articles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_article, container, false);
        init();
        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recycleView);
        //refreshLayout = view.findViewById(R.id.swipe_refresh);

        getArticles();

        //上拉加载  https://www.jianshu.com/p/e8c2475da861
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //滑动到底部 recyclerView.canScrollVertically(1（-1）)为false表示已经滑到底（顶）部
                if (!recyclerView.canScrollVertically(1)) {
                    //recyclerview滑动到底部,更新数据
                    boolean state = getMoreArticles();
                    if(state){
                        adapter.hasMore(true);
                    }
                    else {
                        adapter.hasMore(false);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    //从数据库获取文章
    private void getArticles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArticlesDao articlesDao = new ArticlesDao();
                List<ArticleDetail> contents = articlesDao.getContents(number);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("datalist", (ArrayList<? extends Parcelable>) contents);
                Message message = Message.obtain();
                message.setData(bundle);
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    //加载更多数据
    private boolean getMoreArticles(){
        if(reTimes>=2) return false;
        //加载更多数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArticlesDao articlesDao = new ArticlesDao();
                List<ArticleDetail> contents = articlesDao.getContents(number);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("datalist2", (ArrayList<? extends Parcelable>) contents);
                Message message = Message.obtain();
                message.setData(bundle);
                message.what = 2;
                handler.sendMessage(message);

            }
        }).start();
        return true;
    }

    //获取到文章后为recycleview设置适配器
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            reTimes++;
            switch (message.what) {
                case 1:
                    Articles = message.getData().getParcelableArrayList("datalist");

                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    manager.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setLayoutManager(manager);
                    adapter=new ArticlesAdapter(getContext(), Articles);
                    recyclerView.setAdapter(adapter);
                    break;
                case 2:
                    List<ArticleDetail> list = message.getData().getParcelableArrayList("datalist2");
                    Articles.addAll(list);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
