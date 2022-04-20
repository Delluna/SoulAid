package com.example.soulaid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.soulaid.admin.AdminIndexActivity;
import com.example.soulaid.user.UserIndexActivity;
import com.example.soulaid.util.IOUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断之前是否登陆过，若登陆过直接跳转到首页
        String type = IOUtil.getUserType(MainActivity.this);
        switch (type) {
            case "admin":
                Intent intent = new Intent(MainActivity.this, AdminIndexActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
                break;
            case "teacher":
                Intent intent2 = new Intent(MainActivity.this, UserIndexActivity.class);
                startActivity(intent2);
                MainActivity.this.finish();
                break;
            case "user":
                Intent intent3 = new Intent(MainActivity.this, UserIndexActivity.class);
                startActivity(intent3);
                MainActivity.this.finish();
                break;
            default:
                setContentView(R.layout.activity_main);
                init();
                break;
        }
    }

    private void init() {
        btn_login = findViewById(R.id.btn_loginssss);
        btn_register = findViewById(R.id.btn_register);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_loginssss:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                break;
            case R.id.btn_register:
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                break;
        }
        startActivity(intent);
    }
}
