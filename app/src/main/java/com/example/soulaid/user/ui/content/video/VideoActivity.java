package com.example.soulaid.user.ui.content.video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.soulaid.R;
import com.example.soulaid.entity.Video;

import cn.jzvd.JZVideoPlayerStandard;

public class VideoActivity extends AppCompatActivity {
    Video video;
    //VideoView videoView;
    //TextView textView;
    JZVideoPlayerStandard jzVideoPlayerStandard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        init();
    }

    private void init(){
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        video = (Video)bundle.getSerializable("video");

        //textView=findViewById(R.id.title);
       // textView.setText(video.getTitle());


        jzVideoPlayerStandard = findViewById(R.id.video);
        jzVideoPlayerStandard.setUp(video.getUrl(),JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,video.getTitle());
        jzVideoPlayerStandard.startVideo();

        //videoView=findViewById(R.id.video);
//        //设置video url为在线视频 (将本主机通过IIS技术配置为网站，然后系统可访问本机中的视频)
//        videoView.setVideoURI(Uri.parse(video.getUrl()));
//        MediaController mediaController = new MediaController(this);
//        videoView.setMediaController(mediaController);
//        videoView.start();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        jzVideoPlayerStandard.release();

        //清除文件播放缓存进度
       // JZVideoPlayerStandard.releaseAllVideos();
        //JZUtils.clearSavedProgress(this, null);
    }
}
