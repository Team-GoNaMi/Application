package com.example.gonami.bookboxbook.BookMarket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.gonami.bookboxbook.R;

public class BuyActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private Button btn_goto_payment;
    private CheckBox checkSchool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        linearLayout= findViewById(R.id.linearLayout);
        btn_goto_payment= findViewById(R.id.btn_goto_payment);




    }
}
