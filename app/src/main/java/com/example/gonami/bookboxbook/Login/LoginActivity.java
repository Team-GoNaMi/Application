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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.MainActivity;
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

public class LoginActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "SignIn";

    private EditText edUserID;
    private EditText edUserPW;

    private Button btnSignIn;
    private Button btnSignUp;

    private CheckBox cbAutoLogin;

    private long backKeyPressedTime;    // 앱 종료 위한 백 버튼 누른 시간

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

        edUserPW.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btnSignIn.performClick();
                    return true;
                }
                return false;
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = edUserID.getText().toString();
                String pw = edUserPW.getText().toString();

                Log.i(TAG, "sign in btn clicked");

                if (id.length() == 0) {
                    Toast.makeText(LoginActivity.this, "ID를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserID.requestFocus();
                    return;
                }

                if (pw.length() == 0) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edUserPW.requestFocus();
                    return;
                }
/////////////////////
                String token = FirebaseInstanceId.getInstance().getToken();

                //  확인
                GetMemberData task = new GetMemberData();
                task.execute("https://" + IP_ADDRESS + "/get-login-info.php", id, pw, token);

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SignUp", "signupbtn clicked in login page");
                Intent signupIntent = new Intent(LoginActivity.this, SignUpActivity.class);

                signupIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivityForResult(signupIntent, 1000);
//                LoginActivity.this.startActivity(signupIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1000 && resultCode == RESULT_OK) {
            Log.i("SignUp", "signup finished");

            Toast.makeText(LoginActivity.this, "회원가입을 완료했습니다!", Toast.LENGTH_SHORT).show();
            edUserID.setText(data.getStringExtra("id"));
        }

        if (requestCode==1000 && resultCode == RESULT_CANCELED) {
            Log.i("SignUp", "signup cancelled");
            Toast.makeText(LoginActivity.this, "회원가입이 취소되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
//            super.onBackPressed();
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            this.finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    ////////////////
    private class GetMemberData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        private String userJsonString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "로그인 중 입니다. 기다려 주세요.", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            Log.d(TAG, "response1 - " + s.length() + " : " + s);

            if (s.length() == 0){
                Log.i(TAG, "No ID");
                Toast.makeText(LoginActivity.this, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                Log.i(TAG, errorString);
            }
            else {
                userJsonString = s;
                showResult();
            }
        }
        /////////////////
        @Override
        protected String doInBackground(String... strings) {

            String serverURL = strings[0];
            String id = strings[1];
            String pw = strings[2];
            String token = strings[3];
            String postParameters = "id=" + id + "& password=" + pw + "& token=" + token;
            Log.i(TAG, postParameters);
//////////////////
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
                Log.d(TAG, "Error ", e);
                errorString = e.toString();
            }
            return null;
        }
        ///////////////////
        private void showResult() {
            String TAG_SUCCESS = "success";
            String TAG_ID = "id";
            String TAG_PW = "pw";
            String TAG_NAME = "name";
            String TAG_PN = "phonenum";

            boolean success;

            try {
                JSONObject jsonObject = new JSONObject(userJsonString);

                success = jsonObject.getBoolean(TAG_SUCCESS);
                Log.i(TAG, "success : " + success);
                if(success) {
                    String user_id = jsonObject.getString(TAG_ID);
                    String user_pw = jsonObject.getString(TAG_PW);
                    String user_name = jsonObject.getString(TAG_NAME);
                    String user_pn = jsonObject.getString(TAG_PN);
                    Log.i(TAG, "trueeeeeeeeee");
                    Log.i(TAG, "Login checked");

                    user_id = user_id.trim();

                    SaveSharedPreference.setUserID(LoginActivity.this, cbAutoLogin.isChecked(), user_id, user_pw, user_name, user_pn);

                    Intent signinIntent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(signinIntent);
                    finish();
                }
                else {
                    Log.i(TAG, "falseeeeeeeeeeeee");
                    Toast.makeText(LoginActivity.this, "아이디가 존재하지 않거나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Log.i(TAG, "showResult : ", e);
            }
        }
    }
}