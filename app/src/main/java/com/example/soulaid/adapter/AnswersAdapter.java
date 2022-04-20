package com.example.soulaid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soulaid.R;
import com.example.soulaid.user.ui.exercise.ExerciseActivity;

import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter {
    private int layoutPosition;
    private int question_position;
    private Context context;
    private List<String> answers;

    public AnswersAdapter(Context context, List<String> answers,int question_position) {
        this.context = context;
        this.answers = answers;
        this.question_position=question_position;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        public RadioButton answer;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.answer);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnswersAdapter.ItemHolder(LayoutInflater.from(this.context).inflate(R.layout.item_answer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((ItemHolder) holder).answer.setText(answers.get(position));

        //为radiobutton设置点击事件:点击该radiobutton时，其他被取消
        final ItemHolder holder1=(ItemHolder) holder;
        holder1.answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutPosition=holder1.getLayoutPosition();
                notifyDataSetChanged();
                ExerciseActivity.scores.set(question_position,position);

            }
        });
        if (layoutPosition==position){
            holder1.answer.setChecked(true);
        }else {
            holder1.answer.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }
}
