package com.example.gonami.bookboxbook.BookMarket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.PaymentTransaction.TransactionActivity;
import com.example.gonami.bookboxbook.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuyActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "BuyActivity";

    private LinearLayout linearLayout;
    private CheckBox checkSchool;
    private RadioGroup radioGroup;
    private Button btn_goto_payment;

    private String register_id;
    private String schools;
    private String[] school_list;
    String book_name;
    String book_price;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        Intent intent = getIntent();
        register_id = intent.getExtras().getString("book_regist_id");
        book_name = intent.getExtras().getString("book_name");
        book_price = intent.getExtras().getString("book_price");
        schools = intent.getExtras().getString("schools");

        school_list = null;
        school_list = schools.split(",");
        Log.i(TAG, schools);
        Log.i(TAG, String.valueOf(school_list.length));

//        linearLayout= findViewById(R.id.linearLayout);
        btn_goto_payment= findViewById(R.id.btn_goto_payment);

    }

    @Override
    protected void onResume() {
        super.onResume();
        radioGroup = new RadioGroup(this);
        radioGroup = findViewById(R.id.radioGroup);

        addRadioButtons(school_list.length);


        btn_goto_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                if (radioButtonID != -1) {
                    Log.i(TAG, String.valueOf(radioButtonID));

                    // 디비에 넣기
                    String buyer_id = SaveSharedPreference.getUserID(BuyActivity.this);
                    InsertBuyerInfo task = new InsertBuyerInfo();
                    Log.i(TAG,"gggggggggggggggggg" + school_list[radioButtonID-1]);
                    task.execute("https://" + IP_ADDRESS + "/insert-buyer.php", register_id, buyer_id, school_list[radioButtonID-1]);

                    Intent Intent = new Intent(BuyActivity.this, TransactionActivity.class);
                    //회원 아이디 넘겨야할까?
                    // Intent.putExtra("registBook", registBook);
                    Intent.putExtra("register_id", register_id);
                    Intent.putExtra("book_name", book_name);
                    Intent.putExtra("book_price", book_price);
                    BuyActivity.this.startActivity(Intent);
                    finish();
                }
                else {
                    Toast.makeText(BuyActivity.this, "학교를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addRadioButtons(int num) {
        Log.i(TAG, String.valueOf(radioGroup.getChildCount()));
        radioGroup.removeAllViews();
        for (int row = 0; row < num; row++ ) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(school_list[row]);

            radioGroup.addView(radioButton);
        }
        Log.i(TAG, String.valueOf(radioGroup.getChildCount()));
    }

    private class InsertBuyerInfo extends AsyncTask<String, Void, String> {


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "POST response1  - " + result);
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverURL = (String) strings[0];
            String register_id = (String) strings[1];
            String buyer_id = strings[2];
            String school = strings[3];
            String postParameters = "register_id=" + register_id + "& buyer_id=" + buyer_id + "& school=" + school;

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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    Log.i(TAG, "OKAY");
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                    Log.i(TAG, String.valueOf(responseStatusCode));

                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                Log.i(TAG, "////" + sb.toString());
                return sb.toString();

            } catch (Exception e) {
                Log.i(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }
}
