package com.example.gonami.bookboxbook.Login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


        edUserPWCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = edUserPW.getText().toString();
                String check = edUserPWCheck.getText().toString();

                if(password.equals(check)) {
                    edUserPW.setBackgroundColor(Color.GREEN);
                    edUserPWCheck.setBackgroundColor(Color.GREEN);
                }
                else {
                    edUserPW.setBackgroundColor(Color.RED);
                    edUserPWCheck.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edUserID.getText().toString().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "ID를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserID.requestFocus();
                    return;
                }

                if (edUserPW.getText().toString().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserPW.requestFocus();
                    return;
                }

                if (edUserPWCheck.getText().toString().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                    edUserPWCheck.requestFocus();
                    return;
                }

                if (edUserName.getText().toString().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "이름을 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserName.requestFocus();
                    return;
                }

                if (edUserPN.getText().toString().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "전화번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserPN.requestFocus();
                    return;
                }

                if (edUserSchool.getText().toString().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "학교를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserSchool.requestFocus();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("id", edUserID.getText().toString());

                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
