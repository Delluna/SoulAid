package com.example.soulaid.user.ui.content;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.soulaid.R;
import com.example.soulaid.dao.ArticlesDao;
import com.example.soulaid.dao.VideosDao;
import com.example.soulaid.entity.ArticleDetail;
import com.example.soulaid.entity.Video;
import com.example.soulaid.user.UserIndexActivity;
import com.example.soulaid.user.ui.content.article.ArticleFragment;
import com.example.soulaid.user.ui.content.video.VideoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class cIndexFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private View view = null;
    private EditText text;
    private ImageView img_logo, img_search;

    private BottomNavigationView navView = null;
    private Fragment fa, fv, cur;
    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_content, container, false);
        init();
        return view;
    }

    private void init() {
        fa = new ArticleFragment();
        fv = new VideoFragment();

        fragmentManager = getChildFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fa, fa.getClass().getName()).commit();
        cur = fa;

        //设置顶部导航键
        navView = view.findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        //点击头像跳转至center页面
        img_logo = view.findViewById(R.id.img_userlogo);
        img_logo.setOnClickListener(this);

        //查询功能
        text = view.findViewById(R.id.text);
        img_search = view.findViewById(R.id.search);
        img_search.setOnClickListener(this);
        //按下回车执行搜索操作，代码写的不对
//        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if(i== EditorInfo.IME_NULL){
//                    img_search.callOnClick();
//                }
//                return false;
//            }
//        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.article:
                switchFragment(cur, fa);
                return true;
            case R.id.video:
                switchFragment(cur, fv);
                return true;
        }
        return false;
    }

    public void switchFragment(Fragment cur, Fragment to) {
        fragmentTransaction = fragmentManager.beginTransaction();
        if (!to.isAdded()) {
            fragmentTransaction.hide(cur).add(R.id.container, to, to.getClass().getName()).commit();
        } else {
            fragmentTransaction.hide(cur).show(to).commit();
        }
        this.cur = to;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_userlogo:
                UserIndexActivity parent = (UserIndexActivity) getActivity();
                parent.switchFragment(parent.getCur(), parent.getU());
                //解决BottomNavigationView 的setSelectedItemId方法无法选中MenuItem https://blog.csdn.net/u010152805/article/details/90905034
                parent.getNavView().getMenu().getItem(3).setChecked(true);
                break;
            case R.id.search:
                final String key = text.getText().toString();
                    if (cur == fa) {
                        //搜索文章
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ArticlesDao articlesDao = new ArticlesDao();
                                List<ArticleDetail> articles;
                                if (key.equals("")){
                                    articles = articlesDao.getContents();
                                }else {
                                    articles = articlesDao.getArticlesWithKey(key);
                                }
                                Bundle bundle = new Bundle();
                                bundle.putParcelableArrayList("articles", (ArrayList<? extends Parcelable>) articles);
                                Message message = Message.obtain();
                                message.setData(bundle);
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        }).start();

                    } else {
                        //搜索视频
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                VideosDao videosDao = new VideosDao();
                                List<Video> videos;
                                if (key.equals("")){
                                    videos=videosDao.getVideos();}
                                else {
                                    videos  = videosDao.getVideosWithKey(key);}
                                Bundle bundle = new Bundle();
                                bundle.putParcelableArrayList("videos", (ArrayList<? extends Parcelable>) videos);
                                Message message = Message.obtain();
                                message.setData(bundle);
                                message.what = 2;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    //搜索文章
                    List<ArticleDetail> articles = message.getData().getParcelableArrayList("articles");
                    ((ArticleFragment) fa).setArticles(articles);
                    break;
                case 2:
                    //搜索视频
                    List<Video> videos = message.getData().getParcelableArrayList("videos");
                    ((VideoFragment) fv).setVideos(videos);
                    break;
            }
        }
    };
}