package com.example.soulaid.user.ui.center;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.soulaid.R;
import com.example.soulaid.util.IOUtil;

public class UserMessageActivity extends AppCompatActivity {
    Intent intent=null;
    TextView username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        init();
    }
    void init(){
        intent=getIntent();
        username=findViewById(R.id.name);
        password=findViewById(R.id.pwd);
        String [] result;
        result= IOUtil.getUserInfo(UserMessageActivity.this);
        username.setText(result[0]);
        password.setText(result[1]);
    }
}
