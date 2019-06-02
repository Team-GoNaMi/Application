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
        btn_goto_payment= findViewById(R.id.btn_ok);

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

                    Intent Intent = new Intent(BuyActivity.this, TransactionActivity.class);
                    //회원 아이디 넘겨야할까?
                    // Intent.putExtra("registBook", registBook);
                    Intent.putExtra("register_id", register_id);
                    Intent.putExtra("school",school_list[radioButtonID]);
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
            radioButton.setId(row);
            radioButton.setText(school_list[row]);

            radioGroup.addView(radioButton);
        }
        Log.i(TAG, String.valueOf(radioGroup.getChildCount()));
    }


}
