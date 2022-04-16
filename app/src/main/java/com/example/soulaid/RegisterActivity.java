package com.example.soulaid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soulaid.dao.MessageDao;

public class RegisterActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private TextView username, password, password2;
    private Button register;
    private RadioGroup radioGroup;
    private View titlebar;
    private TextView titlename;
    private ImageView exit;
    private String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        username = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        password2 = findViewById(R.id.editText3);
        register = findViewById(R.id.Register);
        radioGroup = findViewById(R.id.group);
        titlebar = findViewById(R.id.titlebar);
        titlename = titlebar.findViewById(R.id.name);
        titlename.setText("用户注册");
        exit = titlebar.findViewById(R.id.img_exit);

        radioGroup.setOnCheckedChangeListener(this);
        register.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.admin:
                tableName = "admin_message";
                break;
            case R.id.teacher:
                tableName = "teacher_message";
                break;
            case R.id.user:
                tableName = "user_message";
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Register:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String name, pwd, pwd2;
                        name = username.getText().toString();
                        pwd = password.getText().toString();
                        pwd2 = password2.getText().toString();

                        MessageDao MessageDao = new MessageDao();
                        int state = MessageDao.register(tableName, name, pwd, pwd2);
                        handler.sendEmptyMessage(state);
                    }
                }).start();
                break;
            case R.id.img_exit:
                finish();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 1) {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                finish(); //退出当前activity
            } else if (message.what == -1) {
                Toast.makeText(getApplicationContext(), "用户名已存在！", Toast.LENGTH_SHORT).show();
            } else if (message.what == -2) {
                Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "注册失败！", Toast.LENGTH_SHORT).show();
            }
        }
    };
}