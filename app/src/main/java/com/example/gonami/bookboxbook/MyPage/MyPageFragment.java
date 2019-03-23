package com.example.gonami.bookboxbook.MyPage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;

public class MyPageFragment extends Fragment {

    private View thisView = null;

    private ListView menuListView;
    private MyPageListViewAdapter menuListViewAdapter;
    private ArrayList<String> menu;

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

        menu = new ArrayList<String>();

        menu.add("내가 판 책");
        menu.add("내가 산 책");
        menu.add("북마크");
        menu.add("앱 설정");

        menuListView = (ListView)thisView.findViewById(R.id.lv_mypage);
        menuListViewAdapter = new MyPageListViewAdapter(menu);
        menuListView.setAdapter(menuListViewAdapter);

    }
}
