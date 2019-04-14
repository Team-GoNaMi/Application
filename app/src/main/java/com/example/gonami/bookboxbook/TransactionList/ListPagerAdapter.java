package com.example.gonami.bookboxbook.TransactionList;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ListPagerAdapter extends FragmentPagerAdapter {

    private static int PAGE_NUMBER = 2;

    public ListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SellListFragment.newInstance();
            case 1:
                return BuyListFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "판 책";
            case 1:
                return "산 책";
            default:
                return null;

        }
    }
}
