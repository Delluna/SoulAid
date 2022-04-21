package com.example.soulaid.user.ui.society.chats;

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
import com.example.soulaid.dao.CommentsDao;
import com.example.soulaid.entity.Comment;
import com.example.soulaid.entity.MomentDetail;
import com.example.soulaid.util.IOUtil;

public class CommentAddActivity extends AppCompatActivity implements View.OnClickListener {

    private String username;
    private TextView content;
    private Button issue;

    private MomentDetail momentDetail;
    private Comment comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_add);
        init();
    }
    private void init(){
        username= IOUtil.getUserInfo(CommentAddActivity.this)[0];
        content=findViewById(R.id.content);
        issue=findViewById(R.id.issue);

        Intent intent=getIntent();
        Bundle bundle =intent.getExtras();
        momentDetail=(MomentDetail) bundle.getSerializable("momentDetail");

        comment=new Comment();
        issue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str_title,str_content;
                str_content=content.getText().toString();
                CommentsDao commentsDao =new CommentsDao();
                boolean state = commentsDao.addComment(username,momentDetail.getId(),str_content);
                if(state){
                    handler.sendEmptyMessage(1);
                    getLastComment();
                }else {
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private void getLastComment() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommentsDao commentsDao = new CommentsDao();
                Comment comment = commentsDao.getLastComment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("comment", comment);
                Message message = Message.obtain();
                message.what = 2;
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
                    Toast.makeText(CommentAddActivity.this,"添加失败",Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    Toast.makeText(CommentAddActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
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
