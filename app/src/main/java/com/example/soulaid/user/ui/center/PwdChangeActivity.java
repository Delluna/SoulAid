package com.example.soulaid.user.ui.center;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soulaid.R;
import com.example.soulaid.dao.MessageDao;
import com.example.soulaid.util.IOUtil;

import java.io.FileOutputStream;
import java.io.IOException;

public class PwdChangeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView name, pwd1, pwd2;
    private Button commit;
    private String tableName, username, password, password1, password2;//password为密码，password1为验证密码，password2为修改密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_change);
        init();
    }

    private void init() {
        name = findViewById(R.id.name);
        pwd1 = findViewById(R.id.pwd);
        pwd2 = findViewById(R.id.pwd_changed);
        commit = findViewById(R.id.btn_change_pwd);

        switch (IOUtil.getUserType(PwdChangeActivity.this)){
            case "admin":
                tableName = "admin_message";
                break;
            case "teacher":
                tableName = "teacher_message";
                break;
            case "user":
                tableName = "user_message";
                break;
        }

        String [] result;
        result= IOUtil.getUserInfo(PwdChangeActivity.this);
        name.setText(result[0]);
        username=result[0];
        password=result[1];

        commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_pwd:
                password1 = pwd1.getText().toString();
                password2 = pwd2.getText().toString();

                if (!password.equals(password1)) {    //密码验证失败
                    Toast.makeText(PwdChangeActivity.this, "密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                } else if (password.equals(password2)) {      //修改后的密码与原密码相同
                    Toast.makeText(PwdChangeActivity.this, "修改后的密码与原密码相同", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MessageDao userMessageDao = new MessageDao();
                            System.out.println("正在执行Thread");
                            boolean state = userMessageDao.changePassword(tableName,username, password2);
                            System.out.println("state状态" + state);
                            int msg = 0;
                            if (state) {
                                msg = 1;
                            }
                            handler.sendEmptyMessage(msg);
                        }
                    }).start();
                }
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Toast toast;
            if (message.what == 1) {
                password=password2;
                String path = "userInfo.txt";
                String userMessage = username + ";" + password2;
                save(userMessage, path);
                toast = Toast.makeText(PwdChangeActivity.this, "密码修改成功", Toast.LENGTH_SHORT);

            } else {
                toast = Toast.makeText(PwdChangeActivity.this, "密码修改失败！", Toast.LENGTH_SHORT);
            }
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            pwd1.setText(password2);
            pwd2.setText("");
        }
    };

    private void save(String userMessage, String path) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(path, MODE_PRIVATE);
            fileOutputStream.write(userMessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
