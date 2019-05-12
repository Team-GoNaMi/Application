package com.example.gonami.bookboxbook.TransactionProcess;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.gonami.bookboxbook.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BookBoxBookActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private String TAG = "BookBoxBook";

    private TextView tv_school;
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
    private String bb_location;
    private int bb_num;
    private String bb_id;//중앙대학교_1 이런식으로
    private String book_register_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bookboxbook);

        tv_school = findViewById(R.id.tv_school);

        btn_chooseDate = findViewById(R.id.btn_chooseDate);
        btn_book = findViewById(R.id.btn_book);

        Intent intent = new Intent(this.getIntent());
        book_register_id = intent.getExtras().getString("register_id");
        bb_location = intent.getExtras().getString("school");
//        trade_num =

        datePicker = findViewById(R.id.datePicker);
        settingPicker();
    }

    private void settingPicker(){
        datePicker.setMinDate(new Date().getTime());

    }
    @Override
    protected void onResume() {
        super.onResume();

        tv_school.setText(bb_location);

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
                                    Log.i(TAG, book_date + "  &&&&& " + stringDate);
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

                //TODO 예외처리
                bb_id = String.format("%s-%s", bb_location, bb_num);

                BookBoxBookActivity.InsertBookBoxData task = new BookBoxBookActivity.InsertBookBoxData();
                task.execute("https://" + IP_ADDRESS + "/reserve-bookbox.php", bb_location, book_date.toString(),book_register_id);

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

    private class InsertBookBoxData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "POST response1  - " + result);
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverURL = (String)strings[0];

            // TODO date가 이틀이어야함함
           String postParameters = "bb_location=" + strings[1] +"&date=" + strings[2] + "&book_register_id=" + strings[3];
            Log.i(TAG, "postParameters"+postParameters);

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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    Log.i(TAG, "OKAY");
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                    Log.i(TAG, String.valueOf(responseStatusCode));

                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                Log.i(TAG, "////"+sb.toString());
                return sb.toString();

            } catch (Exception e) {
                Log.i(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }


}
