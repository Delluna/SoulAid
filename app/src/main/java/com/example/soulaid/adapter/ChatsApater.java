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
import com.example.soulaid.entity.UserMessage;
import com.example.soulaid.user.ui.society.chats.ChatActivity;
import com.example.soulaid.util.IOUtil;

import java.util.List;

public class ChatsApater extends RecyclerView.Adapter {
    private String userType;
    private Context context;
    private List<TeacherMessage> teachers;
    private List<UserMessage> users;

    public static class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView icon;
        public TextView name;
        public TextView tag;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            icon=itemView.findViewById(R.id.icon);
            name=itemView.findViewById(R.id.name);
            tag=itemView.findViewById(R.id.tag);
        }
    }

    public ChatsApater(Context context, List<TeacherMessage> teachers,String userType){
        this.userType=userType;
        this.context=context;
        this.teachers=teachers;
    }

    public ChatsApater(Context context, List<UserMessage> users){
        userType= IOUtil.getUserType(context);
        this.context=context;
        this.users=users;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_chat,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(userType.equals("user")) {
            ((ItemHolder) holder).name.setText(teachers.get(position).getUsername());
            //设置教师的专业领域
            String speacialize="";
            switch (teachers.get(position).getTag()){
                case 0:
                    speacialize="心理咨询老师";
                    break;
                case 1:
                    speacialize="性格咨询老师";
                    break;
                case 2:
                    speacialize="心理健康咨询老师";
                    break;
                case 3:
                    speacialize="人际交往咨询老师";
                    break;
                case 4:
                    speacialize="恋爱咨询老师";
                    break;
            }
            ((ItemHolder) holder).tag.setText(speacialize);
        }else {
            ((ItemHolder) holder).name.setText(users.get(position).getUsername());
            ((ItemHolder) holder).tag.setText("");
        }

        //设置点击事件
        ((ItemHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, ChatActivity.class);
                Bundle bundle=new Bundle();
                if(userType.equals("user")){
                    bundle.putSerializable("teacher",teachers.get(position));
                }else {
                    bundle.putSerializable("user",users.get(position));
                }

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userType.equals("user")){
            return teachers.size();
        }else {
            return users.size();
        }

    }
}