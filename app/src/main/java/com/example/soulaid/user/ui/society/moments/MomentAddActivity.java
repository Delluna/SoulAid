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
import com.example.soulaid.dao.MomentsDao;
import com.example.soulaid.entity.MomentDetail;
import com.example.soulaid.util.IOUtil;

public class MomentAddActivity extends AppCompatActivity implements View.OnClickListener {

    private String username;
    private TextView title,content;
    private Button issue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_add);
        init();
    }

    private void init(){
        username= IOUtil.getUserInfo(MomentAddActivity.this)[0];
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        issue=findViewById(R.id.issue);
        issue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str_title,str_content;
                str_title=title.getText().toString();
                str_content=content.getText().toString();
                MomentsDao momentsDao=new MomentsDao();
                boolean state = momentsDao.addMoment(username,str_title,str_content);
                if(state){
                    handler.sendEmptyMessage(1);
                    getLastMoment();
                }else {
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private void getLastMoment(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MomentsDao momentsDao=new MomentsDao();
                MomentDetail momentDetail=momentsDao.getLastMoment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("moment",momentDetail);
                Message message=Message.obtain();
                message.what=2;
                message.setData(bundle);
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
                case 0:
                    Toast.makeText(MomentAddActivity.this,"添加失败",Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    Toast.makeText(MomentAddActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Intent intent = new Intent();
                    Bundle bundle = message.getData();
                    intent.putExtras(bundle);
                    setResult(1,intent);
                    finish();
                    break;
            }
        }
    };
}
