package com.example.soulaid.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soulaid.R;
import com.example.soulaid.entity.Video;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Video> videos;

    public static class ItemHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public VideoView videoView;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            videoView=itemView.findViewById(R.id.video);
        }
    }

    public VideosAdapter(Context context,List<Video> videos){
        this.context=context;
        this.videos=videos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(this.context).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Video video=videos.get(position);
        ((ItemHolder) holder).title.setText(video.getTitle());

        //设置video url为在线视频
        ((ItemHolder) holder).videoView.setVideoURI(Uri.parse(video.getUrl()));
        MediaController mediaController = new MediaController(context);
        ((ItemHolder) holder).videoView.setMediaController(mediaController);

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}
