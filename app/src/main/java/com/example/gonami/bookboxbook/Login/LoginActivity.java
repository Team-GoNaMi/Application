package com.example.gonami.bookboxbook.Login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                Log.i("SignUp", "signupbtn clicked in login page");
                Intent signupIntent = new Intent(LoginActivity.this, SignUpActivity.class);

                signupIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                LoginActivity.this.startActivityForResult(signupIntent, 1000);
//                LoginActivity.this.startActivity(signupIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1000 && requestCode == RESULT_OK) {
            Toast.makeText(LoginActivity.this, "회원가입을 완료했습니다!", Toast.LENGTH_SHORT).show();
            edUserID.setText(data.getStringExtra("id"));
        }
    }
}
