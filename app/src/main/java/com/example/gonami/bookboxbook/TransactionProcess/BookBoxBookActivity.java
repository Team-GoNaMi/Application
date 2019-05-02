package com.example.gonami.bookboxbook.TransactionProcess;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.gonami.bookboxbook.AddBook.BookSettingActivity;
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
    private RadioButton radioSchool1;
    private RadioButton radioSchool2;
    private ArrayList<String> schools;

    private Button btn_chooseDate;
    private DatePickerDialog dialog;
    private final Calendar cal = Calendar.getInstance();
    private DatePicker calDialog;

    private DatePicker datePicker;

    private Button btn_book;
    private String stringDate;
    private SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
    private RadioGroup radioGroup;

    //DB
    private Date book_date;
    private String bb_location;
    private int bb_num;
    private String bb_id;//중앙대학교_1 이런식으로

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bookboxbook);
        btn_chooseDate = findViewById(R.id.btn_chooseDate);
        btn_book = findViewById(R.id.btn_book);
        radioSchool1 = findViewById(R.id.checkSchool1);
        radioGroup = findViewById(R.id.radioGroup);

        schools = new ArrayList<String>();
        Intent intent = new Intent(this.getIntent());
        schools = intent.getExtras().getStringArrayList("school");

        radioSchool1.setText(schools.get(0));
        if(schools.size() == 2){
            radioSchool2 = new RadioButton(BookBoxBookActivity.this);
            radioSchool2.setText(schools.get(1));
            radioGroup.addView(radioSchool2);
        }
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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == 0){
                    bb_location = radioSchool1.getText().toString();
                }
                else{
                    bb_location = radioSchool2.getText().toString();
                }
            }
        });
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
                //TODO 예외처리
                bb_id = String.format("%s-%s", bb_location, bb_num);

                BookBoxBookActivity.InsertBookBoxData task = new BookBoxBookActivity.InsertBookBoxData();
                task.execute("https://" + IP_ADDRESS + "/insert-book.php", bb_id, book_date.toString(), bb_location, String.valueOf(bb_num));


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

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(BookBoxBookActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.i(TAG, "POST response1  - " + result);
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverURL = (String)strings[0];
//            String postParameters = (String)strings[1];

            String postParameters = "bb_id=" + strings[1] +"&date=" + strings[2] + "&location="
                    + strings[3] + "&locker_num=" + strings[4];

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
