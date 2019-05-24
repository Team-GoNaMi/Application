package com.example.gonami.bookboxbook.MyPage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.Login.LoginActivity;
import com.example.gonami.bookboxbook.R;


import java.util.ArrayList;

public class MyPageFragment extends Fragment {

    private String TAG = "MyPage";

    private View thisView = null;

    private ListView menuListView;
    private MyPageListViewAdapter menuListViewAdapter;
    private ArrayList<String> menu;

    private TextView tv_user_name;
    private TextView tv_user_id;

    public MyPageFragment() {

    }

    public static MyPageFragment newInstance() {
        MyPageFragment fragment = new MyPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (thisView == null)
            thisView = inflater.inflate(R.layout.fragment_mypage, null);

        return thisView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_user_name = view.findViewById(R.id.user_name);
        tv_user_id = view.findViewById(R.id.user_id);

        tv_user_name.setText(SaveSharedPreference.getUserName(getContext()));
        tv_user_id.setText(SaveSharedPreference.getUserID(getContext()));

        Log.i(TAG, ">>>>" + SaveSharedPreference.getUserName(getContext()));
        Log.i(TAG, ">>>>" + SaveSharedPreference.getUserID(getContext()));

        menu = new ArrayList<String>();

        menu.add("알림 설정");
        menu.add("개인정보 수정");
        menu.add("로그아웃");
        menu.add("회원탈퇴");

        menuListView = (ListView)thisView.findViewById(R.id.lv_mypage);
        menuListViewAdapter = new MyPageListViewAdapter(menu);
        menuListView.setAdapter(menuListViewAdapter);



    }
}
