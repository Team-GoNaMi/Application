package com.example.gonami.bookboxbook.TransactionProcess;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.gonami.bookboxbook.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private String stringDate;

    private SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");


    //DB
    private Date book_date;
    private  String bb_location;
    private int bb_num;
    private String bb_id;//중앙대학교_1 이런식으로
    private String trade_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bookboxbook);
        btn_chooseDate = findViewById(R.id.btn_chooseDate);
        btn_book = findViewById(R.id.btn_book);

//        TODO 앞페이지에서 받아온다.
//        bb_location =
//        trade_num =

        datePicker = findViewById(R.id.datePicker);
        settingPicker();
        bb_num = getRandomAvailBBNum(book_date);
    }

    private int getRandomAvailBBNum(Date select_date){

        //avail 한 값 디비에서 가져오기
        return bb_num;
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
                calDialog = dialog.getDatePicker();
                calDialog.setMinDate(new Date().getTime());
//TODO 달력색
                dialog.show();

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

                                //확인 누르면 저장한다.
                                stringDate = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                                try {
                                    book_date = transFormat.parse(stringDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                        });


            }
        });
        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO 디비에 넣기
                //state false로 바꾸기
                bb_id = String.format("%s_%s", bb_location, bb_num);
//                Intent Intent = new Intent(BookBoxBookActivity.this, SellListFragment.class);
//                BookBoxBookActivity.this.startActivity(Intent);
                finish();
            }
        });

        //TODO 피커선택하면 달력에도..
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                stringDate = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
                Log.i("date", "hee"+stringDate);
                ///선택 완료시 넣어야하는뎅..
                try {
                    book_date = transFormat.parse(stringDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.i("date", "hee"+book_date.toString());
            }


        });


    }

}
