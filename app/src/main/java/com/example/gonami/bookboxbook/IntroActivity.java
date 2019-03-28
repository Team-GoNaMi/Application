package com.example.gonami.bookboxbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gonami.bookboxbook.Login.LoginActivity;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageView logoImage = (ImageView) findViewById(R.id.image_logo);
        TextView logo = (TextView)findViewById(R.id.tv_logo);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                finish();
            }
        },2000);
    }
}
