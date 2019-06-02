package com.example.gonami.bookboxbook.MyPage;

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

import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChangePersonalInfoActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "ChangePersonalInfo";

    private EditText ed_user_name;
    private EditText ed_user_id; // 수정불가
//    private EditText ed_user_password;
//    private EditText ed_user_password_check;
    private EditText ed_user_phone_num;
    private EditText ed_user_school;

    private Button btn_cancel_change_info;
    private Button btn_change_info;

    private String checkedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_personal_info);

        ed_user_name = findViewById(R.id.ed_user_name);
        ed_user_id = findViewById(R.id.ed_user_id);
//        ed_user_password = findViewById(R.id.ed_user_password);
//        ed_user_password_check = findViewById(R.id.ed_user_password_check);
        ed_user_phone_num = findViewById(R.id.ed_user_phone_num);
        ed_user_school = findViewById(R.id.ed_user_school);

        btn_cancel_change_info = findViewById(R.id.btn_cancel_change_info);
        btn_change_info = findViewById(R.id.btn_change_info);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String id = SaveSharedPreference.getUserID(getBaseContext());
        Log.i(TAG, "++++++++++ " + id);
        GetMemberData task = new GetMemberData();
        task.execute("https://" + IP_ADDRESS + "/get-user-info.php", id);

        ed_user_school.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btn_change_info.performClick();
                    return true;
                }
                return false;
            }
        });

        btn_change_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "change btn clicked");

                String id = ed_user_id.getText().toString();

                String phonenum = ed_user_phone_num.getText().toString();
                String school = ed_user_school.getText().toString();



                if (phonenum.length() == 0) {
                    Toast.makeText(ChangePersonalInfoActivity.this, "전화번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    ed_user_phone_num.requestFocus();
                    return;
                }

                if (phonenum.length() != 11) {
                    Toast.makeText(ChangePersonalInfoActivity.this, "전화번호를 다시 입력하세요!", Toast.LENGTH_SHORT).show();
                    ed_user_phone_num.requestFocus();
                    return;
                }

                if (school.length() == 0) {
                    Toast.makeText(ChangePersonalInfoActivity.this, "학교를 입력하세요!", Toast.LENGTH_SHORT).show();
                    ed_user_school.requestFocus();
                    return;
                }

                // 디비에 넣기
                UpdateMemberData task = new UpdateMemberData();
                task.execute("https://" + IP_ADDRESS + "/update-user.php", id, phonenum, school);

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



    private class UpdateMemberData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "POST response1  - " + result);

            SaveSharedPreference.updatePN(getBaseContext(), ed_user_phone_num.getText().toString());

            Toast.makeText(getBaseContext(), "회원 수정 완료", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[1];
            String phonenum = strings[2];
            String school = strings[3];     // error


            String serverURL = strings[0];
            String postParameters = "id=" + id + "& phonenum=" + phonenum +"&school=" + school;


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
                Log.i(TAG, "UpdateData - Error : ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    private class GetMemberData extends AsyncTask<String, Void, String> {

        String errorString = null;

        private String userJsonString;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "response1 - " + s.length() + " : " + s);

            if (s.length() == 0){
                Log.i(TAG, errorString);
            }
            else {
                userJsonString = s;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverURL = strings[0];
            String user_id = strings[1];
            String postParameters = "user_id=" + user_id ;

            Log.i(TAG, "user_id : " + user_id);

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
                Log.d(TAG, "GetUserData : Error ", e);
                errorString = e.toString();
            }
            return null;
        }

        private void showResult() {
            String TAG_SUCCESS = "success";
            String TAG_ID = "id";
            String TAG_NAME = "name";
            String TAG_PHONENUM = "phonenum";
            String TAG_SCHOOL = "school";

            boolean success;
            try {
                JSONObject jsonObject = new JSONObject(userJsonString);

                success = jsonObject.getBoolean(TAG_SUCCESS);
                if(success) {
                    ed_user_id.setText(jsonObject.getString(TAG_ID));
                    ed_user_name.setText(jsonObject.getString(TAG_NAME));
                    ed_user_phone_num.setText(jsonObject.getString(TAG_PHONENUM));
                    ed_user_school.setText(jsonObject.getString(TAG_SCHOOL));
                }
                else {
                    Toast.makeText(getBaseContext(), "회원 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Log.i(TAG, "showResult : ", e);
            }
        }
    }
}
