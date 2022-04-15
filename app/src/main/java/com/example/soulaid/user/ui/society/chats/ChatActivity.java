package com.example.soulaid.user.ui.society.chats;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soulaid.R;
import com.example.soulaid.WebSocket.ChatClient;
import com.example.soulaid.adapter.ChatMessageAdapter;
import com.example.soulaid.adapter.ChatsApater;
import com.example.soulaid.adapter.MomentsAdapter;
import com.example.soulaid.dao.ChatMessageDao;
import com.example.soulaid.entity.ChatMessage;
import com.example.soulaid.entity.MomentDetail;
import com.example.soulaid.entity.TeacherMessage;
import com.example.soulaid.entity.UserMessage;
import com.example.soulaid.util.IOUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name;
    private EditText text;
    private Button send;
    private ImageView exit;
    private RecyclerView recyclerView;

    private URI serverURI = ChatClient.serverURI;
    private ChatClient chatClient;

    private String userType;
    private String username;
    private ChatMessageAdapter adapter;

    private UserMessage user;
    private TeacherMessage teacher;
    private List<ChatMessage> messages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        //重载onMessage方法,并连接服务器。
        chatClient = new ChatClient(serverURI) {
            @Override
            public void onMessage(final String message) {
                System.out.println("received: " + message);
                Message received = Message.obtain();
                received.what = 0;
                received.obj = message;
                handler.sendMessage(received);
            }
        };
        chatClient.connect();
    }

    private void init() {
        userType = IOUtil.getUserType(ChatActivity.this);
        name = findViewById(R.id.name);
        //获取教师、学生信息
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username = IOUtil.getUserInfo(ChatActivity.this)[0];
        if (userType.equals("user")) {
            teacher = (TeacherMessage) bundle.getSerializable("teacher");
            name.setText(teacher.getUsername());
        } else {
            user = (UserMessage) bundle.getSerializable("user");
            name.setText(user.getUsername());
        }

        //找到控件并赋值、设置监听事件

        text = findViewById(R.id.text);
        send = findViewById(R.id.send);
        recyclerView = findViewById(R.id.recycleView);
        View titlebar = findViewById(R.id.titlebar);
        exit = titlebar.findViewById(R.id.img_exit);


        send.setOnClickListener(this);
        exit.setOnClickListener(this);

        //为recycleviwe设置适配器adapter
        getChats();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                String message = text.getText().toString();
                sendMessage(message);
                text.setText("");
                break;
            case R.id.img_exit:
                finish();
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getChats() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatMessageDao chatMessageDao = new ChatMessageDao();
                List<ChatMessage> messages;
                if(userType.equals("user")) {
                    messages = chatMessageDao.getAllChats(teacher.getUsername(), username);
                }else {
                    messages = chatMessageDao.getAllChats(username,user.getUsername());
                }
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("chats", (ArrayList<? extends Parcelable>) messages);
                message.setData(bundle);
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void sendMessage(final String message) {

        if (chatClient.isClosed() || chatClient.isClosing()) {
            chatClient.connect();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg = message;
                String uType = userType;
                String total;
                if (uType.equals("user")) {
                    //type:1 发送：学生->老师
                    total = "sender:" + username + ";receiver:" + teacher.getUsername() + ";message:" + msg + ";type:1";
                } else {
                    //type:0 发送：老师->学生
                    total = "sender:" + username + ";receiver:" + user.getUsername() + ";message:" + msg + ";type:0";
                }
                chatClient.send(total);
            }
        }).start();

        //通知adapter更新
        ChatMessage newMsg = new ChatMessage();
        newMsg.setMessage(message);
        if (userType.equals("user")) {
        newMsg.setUname(username);
        newMsg.setTname(teacher.getUsername());}
        else {
            newMsg.setUname(user.getUsername());
            newMsg.setTname(username);
        }
        newMsg.setType(1); //这里不管是teacher还是user都是1，因为都是发送消息
        messages.add(newMsg);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messages.size() - 1);

        //
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                //接收服务器发来的消息 ：
                case 0:
                    String received = message.obj.toString();
                    //消息格式为： receiver:xx;message:xx
                    //使用正则表达式进行匹配
                    String regex = "^receiver";
                    if (received.matches(regex)) {

                        String receiver = received.split(";")[1].split(":")[1];
                        String receiveData = received.split(";")[2].split(":")[1];
                        if (receiver.equals(username)) {
                            ChatMessage newMsg = new ChatMessage();
                            newMsg.setMessage(receiveData);
                            newMsg.setUname(username);
                            newMsg.setTname(teacher.getUsername());
                            newMsg.setType(0); //这里不管是teacher还是user都是1，因为都是接收消息
                            //通知adapter更新
                            messages.add(newMsg);
                            adapter.notifyDataSetChanged();
                            //设置RecyclerView 移动到底部
                            recyclerView.scrollToPosition(messages.size() - 1);
                        }
                    }

                    break;
                //设置适配器
                case 1:
                    messages = message.getData().getParcelableArrayList("chats");
                    //type:1 学生->老师  |type:0 发送：老师->学生
                    ////若用户为老师，则0应为send,1为receive
                    //由于adapter的原因，需对取出后的数据加以处理，保证消息位置(左、右)正确
                    if(userType.equals("teacher")){
                        for(int i=0;i<messages.size();i++){
                            messages.get(i).setType(1-messages.get(i).getType());
                        }
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(ChatActivity.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setLayoutManager(manager);
                    adapter = new ChatMessageAdapter(ChatActivity.this, messages);
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
    };
}
