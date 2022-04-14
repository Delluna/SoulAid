package com.example.soulaid.user.ui.society;

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
import com.example.soulaid.user.ui.society.chats.ChatsFragment;
import com.example.soulaid.user.ui.society.moments.MomentsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class sIndexFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    private View view;

    private Fragment fm,fc,cur;
    private BottomNavigationView navView = null;
    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_society, container, false);
        init();
        return view;
    }
    private void init(){
        fm=new MomentsFragment();
        fc=new ChatsFragment();

        fragmentManager=getChildFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container,fm).commit();
        cur=fm;

        //设置顶部导航键
        navView = view.findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.moments:
                switchFragment(cur,fm);
                cur=fm;
                return true;
            case R.id.chat:
                switchFragment(cur,fc);
                cur=fc;
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