package com.example.soulaid.user.ui.society.moments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;

import com.example.soulaid.R;
import com.example.soulaid.adapter.MomentsAdapter;
import com.example.soulaid.dao.MomentsDao;
import com.example.soulaid.entity.MomentDetail;

import java.util.ArrayList;
import java.util.List;


public class MomentsFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Button issue;
    private RecyclerView recyclerView;
    private List<MomentDetail> moments;
    private MomentsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_moments, container, false);

        init();
        return view;
    }


    private void init() {
        getMoments();

        issue = view.findViewById(R.id.issue);
        issue.setOnClickListener(this);  //通过点击按钮来发布内容

        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //滑动到底部 recyclerView.canScrollVertically(1（-1）)为false表示已经滑到底（顶）部
                if (!recyclerView.canScrollVertically(1)) {
                    //recyclerview滑动到底部,更新数据
                    List<MomentDetail> moreMoments = getMoreMoments();
                    if (moreMoments != null && moreMoments.size() != 0) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("more", (ArrayList<? extends MomentDetail>) moreMoments);
                        Message message = Message.obtain();
                        message.setData(bundle);
                        message.what = 2;
                        handler.sendMessage(message);
                    } else {
                        adapter.hasMore(false);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.issue:
                Intent intent = new Intent(getContext(), MomentAddActivity.class);
                startActivityForResult(intent, 1);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //对于MomentAddActivity页面
        if(requestCode==1&&resultCode==1){
            if (data != null) {
                Bundle bundle = data.getExtras();
                MomentDetail momentDetail = new MomentDetail();

                //添加数据 request==1 ,result==1
                momentDetail = (MomentDetail) bundle.getSerializable("moment");

                moments.add(0, momentDetail);
                adapter.notifyDataSetChanged();
            }
        }


    }

    private void getMoments() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MomentsDao momentsDao = new MomentsDao();
                moments = momentsDao.getMoments();
                Message message = Message.obtain();
                if (moments == null) {
                    message.what = 0;
                    handler.sendMessage(message);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("moments", (ArrayList<? extends Parcelable>) moments);

                    message.setData(bundle);
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private List<MomentDetail> getMoreMoments() {
        List<MomentDetail> moments = new ArrayList<>();
        //todo

        return moments;
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            //reTimes++;
            switch (message.what) {
                case 0:

                    break;
                case 1:
                    moments = message.getData().getParcelableArrayList("moments");

                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    manager.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setLayoutManager(manager);
                    adapter = new MomentsAdapter(getContext(), moments);
                    recyclerView.setAdapter(adapter);
                    break;
                case 2:
                    List<MomentDetail> momentDetails = message.getData().getParcelableArrayList("more");
                    moments.addAll(momentDetails);
                    adapter.hasMore(true);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
