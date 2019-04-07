package com.example.gonami.bookboxbook.RecognizeCode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.gonami.bookboxbook.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ScannerActivity extends AppCompatActivity {

    private IntentIntegrator scan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanner);
        scan = new IntentIntegrator(this);
        scan.setPrompt("바코드를 인식해주세요");
        scan.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //result of scan
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode,data);
        //print result
        Toast.makeText(this, "ISBN:" + result.getContents(),Toast.LENGTH_SHORT).show();
    }

}
