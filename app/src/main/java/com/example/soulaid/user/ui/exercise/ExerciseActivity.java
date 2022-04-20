package com.example.soulaid.user.ui.exercise;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.soulaid.R;
import com.example.soulaid.adapter.QuestionsAdapter;
import com.example.soulaid.dao.QuestionsDao;
import com.example.soulaid.entity.Question;
import com.example.soulaid.entity.Scale;
import com.example.soulaid.util.CalculateUtil;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    public static  ArrayList scores=new ArrayList();

    private Scale scale;
    private List<Question> questions=new ArrayList<>();
    private List<String> answers=new ArrayList<>();
    private TextView name;
    private Button finish;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private QuestionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        scale = (Scale) intent.getExtras().get("scale");

        name=findViewById(R.id.name);
        name.setText(scale.getName());

        finish=findViewById(R.id.finish);
        finish.setOnClickListener(this);

        int answerNumber = scale.getAnswerNumber();
        for (int i=0;i<answerNumber;i++){
            String answer=scale.getAnswerDeatail().split(";")[i];
            answers.add(answer);
        }

        recyclerView=findViewById(R.id.recycleView);
        getQuestions();
    }

    private void getQuestions(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                QuestionsDao questionsDao =new QuestionsDao();
                List<Question> list = questionsDao.getQuestions(scale.getName());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("datalist", (ArrayList<? extends Parcelable>) list);
                Message message = Message.obtain();
                message.setData(bundle);
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case 1:
                    questions=message.getData().getParcelableArrayList("datalist");
                    manager = new LinearLayoutManager(ExerciseActivity.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(manager);
                    adapter=new QuestionsAdapter(ExerciseActivity.this,questions,answers);
                    recyclerView.setAdapter(adapter);

                    //初始化
                    for(int i=0;i<answers.size();i++){
                        scores.add(0);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onRestart(){
        super.onRestart();

        //清空并初始化
        scores.clear();
        for(int i=0;i<answers.size();i++){
            scores.add(0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finish:
                CalculateUtil calculateUtil=new CalculateUtil(scores);
                int result=calculateUtil.calcuByName(scale.getName());

                Intent intent=new Intent(ExerciseActivity.this, AnalysisActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("scale",scale);
                bundle.putInt("score",result);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onRestart();
        finish();
    }
}