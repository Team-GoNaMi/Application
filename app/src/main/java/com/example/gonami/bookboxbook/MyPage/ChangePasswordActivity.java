package com.example.gonami.bookboxbook.MyPage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChangePasswordActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "ChangePassword";

    private EditText ed_cur_password;
    private EditText ed_password;
    private EditText ed_password_check;

    private Button btn_cancel_change_info;
    private Button btn_change_info;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chage_password);

        ed_cur_password = findViewById(R.id.ed_cur_pw);
        ed_password = findViewById(R.id.ed_pw);
        ed_password_check = findViewById(R.id.ed_pw_check);

        btn_cancel_change_info = findViewById(R.id.btn_cancel_change_info);
        btn_change_info = findViewById(R.id.btn_change_info);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String id = SaveSharedPreference.getUserID(getBaseContext());
        Log.i(TAG, "++++++++++ " + id);

        btn_change_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "change btn clicked");

                String cur_pw = ed_cur_password.getText().toString();
                String pw = ed_password.getText().toString();
                String pwCheck = ed_password_check.getText().toString();

                String cur_pw_check = SaveSharedPreference.getUserPW(getBaseContext());
                String id = SaveSharedPreference.getUserID(getBaseContext());

                if (cur_pw.length() == 0) {
                    Toast.makeText(ChangePasswordActivity.this, "현재 비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    ed_cur_password.requestFocus();
                    return;
                }

                if (!cur_pw.equals(cur_pw_check)) {
                    Toast.makeText(ChangePasswordActivity.this, "현재 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                    ed_cur_password.requestFocus();
                    return;
                }

                if (pw.length() == 0) {
                    Toast.makeText(ChangePasswordActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    ed_password.requestFocus();
                    return;
                }

                if (pwCheck.length() == 0 || !pw.equals(pwCheck)) {
                    Toast.makeText(ChangePasswordActivity.this, "비밀번호를 확인해 주세요!", Toast.LENGTH_SHORT).show();
                    ed_password_check.requestFocus();
                    return;
                }

                if (pw.equals(cur_pw)) {
                    Toast.makeText(ChangePasswordActivity.this, "비밀번호가 같습니다.", Toast.LENGTH_SHORT).show();
                    ed_password.requestFocus();
                    return;
                }

                // 디비에 넣기
                UpdatePassword task = new UpdatePassword();
                task.execute("https://" + IP_ADDRESS + "/update-password.php", id, pw);

                overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
                finish();
            }
        });

        btn_cancel_change_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "cancel btn clicked");

                overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
                finish();
            }
        });
    }

    private class UpdatePassword extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "POST response1  - " + result);

            SaveSharedPreference.updatePW(getBaseContext(), ed_password.getText().toString());

            Toast.makeText(getBaseContext(), "비밀번호 수정 완료", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {


            String serverURL = strings[0];
            String postParameters = "id=" + strings[1] + "& password=" + strings[2];

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
                Log.i(TAG, "UpdatePassword - Error : ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

}
