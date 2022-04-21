package com.example.soulaid.user;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.soulaid.R;
import com.example.soulaid.user.ui.center.uIndexFragment;
import com.example.soulaid.user.ui.society.sIndexFragment;
import com.example.soulaid.user.ui.content.cIndexFragment;
import com.example.soulaid.user.ui.exercise.eIndexFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class UserIndexActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Fragment c, s, e, u, cur;
    private BottomNavigationView navView = null;
    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_index);
        init();
    }

    void init() {
        c = new cIndexFragment();
        s = new sIndexFragment();
        e = new eIndexFragment();
        u = new uIndexFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, c,c.getClass().getName()).commit();
        cur = c;

        //设置底部导航键
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.content:
                switchFragment(cur, c);
                return true;
            case R.id.socialTouch:
                switchFragment(cur, s);
                return true;
            case R.id.exercise:
                switchFragment(cur, e);
                return true;
            case R.id.userCenter:
                switchFragment(cur, u);
                return true;
        }
        return false;
    }

    public Fragment getC() {
        return c;
    }

    public Fragment getS() {
        return s;
    }

    public Fragment getE() {
        return e;
    }

    public Fragment getU() {
        return u;
    }

    public Fragment getCur() {
        return cur;
    }

    public BottomNavigationView getNavView() {
        return navView;
    }

    public void switchFragment(Fragment cur, Fragment to) {
        fragmentTransaction = fragmentManager.beginTransaction();
        if (!to.isAdded()) {
            fragmentTransaction.hide(cur).add(R.id.container, to,to.getClass().getName()).commit();
        } else {
            fragmentTransaction.hide(cur).show(to).commit();
        }
        this.cur=to;
    }
}