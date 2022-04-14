package com.example.soulaid.user.ui.society.chats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soulaid.R;
import com.example.soulaid.entity.ChatMessage;
import com.example.soulaid.entity.TeacherMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name;
    private EditText text;
    private Button send;
    private ImageView exit;

    private TeacherMessage teacher;
    private List<ChatMessage> messages=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }
    private void init(){
        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();
        teacher = (TeacherMessage) bundle.getSerializable("teacher");

        name=findViewById(R.id.name);
        text=findViewById(R.id.text);
        send=findViewById(R.id.send);

        View titlebar=findViewById(R.id.titlebar);
        exit=titlebar.findViewById(R.id.exit);

        name.setText(teacher.getUsername());

        send.setOnClickListener(this);
        exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send:
                //todo:发送消息

                String message = text.getText().toString();
                sendMessage(message);
                text.setText("");
                break;
            case R.id.exit:
                finish();
                break;
        }

    }

    private void sendMessage(String message){

    }
}
