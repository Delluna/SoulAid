package com.example.soulaid.user.ui.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.soulaid.R;
import com.example.soulaid.user.ui.content.article.ArticleFragment;
import com.example.soulaid.user.ui.content.video.VideoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class cIndexFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    private View view = null;

    private BottomNavigationView navView = null;
    private Fragment fa,fv,cur;
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

        fragmentManager=getChildFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container,fa,fa.getClass().getName()).commit();
        cur=fa;

        //设置顶部导航键
        navView = view.findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.article:
                switchFragment(cur,fa);
                cur=fa;
                return true;
            case R.id.video:
                switchFragment(cur,fv);
                cur=fv;
                return true;
        }
        return false;
    }

    public void switchFragment(Fragment cur , Fragment to){
        fragmentTransaction=fragmentManager.beginTransaction();
        if(!to.isAdded()){
            fragmentTransaction.hide(cur).add(R.id.container,to,to.getClass().getName()).commit();
        }else {
            fragmentTransaction.hide(cur).show(to).commit();
        }
    }
}