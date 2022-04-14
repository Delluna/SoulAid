package com.example.soulaid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soulaid.dao.UserFavoriteDao;
import com.example.soulaid.util.IOUtil;

public class ArticleDetailActivity extends AppCompatActivity {
    private Intent intent;
    private TextView name,title,content,time;
    private Button favorite;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        init();
    }

    void  init(){
        intent = getIntent();

        name=findViewById(R.id.name);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        time=findViewById(R.id.time);
        favorite=findViewById(R.id.favorite);

        name.setText(intent.getStringExtra("name"));
        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
        time.setText(intent.getStringExtra("time"));

        String[] result = IOUtil.getUserInfo(ArticleDetailActivity.this);
        username = result[0];

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserFavoriteDao userFavoriteDao = new UserFavoriteDao();
                        int state = userFavoriteDao.addToFavorite(username, title.getText().toString());
                        handler.sendEmptyMessage(state);
                    }
                }).start();
            }
        });
    };

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Toast toast;
            if (message.what == 1) {
                toast=Toast.makeText(ArticleDetailActivity.this, "添加成功", Toast.LENGTH_SHORT);
            } else  if (message.what == 0){
                toast = Toast.makeText(ArticleDetailActivity.this, "已在收藏中", Toast.LENGTH_SHORT);
            }else {
                toast = Toast.makeText(ArticleDetailActivity.this, "添加失败", Toast.LENGTH_SHORT);
            }
//            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    };
}
