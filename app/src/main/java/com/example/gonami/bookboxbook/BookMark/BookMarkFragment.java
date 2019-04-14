package com.example.gonami.bookboxbook.BookMark;

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

public class BookMarkFragment extends Fragment {

    private View thisView = null;

    private ListView bookmarkListView;
    private BookMarkListViewAdapter bookmarkListAdapter;
    private ArrayList<String> bookmarkList;         // 책에 대한 정보 저장하는 클래스 필요? or 책 이름 가지고 그때그때 불러서 보여줌?

    public BookMarkFragment() {

    }

    public static BookMarkFragment newInstance() {
        BookMarkFragment fragment = new BookMarkFragment();
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
            thisView = inflater.inflate(R.layout.fragment_bookmark, null);

        return thisView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookmarkList = new ArrayList<String>();

        //Test
        bookmarkList.add("안녕하세요");
        bookmarkList.add("북마크입니당");
        bookmarkList.add("잘 됐으면 좋겠당");

        bookmarkListView = (ListView)thisView.findViewById(R.id.lv_bookmark);
        bookmarkListAdapter = new BookMarkListViewAdapter(bookmarkList);
        bookmarkListView.setAdapter(bookmarkListAdapter);
    }
}
