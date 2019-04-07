package com.example.gonami.bookboxbook.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "SignUp";

    private EditText edUserID;
    private EditText edUserPW;
    private EditText edUserPWCheck;
    private EditText edUserName;
    private EditText edUserPN;
    private EditText edUserSchool;

    private TextView checkSignUp;

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

        checkSignUp = findViewById(R.id.tv_check_signup);


        edUserPWCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = edUserPW.getText().toString();
                String check = edUserPWCheck.getText().toString();

                if(!password.equals(check)) {
                    edUserPWCheck.setHighlightColor(Color.RED);
                }
//                else {
//                    edUserPW.setBackgroundColor(Color.RED);
//                    edUserPWCheck.setBackgroundColor(Color.RED);
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = edUserID.getText().toString();
                String pw = edUserPW.getText().toString();
                String pwCheck = edUserPWCheck.getText().toString();
                String name = edUserName.getText().toString();
                String phonenum = edUserPN.getText().toString();
                String school = edUserSchool.getText().toString();



                if (id.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "ID를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserID.requestFocus();
                    return;
                }

                if (pw.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserPW.requestFocus();
                    return;
                }

                if (pwCheck.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                    edUserPWCheck.requestFocus();
                    return;
                }

                if (name.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "이름을 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserName.requestFocus();
                    return;
                }

                if (phonenum.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "전화번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserPN.requestFocus();
                    return;
                }

                if (school.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "학교를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserSchool.requestFocus();
                    return;
                }

                InsertMemberData task = new InsertMemberData();


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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent signupIntent = new Intent(SignUpActivity.this, LoginActivity.class);
//        SignUpActivity.this.startActivity(signupIntent);
//        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
//        finish();
//    }

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

    class InsertMemberData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            checkSignUp.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = (String)strings[1];
            String pw = (String)strings[2];
            String name = (String)strings[3];
            String phonenum = (String)strings[4];
            String school = (String)strings[5];

            String serverURL = (String)strings[0];
            String postParameters = "id=" + id + "& pw=" + pw + "& name=" + name +"& ph=" + phonenum +"& school=" + school;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }
}
