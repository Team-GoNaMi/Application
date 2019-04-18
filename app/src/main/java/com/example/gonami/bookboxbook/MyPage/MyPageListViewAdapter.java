package com.example.gonami.bookboxbook.MyPage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.IntroActivity;
import com.example.gonami.bookboxbook.Login.LoginActivity;
import com.example.gonami.bookboxbook.MainActivity;
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

    @SuppressLint("ResourceType")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        LayoutInflater inflater = null;

        if (convertView == null) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mypage_menu, parent, false);

            if(position == 0){
                convertView = inflater.inflate(R.layout.alarm_button, parent, false);
                ToggleButton toggleButton = convertView.findViewById(R.id.toggleButton);
            }
        }

//        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView = inflater.inflate(R.layout.mypage_menu, parent, false);
        if (position != 0) {
            TextView tvMenuName = convertView.findViewById(R.id.tv_menu_name);
            tvMenuName.setText(menu.get(position));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, menu.get(position), Toast.LENGTH_SHORT).show();

                if (position == 2) {
                    SaveSharedPreference.logout(context);
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    // Finish를 못함..ㅠㅠ
                }

                SaveSharedPreference.logout(context);

            }
        });

        return convertView;
    }
}
