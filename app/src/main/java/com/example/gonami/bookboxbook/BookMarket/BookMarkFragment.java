package com.example.gonami.bookboxbook.BookMarket;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gonami.bookboxbook.R;

public class BookMarkFragment extends Fragment {

    private View thisView = null;

    public BookMarkFragment() {

    }

    public static BookMarkFragment newInstance() {
        BookMarkFragment fragment = new BookMarkFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (thisView == null)
            thisView = inflater.inflate(R.layout.fragment_bookmark, null);

        return thisView;
    }
}
