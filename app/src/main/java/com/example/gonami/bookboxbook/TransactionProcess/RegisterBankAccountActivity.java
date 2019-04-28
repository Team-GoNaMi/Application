package com.example.gonami.bookboxbook.TransactionProcess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gonami.bookboxbook.R;

public class RegisterBankAccountActivity extends AppCompatActivity {

    private EditText edBankInfo;
    private EditText edBankAccountNum;
    private EditText edBankAccountOwner;
    private Button btnBankResComplete;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_bank_account);
        edBankInfo = findViewById(R.id.ed_bank_info);
        edBankAccountNum = findViewById(R.id.ed_bank_account_num);
        edBankAccountOwner = findViewById(R.id.ed_bank_account_owner);
        btnBankResComplete = findViewById(R.id.btn_bank_res_complete);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnBankResComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 계좌정보 넣기
            }
        });
    }
}
