package com.example.gonami.bookboxbook.TransactionList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.example.gonami.bookboxbook.R;

public class RateActivity extends AppCompatActivity {
    private Button btn_rate;
    private RatingBar ratingBar;
    private float rate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        btn_rate = findViewById(R.id.btn_rate);
        ratingBar = findViewById(R.id.ratingBar);
    }
    @Override
    protected void onResume() {
        super.onResume();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
            }
        });
        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO rate db에 넣기
                finish();
            }
        });
    }

}
