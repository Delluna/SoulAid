package com.example.soulaid.user.ui.society.chats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.soulaid.R;
import com.example.soulaid.entity.TeacherMessage;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name;
    private EditText text;
    private Button send;

    private TeacherMessage teacher;

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

        name.setText(teacher.getUsername());

        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //todo:发送消息

        String message = text.getText().toString();
    }
}
