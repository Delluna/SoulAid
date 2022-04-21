package com.example.soulaid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soulaid.R;
import com.example.soulaid.entity.Comment;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter {

    private List<Comment> comments;
    private Context context;

    public static class ItemHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView content;
        public TextView time;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            content=itemView.findViewById(R.id.content);
            time=itemView.findViewById(R.id.time);
        }
    }

    public CommentsAdapter(Context context,List<Comment> comments){
        this.comments=comments;
        this.context=context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder) holder).name.setText(comments.get(position).getUname());
        ((ItemHolder) holder).content.setText(comments.get(position).getContent());
        ((ItemHolder) holder).time.setText(comments.get(position).getDate().toString());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
