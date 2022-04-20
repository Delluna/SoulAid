package com.example.soulaid.user.ui.exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.soulaid.R;
import com.example.soulaid.entity.Scale;

public class AnalysisActivity extends AppCompatActivity {
    private Scale scale;
    private TextView score,name,analysis;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        init();
    }
    private void init(){
        Intent intent = getIntent();
        scale = (Scale) intent.getExtras().get("scale");
        result = intent.getExtras().getInt("score");

        name=findViewById(R.id.name);
        name.setText(scale.getName());
        score=findViewById(R.id.score);
        score.setText(score.getText()+Integer.toString(result)+"分");
        analysis=findViewById(R.id.analysis);
        analysis.setText(scale.getAnswerAnalysis());
    }
}
