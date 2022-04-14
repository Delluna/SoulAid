package com.example.soulaid.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.soulaid.MainActivity;
import com.example.soulaid.R;
import com.example.soulaid.util.IOUtil;

public class AdminIndexActivity extends AppCompatActivity {

    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_index);
        init();
    }

    private void init() {
        exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean state = IOUtil.setUserType(view.getContext(), "");
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                IOUtil.setUserType(view.getContext(), "");
                startActivity(intent);
            }
        });
    }
}
