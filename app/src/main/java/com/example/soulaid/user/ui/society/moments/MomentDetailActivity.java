package com.example.soulaid.user.ui.society.moments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soulaid.R;
import com.example.soulaid.adapter.ArticlesAdapter;
import com.example.soulaid.adapter.CommentsAdapter;
import com.example.soulaid.adapter.MomentsAdapter;
import com.example.soulaid.dao.ArticlesDao;
import com.example.soulaid.dao.CommentsDao;
import com.example.soulaid.dao.MomentsDao;
import com.example.soulaid.entity.ArticleDetail;
import com.example.soulaid.entity.Comment;
import com.example.soulaid.entity.MomentDetail;
import com.example.soulaid.user.ui.society.chats.CommentAddActivity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MomentDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name, title, content, time, likedCount;
    private Button issue;
    private RecyclerView recyclerView;
    private List<Comment> comments;
    private CommentsAdapter adapter;

    private MomentDetail momentDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_detail);
        init();
    }

    private void init() {
        name = findViewById(R.id.name);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        time = findViewById(R.id.time);
        likedCount = findViewById(R.id.likedCount);
        issue = findViewById(R.id.issue);
        recyclerView = findViewById(R.id.recycleView);

        issue.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        momentDetail = (MomentDetail) bundle.getSerializable("moment");

        name.setText(momentDetail.getUname());
        title.setText(momentDetail.getTitle());
        content.setText(momentDetail.getContent());
        time.setText(momentDetail.getDatetime().toString());
        likedCount.setText(String.valueOf(momentDetail.getLiked()));

        //评论功能
        LinearLayoutManager manager = new LinearLayoutManager(MomentDetailActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        comments = new ArrayList<>();
        adapter = new CommentsAdapter(MomentDetailActivity.this, comments);
        recyclerView.setAdapter(adapter);

        getComments();
    }

    private void getComments() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommentsDao commentsDao = new CommentsDao();
                List<Comment> list = commentsDao.getComments(momentDetail.getId());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("datalist", (ArrayList<? extends Parcelable>) list);
                Message message = Message.obtain();
                message.setData(bundle);
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.issue:
                Intent intent = new Intent(MomentDetailActivity.this, CommentAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("momentDetail",momentDetail);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            Comment comment = new Comment();

            //添加数据 request==1 ,result==1
            comment = (Comment) bundle.getSerializable("comment");

            comments.add(0, comment);
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 0:
                    break;
                case 1:
                    List<Comment> list = message.getData().getParcelableArrayList("datalist");
                    comments.addAll(list);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
