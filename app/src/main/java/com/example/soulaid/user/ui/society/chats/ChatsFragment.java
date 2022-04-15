package com.example.soulaid.user.ui.society.chats;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soulaid.R;
import com.example.soulaid.adapter.ChatsApater;
import com.example.soulaid.dao.MessageDao;
import com.example.soulaid.entity.TeacherMessage;
import com.example.soulaid.entity.UserMessage;
import com.example.soulaid.util.IOUtil;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {
    private String userType;
    private View view;
    private RecyclerView recyclerView;

    private List<TeacherMessage> teachers;
    private List<UserMessage> users;

    private ChatsApater adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chats, container, false);
        init();
        return view;
    }

    private void init() {
        userType= IOUtil.getUserType(getContext());
        if(userType.equals("user")){
            getTeachers();
        }else{
            //userType.equals("teacher")
            getUsers();
        }

        recyclerView = view.findViewById(R.id.recycleView);
    }

    private void getTeachers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MessageDao messageDao = new MessageDao();
                List<TeacherMessage> teachers = messageDao.getTeachers();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("teachers", (ArrayList<? extends TeacherMessage>) teachers);
                Message message = Message.obtain();
                message.setData(bundle);
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    //获取所有与该老师聊过天的学生列表
    private void getUsers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MessageDao messageDao = new MessageDao();
                List<UserMessage> users = messageDao.getUsers(IOUtil.getUserInfo(getContext())[0]);

                //todo:可能会有users为空的情况，即该老师尚未与学生聊过天

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("users", (ArrayList<? extends UserMessage>) users);
                Message message = Message.obtain();
                message.setData(bundle);
                message.what = 2;
                handler.sendMessage(message);
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            switch (message.what) {
                //userType为学生，获取教师列表
                case 1:
                    teachers = message.getData().getParcelableArrayList("teachers");
                    adapter = new ChatsApater(getContext(), teachers,userType);
                    break;
                    //userType为教师，获取学生列表
                case 2:
                    users = message.getData().getParcelableArrayList("users");
                    adapter = new ChatsApater(getContext(), users);
                    break;
            }
            recyclerView.setAdapter(adapter);
        }
    };
}