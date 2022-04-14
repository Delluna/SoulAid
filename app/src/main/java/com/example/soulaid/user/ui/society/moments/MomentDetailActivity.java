package com.example.soulaid.user.ui.society.moments;

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

import com.example.soulaid.R;
import com.example.soulaid.adapter.MomentsAdapter;
import com.example.soulaid.dao.MomentsDao;
import com.example.soulaid.entity.MomentDetail;

import java.sql.Timestamp;

public class MomentDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name,title,content,time,likedCount;
    //private Button liked;

    private MomentDetail momentDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_detail);
        init();
    }
    private  void  init(){
        name=findViewById(R.id.name);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        time=findViewById(R.id.time);
        likedCount=findViewById(R.id.likedCount);

        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        momentDetail=(MomentDetail) bundle.getSerializable("moment");

        name.setText(momentDetail.getUname());
        title.setText(momentDetail.getTitle());
        content.setText(momentDetail.getContent());
        time.setText(momentDetail.getDatetime().toString());
        likedCount.setText(String.valueOf(momentDetail.getLiked()));

        //liked=findViewById(R.id.liked);
        //liked.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                MomentsDao momentsDao=new MomentsDao();
//                boolean state= momentsDao.addLikedCount(momentDetail.getId());
//                if(state){
//                    handler.sendEmptyMessage(1);
//                }else {
//                    handler.sendEmptyMessage(0);
//                }
//            }
//        }).start();
    }
//    @SuppressLint("HandlerLeak")
//    final Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message message) {
//            super.handleMessage(message);
//            //reTimes++;
//            switch (message.what) {
//                //点赞失败
//                case 0:
//                    Toast.makeText(MomentDetailActivity.this,"点赞失败",Toast.LENGTH_SHORT).show();
//                    break;
//                //点赞成功
//                case 1:
//                    Toast.makeText(MomentDetailActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
//                    //设置detailactivity的likedCount
//                    int num=Integer.parseInt( likedCount.getText().toString())+1;
//                    likedCount.setText(String.valueOf(num));
//                    break;
//            }
//        }
//    };
}
