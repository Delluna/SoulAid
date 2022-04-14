package com.example.soulaid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soulaid.R;
import com.example.soulaid.entity.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter {

    public static final int TYPE_SEND = 1;
    public static final int TYPE_RECEIVE = 0;

    private Context context;
    private List<ChatMessage> messages=new ArrayList<>();

    public ChatMessageAdapter(Context context,List<ChatMessage> messages){
        this.context=context;
        this.messages=messages;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        public TextView msg;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.chat_item_content_text);
        }
    }

    @Override
    public int getItemViewType(int position){
        return messages.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_SEND){
            return new ItemHolder(LayoutInflater.from(this.context).inflate(R.layout.item_message_send, parent, false));
        }else {
            return new ItemHolder(LayoutInflater.from(this.context).inflate(R.layout.item_message_receive, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder)holder).msg.setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
