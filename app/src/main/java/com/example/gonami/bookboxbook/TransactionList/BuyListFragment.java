package com.example.gonami.bookboxbook.TransactionList;

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

public class BuyListFragment extends Fragment {

    private View thisView = null;

    private ListView buyListView;
    private BuyListViewAdapter buyListAdapter;
    private ArrayList<String> buyList;

    public BuyListFragment() {

    }

    public static BuyListFragment newInstance() {
        BuyListFragment fragment = new BuyListFragment();
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
            thisView = inflater.inflate(R.layout.fragment_list_buy, null);

        return thisView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buyList = new ArrayList<String>();

        //Test
        buyList.add("이것들이 바로");
        buyList.add("내가 산 책");
        buyList.add("입니당!");

        buyListView = (ListView)thisView.findViewById(R.id.lv_buy_list);
        buyListAdapter = new BuyListViewAdapter(buyList);
        buyListView.setAdapter(buyListAdapter);
    }
}
