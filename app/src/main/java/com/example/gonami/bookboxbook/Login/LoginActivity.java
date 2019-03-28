package com.example.gonami.bookboxbook.Login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edUserID;
    private EditText edUserPW;

    private Button btnSignIn;
    private Button btnSignUp;

    private CheckBox cbAutoLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUserID = findViewById(R.id.ed_user_id);
        edUserPW = findViewById(R.id.ed_user_password);

        btnSignIn = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_sign_up);

        cbAutoLogin = findViewById(R.id.cb_autologin);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(signinIntent);
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(signupIntent);
            }
        });
    }
}
