package com.example.soulaid.user.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.soulaid.R;

public class eIndexFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button test_disposition,test_mentality,test_relationship,test_love;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_exercise, container, false);
        init();
        return view;
    }
    private void init(){
        test_disposition=view.findViewById(R.id.test_disposition);
        test_mentality=view.findViewById(R.id.test_mentality);
        test_relationship=view.findViewById(R.id.test_relationship);
        test_love=view.findViewById(R.id.test_love);

        test_disposition.setOnClickListener(this);
        test_mentality.setOnClickListener(this);
        test_relationship.setOnClickListener(this);
        test_love.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(getActivity(),ExerciseListActivity.class);
        Bundle bundle=new Bundle();
        switch (view.getId()){
            case R.id.test_disposition:
                bundle.putString("type","性格");
                break;
            case R.id.test_mentality:
                bundle.putString("type","心理健康");
                break;
            case R.id.test_relationship:
                bundle.putString("type","人际关系");
                break;
            case R.id.test_love:
                bundle.putString("type","恋爱");
                break;
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }
}