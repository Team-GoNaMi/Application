package com.example.gonami.bookboxbook.MyPage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.BookMark.BookMarkListViewAdapter;
import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.Login.LoginActivity;
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
import java.util.ArrayList;

public class MyPageFragment extends Fragment {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private String TAG = "MyPage";

    private View thisView = null;

    private ListView menuListView;
    private MyPageListViewAdapter menuListViewAdapter;
    private ArrayList<String> menu;

    private TextView tv_user_name;
    private TextView tv_user_id;

    private LinearLayout linearLayout;
    private TextView tv_rate;
    public MyPageFragment() {

    }

    public static MyPageFragment newInstance() {
        MyPageFragment fragment = new MyPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (thisView == null)
            thisView = inflater.inflate(R.layout.fragment_mypage, null);

        return thisView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_user_name = view.findViewById(R.id.user_name);
        tv_user_id = view.findViewById(R.id.user_id);

        linearLayout = view.findViewById(R.id.ll_rating);
        tv_rate = view.findViewById(R.id.tv_rate);

        tv_user_name.setText(SaveSharedPreference.getUserName(getContext()));
        tv_user_id.setText(SaveSharedPreference.getUserID(getContext()));

        GetRate task = new GetRate();
        task.execute("https://" + IP_ADDRESS + "/get-rate.php", tv_user_id.getText().toString());


        Log.i(TAG, ">>>>" + SaveSharedPreference.getUserName(getContext()));
        Log.i(TAG, ">>>>" + SaveSharedPreference.getUserID(getContext()));

        menu = new ArrayList<String>();

        menu.add("알림 설정");
        menu.add("개인정보 수정");
        menu.add("비밀번호 변경");
        menu.add("로그아웃");
        menu.add("회원탈퇴");

        menuListView = (ListView)thisView.findViewById(R.id.lv_mypage);
        menuListViewAdapter = new MyPageListViewAdapter(menu);
        menuListView.setAdapter(menuListViewAdapter);



    }
    private class GetRate extends AsyncTask<String, Void, String> {

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
            String seller_id = strings[1];
            String postParameters = "seller_id=" + seller_id ;

            Log.i(TAG, "seller_id : " + seller_id);

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
            String TAG_RATE = "rate";
            String TAG_SUCCESS = "success";

            try {
                JSONObject jsonObject = new JSONObject(userJsonString);
                Boolean success = jsonObject.getBoolean(TAG_SUCCESS);

                if (success) {
                    linearLayout.removeAllViews();
                    String rate = jsonObject.getString(TAG_RATE);
                    Log.i(TAG,rate + "rate입니다");
                    tv_rate.setText("  (" + rate + ")");

                    float ratef = Float.valueOf(rate);
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    for (int i = 0; i < 5; i++) {
                        ImageView iv = new ImageView(getContext());
                        iv.setLayoutParams(lp);
                        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        float remains = ratef - i;

                       if (remains < 0.5) {
                            iv.setImageResource(R.drawable.ic_star_border_24dp);
                        }
                        else if ((0.5 <= remains) && (remains < 1)) {
                            iv.setImageResource(R.drawable.ic_star_half_24dp);
                        }
                        else {      // 1보다 큰 경우
                            iv.setImageResource(R.drawable.ic_star_full_24dp);
                        }
                        linearLayout.addView(iv);

                    }



                }
                else {
                    tv_rate.setText("별점이 아직 없습니다.");
                }


            } catch (JSONException e) {
                Log.i(TAG, "showResult : ", e);
            }
        }
    }
}
