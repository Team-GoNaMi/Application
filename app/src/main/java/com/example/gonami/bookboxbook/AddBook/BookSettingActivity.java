package com.example.gonami.bookboxbook.AddBook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gonami.bookboxbook.R;

public class BookSettingActivity extends AppCompatActivity {
    private TextView tv_sell;
    private TextView tv_writing;
    private CheckBox check_underline1;
    private CheckBox check_underline2;
    private CheckBox check_writing1;
    private CheckBox check_writing2;

    private TextView tv_cover;
    private CheckBox check_clean;
    private CheckBox check_name;
    private CheckBox check_damage1;
    private CheckBox check_damage2;

    private TextView tv_photo;
    private ImageButton image1;
    private ImageButton image2;

    private TextView tv_location;

    private TextView tv_memo;

    private Button btn_regist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_setting);

    }
}
