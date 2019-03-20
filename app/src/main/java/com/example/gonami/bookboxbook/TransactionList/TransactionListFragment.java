package com.example.gonami.bookboxbook.TransactionList;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

public class TransactionListFragment extends Fragment {

    private View thisView = null;

    private TabLayout tabLayout;

    private ListPagerAdapter listPagerAdapter;
    private ViewPager viewPager;

    public TransactionListFragment() {

    }

    public static TransactionListFragment newInstance() {
        TransactionListFragment fragment = new TransactionListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (thisView == null)
            thisView = inflater.inflate(R.layout.fragment_list, null);

//        listPagerAdapter = new ListPagerAdapter(getSupportFragmentManager());
        listPagerAdapter = new ListPagerAdapter(getChildFragmentManager());
        viewPager = thisView.findViewById(R.id.view_pager);
        viewPager.setAdapter(listPagerAdapter);

        tabLayout = thisView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return thisView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
