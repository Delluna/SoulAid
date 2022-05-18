package com.example.soulaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soulaid.dao.MessageDao;
import com.example.soulaid.user.UserIndexActivity;
import com.example.soulaid.util.IOUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private EditText username, password;
    private Button login;
    private RadioGroup radioGroup;
    private View titlebar;
    private TextView titlename;
    private ImageView exit;
    public String str_username, str_password, type;
    private String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        init();
    }

    private void init() {
        //获取账号密码，验证，登录用户信息
        username = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        login = findViewById(R.id.button);
        radioGroup = findViewById(R.id.group);
        titlebar=findViewById(R.id.titlebar);
        titlename=titlebar.findViewById(R.id.name);
        titlename.setText("用户登录");
        exit=titlebar.findViewById(R.id.img_exit);

        login.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
//            case R.id.admin:
//                tableName = "admin_message";
//                type = "admin";
//                break;
            case R.id.teacher:
                tableName = "teacher_message";
                type = "teacher";
                break;
            case R.id.user:
                tableName = "user_message";
                type = "user";
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                str_username = username.getText().toString();
                str_password = password.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MessageDao MessageDao = new MessageDao();
                        boolean state = MessageDao.login(tableName, str_username, str_password);
                        int msg = 0;
                        if (state) {
                            msg = 1;
                        }
                        handler.sendEmptyMessage(msg);
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

                //标记用户类型到“userType.txt”
                String path2 = "userType.txt";
                IOUtil.save(LoginActivity.this, type, path2);

                //将用户信息写入到文件"userInfo.txt"
                String path = "userInfo.txt";
                String userMessage = str_username + ";" + str_password;
                IOUtil.save(LoginActivity.this, userMessage, path);

                //页面跳转
                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = null;
                switch (type) {
//                    case "admin":
//                        intent = new Intent(LoginActivity.this, AdminIndexActivity.class);
//                        break;
                    case "teacher":
                        intent = new Intent(LoginActivity.this, UserIndexActivity.class);
                        break;
                    case "user":
                        intent = new Intent(LoginActivity.this, UserIndexActivity.class);
                        break;
                }
                startActivity(intent);
            } else {
                //登陆失败
                Toast toast = Toast.makeText(getApplicationContext(), "用户名或密码错误！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    };
}
