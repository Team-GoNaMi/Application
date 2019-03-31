package com.example.gonami.bookboxbook.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText edUserID;
    private EditText edUserPW;
    private EditText edUserPWCheck;
    private EditText edUserName;
    private EditText edUserPN;
    private EditText edUserSchool;

    private Button btnIDDupCheck;
    private Button btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        edUserID = findViewById(R.id.ed_user_id);
        edUserPW = findViewById(R.id.ed_user_password);
        edUserPWCheck = findViewById(R.id.ed_user_password_check);
        edUserName = findViewById(R.id.ed_user_name);
        edUserPN = findViewById(R.id.ed_user_phone_num);
        edUserSchool = findViewById(R.id.ed_user_school);

        btnIDDupCheck = findViewById(R.id.btn_dup_check);

        btnSignUp = findViewById(R.id.btn_sign_up);

    }

    @Override
    protected void onResume() {
        super.onResume();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                SignUpActivity.this.startActivity(signupIntent);
                overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent signupIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        SignUpActivity.this.startActivity(signupIntent);
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
        finish();
    }
}
