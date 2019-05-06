package com.example.gonami.bookboxbook.BookMarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gonami.bookboxbook.InicisTransaction.TransactionActivity;
import com.example.gonami.bookboxbook.R;

public class BuyActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private CheckBox checkSchool;
    private RadioGroup radioGroup;
    private Button btn_goto_payment;

    private String register_id;
    private String schools;
    private String[] school_list;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        Intent intent = getIntent();
        register_id = intent.getExtras().getString("book_regist_id");
        schools = intent.getExtras().getString("schools");
        school_list = schools.split(",");

//        linearLayout= findViewById(R.id.linearLayout);
        radioGroup = findViewById(R.id.radioGroup);
        btn_goto_payment= findViewById(R.id.btn_goto_payment);

    }

    @Override
    protected void onResume() {
        super.onResume();

        addRadioButtons(school_list.length);


        btn_goto_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                if (radioButtonID != -1) {

                    Intent Intent = new Intent(BuyActivity.this, TransactionActivity.class);
                    //회원 아이디 넘겨야할까?
                    // Intent.putExtra("registBook", registBook);
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

        for (int row = 0; row < num; row++ ) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(school_list[row]);

            radioGroup.addView(radioButton);
        }
    }
}
