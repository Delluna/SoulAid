package com.example.soulaid.user.ui.center;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.example.soulaid.R;
import com.example.soulaid.dao.UserFavoriteDao;
import com.example.soulaid.entity.ArticleDetail;
import com.example.soulaid.adapter.ArticlesAdapter;
import com.example.soulaid.util.IOUtil;

import java.util.ArrayList;
import java.util.List;

public class UserFavoriteActivity extends AppCompatActivity {

    private String username;
    private RecyclerView recyclerView;

    private List<ArticleDetail> contents;
    private ArticlesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_favorite);
        init();
    }

    void init() {
        username = IOUtil.getUserInfo(UserFavoriteActivity.this)[0];

        recyclerView = findViewById(R.id.favorite);

        LinearLayoutManager manager = new LinearLayoutManager(UserFavoriteActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        contents = new ArrayList<>();
        adapter=new ArticlesAdapter(UserFavoriteActivity.this, contents,0);
        recyclerView.setAdapter(adapter);

        getFavoriteContents();
    }

    //获取收藏列表
    private void getFavoriteContents() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserFavoriteDao userFavoriteDao = new UserFavoriteDao();
                List<ArticleDetail> contents = userFavoriteDao.getFavoriteContents(username);
                for(int i=0;i<contents.size();i++){
                    contents.get(i).setIsFavorited(true);
                }
                //bundle传输List<>类型数据
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("datalist", (ArrayList<? extends Parcelable>) contents);
                Message message = Message.obtain();
                message.setData(bundle);
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    List<ArticleDetail> list = message.getData().getParcelableArrayList("datalist");
                    contents.addAll(list);
                    //当内容为空时,创建空白页
                    if(contents.size()==0){
                        setContentView(R.layout.activity_blank);
                    }else {
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
}
