package com.example.gonami.bookboxbook.AddBook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gonami.bookboxbook.R;

public class AddActivity extends Fragment {

    private View thisView = null;

    private Button btnBarcord;
    private Button btnManual;

    public AddActivity() {

    }

    public static AddActivity newInstance() {
        AddActivity fragment = new AddActivity();
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
            thisView = inflater.inflate(R.layout.activity_add, null);

        return thisView;
    }
}
