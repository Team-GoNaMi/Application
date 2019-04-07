package com.example.gonami.bookboxbook.AddBook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;
import com.example.gonami.bookboxbook.RecognizeCode.ScannerActivity;

public class AddFragment extends Fragment {

    private View thisView = null;

    private Button btn_barcord;
    private Button btn_manual;

    public AddFragment() {

    }

    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_barcord = thisView.findViewById(R.id.btn_barcord);
        btn_manual = thisView.findViewById(R.id.btn_manual);
    }


}
