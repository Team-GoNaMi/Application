package com.example.gonami.bookboxbook.TransactionList;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.gonami.bookboxbook.R;

import java.util.Calendar;
import java.util.Date;

public class BookBoxBookActivity extends AppCompatActivity {

    private String TAG = "PickerActivity";
    private Button btn_chooseDate;
    private DatePicker datePicker;
    private final Calendar cal = Calendar.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bookboxbook);
        btn_chooseDate = findViewById(R.id.btn_chooseDate);
        datePicker = findViewById(R.id.datePicker);
    }

    private void init(){

    }
    @Override
    protected void onResume() {
        super.onResume();
        //DATE PICKER DIALOG
        btn_chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(BookBoxBookActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMinDate(new Date().getTime());
                dialog.show();
                String a = dialog.getContext().toString();

            }
        });

    }

}
