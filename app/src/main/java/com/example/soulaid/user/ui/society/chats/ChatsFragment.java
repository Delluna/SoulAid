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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.soulaid.R;
import com.example.soulaid.adapter.ChatsApater;
import com.example.soulaid.dao.MessageDao;
import com.example.soulaid.entity.TeacherMessage;
import com.example.soulaid.entity.UserMessage;
import com.example.soulaid.util.IOUtil;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment implements View.OnClickListener {
    private String userType;
    private View view;
    private View R1;
    private EditText text;
    private ImageView search;
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
        userType = IOUtil.getUserType(getContext());
        if (userType.equals("user")) {
            getTeachers();
        } else {
            //userType.equals("teacher")
            R1 = view.findViewById(R.id.R1);
            R1.setVisibility(View.VISIBLE);
            text = view.findViewById(R.id.text);
            search = view.findViewById(R.id.search);
            search.setOnClickListener(this);
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

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("users", (ArrayList<? extends UserMessage>) users);
                Message message = Message.obtain();
                message.setData(bundle);
                message.what = 2;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                final String str_text = text.getText().toString();
                if (str_text.equals("")) {
                    Toast.makeText(getContext(), "请输入学生姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MessageDao messageDao = new MessageDao();
                        UserMessage userMessage = messageDao.findUserByName("user_message", str_text);
                        if (userMessage == null) {
                            handler.sendEmptyMessage(4);
                        } else if (users.contains(userMessage)) {  //此处需在userMessage中重写equals方法
                            handler.sendEmptyMessage(5);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("userMessage", userMessage);
                            Message message = Message.obtain();
                            message.setData(bundle);
                            message.what = 3;
                            handler.sendMessage(message);
                        }
                    }
                }).start();

                break;
        }
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
                    adapter = new ChatsApater(getContext(), teachers, userType);
                    recyclerView.setAdapter(adapter);
                    break;
                //userType为教师，获取学生列表
                case 2:
                    users = message.getData().getParcelableArrayList("users");
                    adapter = new ChatsApater(getContext(), users);
                    recyclerView.setAdapter(adapter);
                    break;
                case 3:
                    UserMessage userMessage = (UserMessage) message.getData().getSerializable("userMessage");
                    users.add(userMessage);
                    adapter.notifyDataSetChanged();
                    break;
                case 4:
                    Toast.makeText(getContext(), "无此学生", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(getContext(), "此学生已在列表中", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

}