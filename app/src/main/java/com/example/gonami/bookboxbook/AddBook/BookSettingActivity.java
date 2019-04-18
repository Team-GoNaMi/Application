package com.example.gonami.bookboxbook.AddBook;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.gonami.bookboxbook.BookMarket.BookSellDetailFragment;
import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

//        new Thread() {
//            public void run() {
//                String schoolResult = getSchool("중앙대학교");
//
//                Bundle bun = new Bundle();
//                bun.putString("DATA", schoolResult);
//
//                Message msg = handler.obtainMessage();
//                msg.setData(bun);
//                handler.sendMessage(msg);
//            }
//        }.start();
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
                InsertBookData task = new InsertBookData();

                task.execute("https://" + IP_ADDRESS + "/insert-book.php", registBook.toString());
//
                Log.i(TAG, "Added book in db");

                /**
                 * TODO
                 * 책 추가가 끝나고 입력한 정보를 보여주는 상세 페이지 창을 BookSellDetailFragment로 하면
                 * MainActivity에서 받는 Back Listener 때문에 안됨!
                 * 따라서 판매자의 상세 정보 창을 보여주고 싶다면, 새로운 Fragment 또는 Activity를 생성해야한다.
                 * 밑의 주석처리한 코드는 잘못된 코드임! 참고 사항으로 남겨둔 것이므로 지우지 말 것
                 */

//                String book_register_id = register_id;
//                Bundle bundle = new Bundle();
//                bundle.putString("BookRegisterID", book_register_id);
//                BookSellDetailFragment bookSellDetailFragment = BookSellDetailFragment.newInstance(bundle);
//
//                FragmentManager fragmentManager = getSupportFragmentManager();
//
//                fragmentManager.beginTransaction()
//                               .replace(R.id.frame_layout, bookSellDetailFragment)
//                               .commit();
//                finish();

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
        Log.i("photo","gg"+resultCode);

        ImageView imagebook = new ImageView(this);
        imagebook.setImageURI(data.getData());
        layout.addView(imagebook);

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




    private class InsertBookData extends AsyncTask<String, Void, String> {

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
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String naverResult = bun.getString("DATA");

            String[] splitResult = naverResult.split("\\n");
            //splitResult[2] = image
//            ed_name.setText(splitResult[1]);
//            ed_isbn.setText(splitResult[2]);
            Log.i("result","ggg"+splitResult.toString());
        }
    };
    public String getSchool(String school) {
        String key = "bcad9a7ff9219a1bb57dbf6353f2e262";

        StringBuffer sb = new StringBuffer();

        try {
            String text = URLEncoder.encode(school, "UTF-8");

            String apiURL = "https://www.career.go.kr/cnet/openapi/getOpenApi?apiKey="+key+"&svcType=api&svcCode=SCHOOL&contentType=xml&gubun=univ_list&searchSchulNm="+text;

            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            String tag;

            //inputStream으로부터 xml값 받기
            xpp.setInput(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기

                        if (tag.equals("content")) ; //첫번째 검색 결과
                        else if (tag.equals("schoolName")) {
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("region")) {
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        }
                        break;
                }
                eventType = xpp.next();
            }
            Log.i("sb", sb.toString());
            return sb.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }
}



