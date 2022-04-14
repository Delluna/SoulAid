package com.example.soulaid.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soulaid.R;
import com.example.soulaid.entity.TeacherMessage;
import com.example.soulaid.user.ui.society.chats.ChatActivity;

import java.util.List;

public class ChatsApater extends RecyclerView.Adapter {
    private Context context;
    private List<TeacherMessage> teachers;

    public static class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView icon;
        public TextView name;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            icon=itemView.findViewById(R.id.icon);
            name=itemView.findViewById(R.id.name);
        }
    }

    public ChatsApater(Context context, List<TeacherMessage> teachers){
        this.context=context;
        this.teachers=teachers;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_chat,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((ItemHolder)holder).name.setText(teachers.get(position).getUsername());

        //设置点击事件
        ((ItemHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, ChatActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("teacher",teachers.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }
}