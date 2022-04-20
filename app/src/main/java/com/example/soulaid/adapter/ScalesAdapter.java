package com.example.soulaid.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soulaid.R;
import com.example.soulaid.entity.Scale;
import com.example.soulaid.user.ui.exercise.DescriptionActivity;
import com.example.soulaid.user.ui.exercise.ExerciseActivity;

import java.util.List;

public class ScalesAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Scale> scales;

    public ScalesAdapter(Context context,List<Scale> scales){
        this.context=context;
        this.scales=scales;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScalesAdapter.ItemHolder(LayoutInflater.from(this.context).inflate(R.layout.item_scale, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final Scale scale=scales.get(position);
        ((ItemHolder)holder).title.setText(position+1 +". "+scale.getName());

        ((ItemHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DescriptionActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("scale",scales.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scales.size();
    }
}
