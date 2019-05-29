package com.example.gonami.bookboxbook.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gonami.bookboxbook.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

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

    private Button btnIDDupCheck;
    private Button btnSignUp;

    private String checkedID;

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
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnIDDupCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = edUserID.getText().toString();
                if (id.length() != 0) {
                    //  아이디 중복 확인
                    CheckDupID task = new CheckDupID();
                    task.execute("https://" + IP_ADDRESS + "/check-dup-id.php", id);
                }
                else {
                    Toast.makeText(SignUpActivity.this, "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        edUserSchool.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btnSignUp.performClick();
                    return true;
                }
                return false;
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btn clicked");

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

                if (id.length() > 10) {
                    Toast.makeText(SignUpActivity.this, "ID를 다시 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserID.requestFocus();
                    return;
                }

                if (!id.equals(checkedID)) {
                    Toast.makeText(SignUpActivity.this, "ID를 중복체크 해 주세요.", Toast.LENGTH_SHORT).show();
                    edUserID.requestFocus();
                    return;
                }

                if (pw.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserPW.requestFocus();
                    return;
                }

                if (pwCheck.length() == 0 || !pw.equals(pwCheck)) {
                    Toast.makeText(SignUpActivity.this, "비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                    edUserPWCheck.requestFocus();
                    return;
                }

                if (name.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "이름을 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserName.requestFocus();
                    return;
                }

                if (phonenum.length() == 0) {   // TODO 11자리가 아니면이라고 고쳐야 함
                    Toast.makeText(SignUpActivity.this, "전화번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserPN.requestFocus();
                    return;
                }

                if (school.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "학교를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserSchool.requestFocus();
                    return;
                }

                String token = FirebaseInstanceId.getInstance().getToken();

                // 디비에 넣기
                InsertMemberData task = new InsertMemberData();
                task.execute("https://" + IP_ADDRESS + "/insert-user.php", id, pw, name, phonenum, school, token);


                Intent intent = new Intent();
                intent.putExtra("id", edUserID.getText().toString());

                Log.i(TAG, "Added member in db");

                setResult(RESULT_OK, intent);
                overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
                finish();
            }
        });
    }

    private class InsertMemberData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "회원가입 중입니다. 기다려 주세요.", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.i(TAG, "POST response1  - " + result);
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[1];
            String pw = strings[2];
            String name = strings[3];
            String phonenum = strings[4];
            String school = strings[5];
            String token = strings[6];

            String serverURL = strings[0];
            String postParameters = "id=" + id + "& password=" + pw + "& name=" + name +"& phonenum=" + phonenum +"&school=" + school + "&token=" + token;


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
                Log.i(TAG, "POST response code2 - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    Log.i(TAG, "OKAY");
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                    Log.i(TAG, String.valueOf(responseStatusCode));

                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                Log.i(TAG, "////"+sb.toString());
                return sb.toString();

            } catch (Exception e) {
                Log.i(TAG, "InsertData - Error : ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    private class CheckDupID extends AsyncTask<String, Void, String> {

        String errorString = null;

        private String userJsonString;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "response1 - " + s.length() + " : " + s);

            if (s.length() == 0)
                Log.i(TAG, errorString);
            else {
                userJsonString = s;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            String serverURL = strings[0];
            String id = strings[1];

            String postParameters = "id=" + id;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code2 - " + responseStatusCode);

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
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "CheckDupID - Error : ", e);
                errorString = e.toString();
            }

            return null;
        }

        private void showResult() {
            String TAG_SUCCESS="success";
            String TAG_ID="id";

            boolean success;

            try {
                JSONObject jsonObject = new JSONObject(userJsonString);

                success = jsonObject.getBoolean(TAG_SUCCESS);
                Log.i(TAG, "success : " + success);
                if(success) {
                    Log.i(TAG, "ID Dup true");
                    Toast.makeText(SignUpActivity.this, "아이디를 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    checkedID = jsonObject.getString(TAG_ID);
                }
                else {
                    Log.i(TAG, "ID Dup false");
                    Toast.makeText(SignUpActivity.this, "아이디가 중복되었습니다.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Log.i(TAG, "showResult : ", e);
            }
        }
    }
}
