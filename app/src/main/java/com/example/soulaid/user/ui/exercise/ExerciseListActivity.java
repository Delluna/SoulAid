package com.example.soulaid.user.ui.exercise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.Gravity;
import android.widget.Toast;

import com.example.soulaid.LoginActivity;
import com.example.soulaid.R;
import com.example.soulaid.adapter.ScalesAdapter;
import com.example.soulaid.admin.AdminIndexActivity;
import com.example.soulaid.dao.ExerciseListDao;
import com.example.soulaid.entity.Scale;
import com.example.soulaid.user.UserIndexActivity;
import com.example.soulaid.util.IOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExerciseListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Scale> scales;
    String typename;
    private ScalesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);
        init();
    }

    private void init(){
        Intent intent=getIntent();
        typename=(String) intent.getExtras().get("type");

        recyclerView=findViewById(R.id.recycleView);
        getScales();
    }
    private  void getScales(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ExerciseListDao exerciseListDao=new ExerciseListDao();
                List<Scale> list = exerciseListDao.getScales(typename);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("datalist", (ArrayList<? extends Parcelable>) list);
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
            switch (message.what){
                case 1:
                    scales=message.getData().getParcelableArrayList("datalist");
                    LinearLayoutManager manager = new LinearLayoutManager(ExerciseListActivity.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(manager);
                    adapter=new ScalesAdapter(ExerciseListActivity.this,scales);
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
    };
}