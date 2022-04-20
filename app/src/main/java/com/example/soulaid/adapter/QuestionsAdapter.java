package com.example.soulaid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soulaid.R;
import com.example.soulaid.entity.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Question> questions;
    private List<String> answers;
    LinearLayoutManager manager;
    private AnswersAdapter adapter;
    public QuestionsAdapter(Context context,List<Question> questions, List<String> answers){
        this.context=context;
        this.questions=questions;
        this.answers=answers;
    }

    //嵌套recycleview
    public static class ItemHolder extends RecyclerView.ViewHolder {
        public TextView question;
        public RecyclerView answer;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.recycleView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionsAdapter.ItemHolder(LayoutInflater.from(this.context).inflate(R.layout.item_question, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder)holder).question.setText(position+1+". " +questions.get(position).getQuestion());

        //设置adapter
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ((ItemHolder)holder).answer.setLayoutManager(manager);
         adapter=new AnswersAdapter(context,answers,position);
        ((ItemHolder)holder).answer.setAdapter(adapter);


    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

}
