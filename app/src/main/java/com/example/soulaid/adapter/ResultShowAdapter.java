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
import com.example.soulaid.user.ui.exercise.AnalysisActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ResultShowAdapter extends RecyclerView.Adapter {

    private Context context;
    private HashMap<String,Integer> result;
    private ArrayList<String> k=new ArrayList<>();
    private ArrayList<Integer> v=new ArrayList<>();

    public static class ItemHolder extends RecyclerView.ViewHolder {
        public TextView key,value;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.key);
            value=itemView.findViewById(R.id.value);
        }
    }

    //构造函数
    public ResultShowAdapter(Context context,HashMap<String,Integer> result){
        this.context=context;
        this.result=result;

        Set<String> keySet=result.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()){
            String t=iter.next();
            k.add(t);                  //这里之前有问题，应使用add() ，不能用set()，否则会出现越界问题
            v.add(result.get(t));
        }
        ((AnalysisActivity)context).setK(k);
        ((AnalysisActivity)context).setV(v);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResultShowAdapter.ItemHolder(LayoutInflater.from(this.context).inflate(R.layout.item_result_show, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder)holder).key.setText(k.get(position));
        ((ItemHolder)holder).value.setText(v.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }
}
