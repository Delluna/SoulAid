package com.example.soulaid.user.ui.exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.soulaid.R;
import com.example.soulaid.entity.Scale;

public class DescriptionActivity extends AppCompatActivity {
    private Scale scale;
    private TextView name,description;
    private Button begin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        init();

    }
    private void init() {
        Intent intent = getIntent();
        scale = (Scale) intent.getExtras().get("scale");

        name=findViewById(R.id.name);
        name.setText(scale.getName());
        description=findViewById(R.id.description);
        description.setText(scale.getDescription());
        begin=findViewById(R.id.begin);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DescriptionActivity.this, ExerciseActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("scale",scale);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
