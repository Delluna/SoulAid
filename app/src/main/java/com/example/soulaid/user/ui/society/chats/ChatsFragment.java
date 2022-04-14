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
import com.example.soulaid.adapter.MomentsAdapter;
import com.example.soulaid.dao.MessageDao;
import com.example.soulaid.entity.MomentDetail;
import com.example.soulaid.entity.TeacherMessage;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;

    private List<TeacherMessage> teachers;

    private ChatsApater adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chats, container, false);
        init();
        return view;
    }

    private void init() {
        getTeachers();
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

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    teachers = message.getData().getParcelableArrayList("teachers");

                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    manager.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setLayoutManager(manager);
                    adapter = new ChatsApater(getContext(), teachers);
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
    };

}
