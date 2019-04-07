package com.example.gonami.bookboxbook.RecognizeCode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.gonami.bookboxbook.AddBook.BookInfoActivity;
import com.example.gonami.bookboxbook.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

public class ScannerActivity extends AppCompatActivity {

    private IntentIntegrator scan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanner);
        scan = new IntentIntegrator(this);
        scan.setOrientationLocked(true);
        scan.setPrompt("바코드를 인식해주세요");
        scan.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //result of scan
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //print result
        Intent intent = new Intent(ScannerActivity.this, BookInfoActivity.class);
        intent.putExtra("isBarcord", true);
        intent.putExtra("isbn", result.getContents()); /*송신*/
        ScannerActivity.this.startActivity(intent);
        finish();
    }

}
