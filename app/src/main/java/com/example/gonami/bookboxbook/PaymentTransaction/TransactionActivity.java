package com.example.gonami.bookboxbook.PaymentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;


import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;
import com.example.gonami.bookboxbook.TransactionList.BuyListFragment;
import com.example.gonami.bookboxbook.TransactionList.BuyListViewAdapter;
import com.example.gonami.bookboxbook.TransactionList.SellListFragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TransactionActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "TransactionActivity";

    private WebView webViewTransaction;
    private WebSettings webSettings;
    private static final String APP_SCHEME = "iamporttest://";

    private final Handler handler = new Handler();
    private String postData;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        webViewTransaction = findViewById(R.id.webview_transaction);
        webViewTransaction.setWebViewClient(new InicisWebViewClient(this));
        webSettings = webViewTransaction.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webViewTransaction.addJavascriptInterface(new AndroidBridge(), "android");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webViewTransaction, true);
        }

        Intent intent = getIntent();
        String register_id = intent.getExtras().getString("register_id");
        String book_name = intent.getExtras().getString("book_name");
        String book_price = intent.getExtras().getString("book_price");

        Uri intentData = intent.getData();
        String phone_num = SaveSharedPreference.getUserPN(this);

        postData = "register_id=" + register_id + "&book_name=" + book_name + "&book_price=" + book_price + "&phone_num=" + phone_num;

        if (intentData == null) {
//            PaymentInfo task = new PaymentInfo();
//            task.execute("https://" + IP_ADDRESS + "/payment.php", register_id, book_name, book_price,phone_num);

//            webViewTransaction.loadUrl("https://"+IP_ADDRESS +"/payment.php");
            Log.i(TAG, "start~~~~~");
            webViewTransaction.postUrl("https://" + IP_ADDRESS + "/payment_js.php", postData.getBytes());



        } else {
            //isp 인증 후 복귀했을 때 결제 후속조치
            String url = intentData.toString();
            Log.i(TAG, "ggg:" + url);
            if (url.startsWith(APP_SCHEME)) {
                String redirectURL = url.substring(APP_SCHEME.length() + 3);
                webViewTransaction.loadUrl(redirectURL);
                Log.i(TAG, "1: " + redirectURL);


            }
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String url = intent.toString();
        if ( url.startsWith(APP_SCHEME) ) {
            String redirectURL = url.substring(APP_SCHEME.length()+3);
            webViewTransaction.loadUrl(redirectURL);
            Log.i(TAG,"2: "+redirectURL);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(TransactionActivity.this, BuyListViewAdapter.class);
        //intent.putExtra("from", "TransactionList");
        TransactionActivity.this.startActivity(intent);


    }

    private class AndroidBridge {

        @JavascriptInterface
        public void testMove(final String arg) { // must be final

            handler.post(new Runnable() {

                @Override

                public void run() {

                    // 원하는 동작

                    //webViewTransaction.loadUrl("javascript:IMP.request_pay("+postData+"')");

                }

            });

        }


    }

}
