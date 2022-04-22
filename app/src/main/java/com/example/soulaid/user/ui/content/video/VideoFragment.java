package com.example.soulaid.user.ui.content.video;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.soulaid.R;
import com.example.soulaid.adapter.VideosAdapter;
import com.example.soulaid.dao.VideosDao;
import com.example.soulaid.entity.ArticleDetail;
import com.example.soulaid.entity.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {
    private View view = null;
    private RecyclerView recyclerView;
    private VideosAdapter adapter;

    private List<Video> videos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_video, container, false);
        init();
        return view;
    }
    private void init(){
        recyclerView=view.findViewById(R.id.recycleView);
        //设置适配器
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        videos=new ArrayList<>();
        adapter=new VideosAdapter(getContext(),videos);
        recyclerView.setAdapter(adapter);

        getVideos();
    }

    private void getVideos(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                VideosDao videosDao=new VideosDao();
                List<Video> list = videosDao.getVideos();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("datalist", (ArrayList<? extends Parcelable>) list);
                Message message = Message.obtain();
                message.setData(bundle);
                message.what = 1;
                handler.sendMessage(message);

            }
        }).start();
    }

    public void setVideos(List<Video> videos){
        if(videos.size()==0){
            Toast.makeText(getContext(),"找不到您想要的内容！",Toast.LENGTH_SHORT).show();
        }else {
            this.videos.clear();
            this.videos.addAll(videos);
            adapter.notifyDataSetChanged();
        }

    }
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    List<Video> list = message.getData().getParcelableArrayList("datalist");
                    videos.addAll(list);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
