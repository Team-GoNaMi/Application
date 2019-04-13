package com.example.gonami.bookboxbook.TransactionList;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.gonami.bookboxbook.R;

import java.util.Calendar;
import java.util.Date;

public class BookBoxBookActivity extends AppCompatActivity {

    private String TAG = "PickerActivity";
    private Button btn_chooseDate;
    private DatePickerDialog dialog;
    private final Calendar cal = Calendar.getInstance();
    private DatePicker calDialog;

    private DatePicker datePicker;


    private Button btn_book;


    //DB
    private Date book_date;
    private String bb_id;
    private String trade_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bookboxbook);
        btn_chooseDate = findViewById(R.id.btn_chooseDate);
        btn_book = findViewById(R.id.btn_book);


        datePicker = findViewById(R.id.datePicker);
        settingPicker();
    }

    private void settingPicker(){
        datePicker.setMinDate(new Date().getTime());
    }
    @Override
    protected void onResume() {
        super.onResume();
        //DATE PICKER DIALOG
        btn_chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new DatePickerDialog(BookBoxBookActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMinDate(new Date().getTime());
                dialog.show();
                calDialog = dialog.getDatePicker();
                calDialog.init(calDialog.getYear(), calDialog.getMonth(), calDialog.getDayOfMonth(),
                        new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                                datePicker.init(year, monthOfYear,dayOfMonth,
                                        new DatePicker.OnDateChangedListener() {
                                    @Override
                                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    }
                                });
                               // tv_test2.setText(String.format("%d/%d/%d", year,monthOfYear + 1, dayOfMonth));
                            }
                        });

            }
        });
        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent Intent = new Intent(BookBoxBookActivity.this, SellListFragment.class);
//                BookBoxBookActivity.this.startActivity(Intent);
                finish();
            }
        });
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                book_date = new Date();
                Log.i("///","/////"+book_date);
                //tv_test.setText(String.format("%d/%d/%d", year,monthOfYear + 1, dayOfMonth));

            }

        });

    }

}
