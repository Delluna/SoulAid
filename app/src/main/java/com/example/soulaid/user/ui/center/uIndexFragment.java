package com.example.soulaid.user.ui.center;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.soulaid.MainActivity;
import com.example.soulaid.R;
import com.example.soulaid.util.IOUtil;


public class uIndexFragment extends Fragment implements View.OnClickListener {

    private View view = null;
    private Intent intent = null;
    private TextView username = null;
    private Button btn_detail, btn_change_pwd, btn_favorite, btn_exit;
    private String uname = "用户名", pwd = "123456";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_center, container, false);
        init();
        return view;
    }

    private void init() {
        username = view.findViewById(R.id.username);
        btn_detail = view.findViewById(R.id.btn_detail);
        btn_change_pwd = view.findViewById(R.id.btn_change_pwd);
        btn_favorite = view.findViewById(R.id.btn_favorite);
        btn_exit = view.findViewById(R.id.btn_exit);

        btn_detail.setOnClickListener(this);
        btn_change_pwd.setOnClickListener(this);
        btn_favorite.setOnClickListener(this);
        btn_exit.setOnClickListener(this);

        String [] result;
        result=IOUtil.getUserInfo(getContext());
        username.setText(result[0]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //跳转至用户信息详情页面
            case R.id.btn_detail:
                intent = new Intent(view.getContext(), UserMessageActivity.class);
                break;
                //跳转到修改密码界面
            case R.id.btn_change_pwd:
                intent = new Intent(view.getContext(),PwdChangeActivity.class);
                break;
            //跳转到收藏activity
            case R.id.btn_favorite:
                intent = new Intent(view.getContext(), UserFavoriteActivity.class);
                break;
            //跳转回MainActivity
            case R.id.btn_exit:
                intent = new Intent(view.getContext(), MainActivity.class);
                IOUtil.setUserType(view.getContext(),"");
                break;
        }
        startActivity(intent);
    }
}