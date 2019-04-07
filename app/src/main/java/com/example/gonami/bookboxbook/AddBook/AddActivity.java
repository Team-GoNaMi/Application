package com.example.gonami.bookboxbook.AddBook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.gonami.bookboxbook.R;
import com.example.gonami.bookboxbook.RecognizeCode.ScannerActivity;

public class AddActivity extends AppCompatActivity {

    private Button btn_barcord;
    private Button btn_manual;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btn_barcord = findViewById(R.id.btn_barcord);
        btn_manual = findViewById(R.id.btn_manual);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btn_barcord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent barcordIntent = new Intent(AddActivity.this, ScannerActivity.class);
                AddActivity.this.startActivity(barcordIntent);
                finish();
            }
        });
        btn_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manualIntent = new Intent(AddActivity.this, BookInfoActivity.class);
                manualIntent.putExtra("isBarcord",false);
                AddActivity.this.startActivity(manualIntent);
                finish();
            }
        });
    }
}
