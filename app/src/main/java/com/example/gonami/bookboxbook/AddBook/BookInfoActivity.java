package com.example.gonami.bookboxbook.AddBook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gonami.bookboxbook.R;
import com.example.gonami.bookboxbook.RecognizeCode.ScannerActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class BookInfoActivity extends AppCompatActivity {

    private EditText ed_name;
    private EditText ed_isbn;
    private EditText ed_author;
    private EditText ed_publisher;
    private EditText ed_price;

    private Button btn_next;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinfo);

        ed_name = findViewById(R.id.ed_BookName);
        ed_isbn = findViewById(R.id.ed_ISBN);
        ed_author = findViewById(R.id.ed_Author);
        ed_publisher = findViewById(R.id.ed_Publisher);
        ed_price = findViewById(R.id.ed_originalPrice);

        btn_next = findViewById(R.id.btn_next);

        Intent Intent = new Intent(this.getIntent());
        Boolean isBarcord = Intent.getExtras().getBoolean("isBarcord");
        if(isBarcord==true){
            final String isbn = Intent.getExtras().getString("isbn");
            new Thread() {
                public void run() {
                    String naverResult = getNaverSearch(isbn);

                    Bundle bun = new Bundle();
                    bun.putString("DATA", naverResult);

                    Message msg = handler.obtainMessage();
                    msg.setData(bun);
                    handler.sendMessage(msg);
                }
            }.start();

        }else{

        }


    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String naverResult = bun.getString("DATA");

            String[] splitResult = naverResult.split("\\n");
            //set text
              ed_name.setText(splitResult[1]);
              ed_isbn.setText(splitResult[6]);
              ed_author.setText(splitResult[3]);
              ed_publisher.setText(splitResult[5]);
              ed_price.setText(splitResult[4]);
        }
    };
    @Override
    protected void onResume() {
        super.onResume();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent barcordIntent = new Intent(BookInfoActivity.this, BookSettingActivity.class);
                BookInfoActivity.this.startActivity(barcordIntent);
                finish();
            }
        });

    }
    public String getNaverSearch(String ISBN) {
        String clientID = "wNNgAlcczg7mtaePzFES";
        String clientSecret = "IsJqKOgX1y";
        StringBuffer sb = new StringBuffer();

        try {
            String text = URLEncoder.encode(ISBN, "UTF-8");

            String apiURL = "https://openapi.naver.com/v1/search/book_adv.xml?guery=" + text+"&d_isbn="+text;

            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Naver-Client-Id", clientID);
            conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);

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

                        if (tag.equals("item")) ; //첫번째 검색 결과
                        else if (tag.equals("title")) {
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("isbn")) {
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("author")) {
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("publisher")) {
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("price")) {
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("image")) {
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
