package com.example.gonami.bookboxbook.MyPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.IntroActivity;
import com.example.gonami.bookboxbook.Login.LoginActivity;
import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;

public class MyPageListViewAdapter extends BaseAdapter {

    private ArrayList<String> menu = new ArrayList<String>();

    public MyPageListViewAdapter(ArrayList<String> menu) {
        this.menu = menu;
    }

    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public Object getItem(int position) {
        return menu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mypage_menu, parent, false);
        }

        TextView tvMenuName = convertView.findViewById(R.id.tv_menu_name);
        tvMenuName.setText(menu.get(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, menu.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
