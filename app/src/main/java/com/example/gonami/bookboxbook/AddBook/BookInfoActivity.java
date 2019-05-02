package com.example.gonami.bookboxbook.AddBook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.R;

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
    private EditText ed_publish_date;

    private String bookImage;
    private Button btn_next;
    private Boolean isBarcord;
    private String naverResult;
    private BookInformation registBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinfo);

        ed_name = findViewById(R.id.ed_BookName);
        ed_isbn = findViewById(R.id.ed_ISBN);
        ed_author = findViewById(R.id.ed_Author);
        ed_publisher = findViewById(R.id.ed_Publisher);
        ed_price = findViewById(R.id.ed_originalPrice);
        ed_publish_date = findViewById(R.id.ed_publishDate);
        btn_next = findViewById(R.id.btn_next);

        Intent Intent = new Intent(this.getIntent());
        isBarcord = Intent.getExtras().getBoolean("isBarcord");
        if (isBarcord == true) {
            final String isbn = Intent.getExtras().getString("isbn");
            new Thread() {
                public void run() {
                    naverResult = getNaverSearch(isbn);

                    Bundle bun = new Bundle();
                    bun.putString("DATA", naverResult);

                    Message msg = handler.obtainMessage();
                    msg.setData(bun);
                    handler.sendMessage(msg);

                }
            }.start();
        }

    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            naverResult = bun.getString("DATA");

            String[] splitResult = naverResult.split("\\n");

            bookImage = splitResult[2];

            ed_name.setText(splitResult[1]);
            ed_isbn.setText(splitResult[7]);
            ed_author.setText(splitResult[3]);
            ed_publisher.setText(splitResult[5]);
            ed_price.setText(splitResult[4]);
            ed_publish_date.setText(splitResult[6]);
        }
    };
    @Override
    protected void onResume() {
        super.onResume();

        ed_publish_date.setOnKeyListener(new View.OnKeyListener() {
           //TODO 엔터 동작 이상함
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btn_next.performClick();
                    return true;
                }
                return false;
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String origin_price = ed_price.getText().toString();

                //TODO 가격이 달러면..?
                if (origin_price.contains(".")) {
                    String[] temp = origin_price.split("\\.");
                    origin_price = temp[0];
                }


                if (ed_isbn.getText().length() == 0) {
                    Toast.makeText(BookInfoActivity.this, "ISBN을 입력하세요!", Toast.LENGTH_SHORT).show();
                    ed_isbn.requestFocus();
                    return;
                }

                if (ed_name.getText().length() == 0) {
                    Toast.makeText(BookInfoActivity.this, "책제목을 입력하세요!", Toast.LENGTH_SHORT).show();
                    ed_name.requestFocus();
                    return;
                }

                if (ed_author.getText().length() == 0) {
                    Toast.makeText(BookInfoActivity.this, "저자를 입력하세요.", Toast.LENGTH_SHORT).show();
                    ed_author.requestFocus();
                    return;
                }

                if (ed_publisher.getText().length() == 0) {
                    Toast.makeText(BookInfoActivity.this, "출판사를 입력하세요!", Toast.LENGTH_SHORT).show();
                    ed_publisher.requestFocus();
                    return;
                }

                if (ed_price.getText().length() == 0) {
                    Toast.makeText(BookInfoActivity.this, "가격을 입력하세요!", Toast.LENGTH_SHORT).show();
                    ed_price.requestFocus();
                    return;
                }
                if (ed_publish_date.getText().length() == 0) {
                    Toast.makeText(BookInfoActivity.this, "출간연도를 입력하세요!", Toast.LENGTH_SHORT).show();
                    ed_publish_date.requestFocus();
                    return;
                }

                //TODO ISBN 예외처리..
                if(isBarcord == false){

                    new Thread() {
                        public void run() {
                            naverResult = getNaverSearch(ed_isbn.getText().toString());

                            Bundle bun = new Bundle();
                            bun.putString("DATA", naverResult);

                            Message msg = handler.obtainMessage();
                            msg.setData(bun);
                            handler.sendMessage(msg);
                        }
                    }.start();
                }

                registBook = new BookInformation(ed_isbn.getText().toString(),ed_name.getText().toString(),
                        ed_author.getText().toString(), ed_publisher.getText().toString(),
                        ed_price.getText().toString(), ed_publish_date.getText().toString(), bookImage);

                Log.i("gg","bookimage" + registBook.getFirstBookImage());

                Intent Intent = new Intent(BookInfoActivity.this, BookSettingActivity.class);
                Intent.putExtra("registBook", registBook);
                BookInfoActivity.this.startActivity(Intent);
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
                        }else if (tag.equals("pubdate")) {
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
