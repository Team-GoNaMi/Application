package com.example.gonami.bookboxbook.AddBook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gonami.bookboxbook.BookMarket.BookSellFragment;
import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class BookSettingActivity extends AppCompatActivity{

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "BookSettingg";

    private CheckBox check_underline1;
    private CheckBox check_underline2;
    private CheckBox check_writing1;
    private CheckBox check_writing2;

    private CheckBox check_clean;
    private CheckBox check_name;
    private CheckBox check_damage1;
    private CheckBox check_damage2;

    private LinearLayout layout;
    private ImageButton btn_addphoto;


    ////////////////커리어넷 api
    private EditText ed_memo;
    private EditText ed_price;


    private Button btn_regist;

    //DB에 넣을 값들
    private int underline = 0;
    private int writing = 0;
    private int cover = 0;
    private int damage_page = 0;
    private String memo = "";
    private ArrayList<String> bookImage;
    private ArrayList<String> school;
    private String register_id;

    private String selling_price = "";
    private String seller_id = "";

    private BookInformation registBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_setting);

        check_underline1 = findViewById(R.id.check_underline1);
        check_underline2= findViewById(R.id.check_underline2);
        check_writing1 = findViewById(R.id.check_writing1);
        check_writing2 = findViewById(R.id.check_writing2);
        check_clean = findViewById(R.id.check_clean);
        check_name = findViewById(R.id.check_name);
        check_damage1 = findViewById(R.id.check_damage1);
        check_damage2 = findViewById(R.id.check_damage2);

        ed_memo = findViewById(R.id.ed_memo);
        ed_price = findViewById(R.id.ed_price);
        btn_addphoto = findViewById(R.id.btn_addphoto);

        btn_regist = findViewById(R.id.btn_regist);

        layout = findViewById(R.id.linearLayout);

        bookImage = new ArrayList<String>();
        school = new ArrayList<String>();

        registBook = (BookInformation) this.getIntent().getSerializableExtra("registBook");
    }


    protected void onResume() {
        super.onResume();

        btn_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 0);
                }
            }
        });

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookSettingActivity.this, MainActivity.class);
                BookSettingActivity.this.startActivity(intent);

                //값에 넣어줌
                check_box_value();

//                String tempmemo = ed_memo.getText().toString();
//                String tempprice = ed_price.getText().toString();
//                if(tempmemo != "" && tempprice != ""){
//                    memo = ed_memo.getText().toString();
//                    selling_price = ed_price.getText().toString();
//                }

                selling_price = ed_price.getText().toString();
                memo = ed_memo.getText().toString();



                seller_id = SaveSharedPreference.getUserID(BookSettingActivity.this);
                LocalDateTime register_date = LocalDateTime.now();
                String date = register_date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                register_id = date + "-" + seller_id;

                registBook.setBookInformation(register_id, seller_id, school, selling_price, bookImage,
                underline, writing, cover, damage_page, memo);

                String concatt = registBook.toString();
                Log.i(TAG, ">>>>>>>>>>>>>\n"+ concatt);


                // 디비에 넣기
                BookSettingActivity.InsertMemberData task = new InsertMemberData();

                task.execute("https://" + IP_ADDRESS + "/insert-book.php", registBook.toString());
//
                Log.i(TAG, "Added book in db");


//                Intent registerIntent = new Intent(BookSettingActivity.this, );
//                registerIntent.putExtra("registerID", register_id);
//                startActivity(registerIntent);
//                finish();


                String book_register_id = register_id;
                Bundle bundle = new Bundle();
                bundle.putString("BookRegisterID", book_register_id);
                BookSellFragment bookSellFragment = BookSellFragment.newInstance(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                               .replace(R.id.frame_layout, bookSellFragment)
                               .commit();
                finish();

            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            ImageView imagebook = new ImageView(this);
            imagebook.setImageURI(data.getData());
            layout.addView(imagebook);
        }
        //get thumnail

        bookImage.add(String.valueOf(data.getData()));

        //Uri.parse() 데이터베이스에서 이미지 불러올때

    }



    private void check_box_value(){
        //underline
        if(check_underline1.isChecked()) {
            if(check_underline2.isChecked()){//둘 다 ok
                underline = 3;
            }
            else {//1번만 ok
                underline = 1;
            }
        }
        else{
            if(!check_underline2.isChecked()){//둘 다 not
                underline = 0;
            }
            else {//2번만 ok
                underline = 2;
            }
        }
        //writing
        if(check_writing1.isChecked()) {
            if(check_writing2.isChecked()){//둘 다 ok
                writing = 3;
            }
            else {//1번만 ok
                writing = 1;
            }
        }
        else{
            if(!check_writing2.isChecked()){//둘 다 not
                writing = 0;
            }
            else {//2번만 ok
                writing = 2;
            }
        }

        //cover
        if(check_clean.isChecked()) {
            if(check_name.isChecked()){//둘 다 ok
                cover = 3;
            }
            else {//1번만 ok
                cover = 1;
            }
        }
        else{
            if(!check_name.isChecked()){//둘 다 not
                cover = 0;
            }
            else {//2번만 ok
                cover = 2;
            }
        }

        //damage_page
        if(check_damage1.isChecked()) {
            if(check_damage2.isChecked()){//둘 다 ok
                damage_page = 3;
            }
            else {//1번만 ok
                damage_page = 1;
            }
        }
        else{
            if(!check_damage2.isChecked()){//둘 다 not
                damage_page = 0;
            }
            else {//2번만 ok
                damage_page = 2;
            }
        }
    }




    private class InsertMemberData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(BookSettingActivity.this,
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
            String postParameters = (String)strings[1];

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



