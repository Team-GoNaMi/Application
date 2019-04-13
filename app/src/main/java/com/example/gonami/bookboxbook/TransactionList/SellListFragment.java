package com.example.gonami.bookboxbook.TransactionList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;
import com.example.gonami.bookboxbook.RecognizeCode.MakeQRcode;

import java.util.ArrayList;

public class SellListFragment extends Fragment {

    private View thisView = null;

    private ListView sellListView;
    private SellLisViewAdapter sellLisViewAdapter;
    private ArrayList<String> sellList;

    private Button testBtn;
    private Button testBtn2;

    public SellListFragment() {

    }

    public static SellListFragment newInstance() {
        SellListFragment fragment = new SellListFragment();
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
            thisView = inflater.inflate(R.layout.fragment_list_sell, null);

        sellList = new ArrayList<String>();

        testBtn = thisView.findViewById(R.id.button);
        testBtn2 = thisView.findViewById(R.id.button2);

        // Test
        sellList.add("여기는!!!!");
        sellList.add("내가 판 책");
        sellList.add("입 니 당 ! ! !");

        sellListView = thisView.findViewById(R.id.lv_sell_list);
        sellLisViewAdapter = new SellLisViewAdapter(sellList);
        sellListView.setAdapter(sellLisViewAdapter);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(getActivity(), BookBoxBookActivity.class);
                getActivity().startActivity(Intent);
            }
        });
        testBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(getActivity(), MakeQRcode.class);
                getActivity().startActivity(Intent);
            }
        });
        return thisView;
    }

}
