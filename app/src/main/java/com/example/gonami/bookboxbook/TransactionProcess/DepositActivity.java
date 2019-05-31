package com.example.gonami.bookboxbook.TransactionProcess;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.PaymentTransaction.InicisWebViewClient;
import com.example.gonami.bookboxbook.R;

public class DepositActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "DepositActivity";

    private static final String APP_SCHEME = "iamporttest://";

    private WebView webViewDeposit;
    private WebSettings webSettings;
    private String postData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        webViewDeposit = findViewById(R.id.webview_deposit);
        webViewDeposit.setWebViewClient(new InicisWebViewClient(this));
        webSettings = webViewDeposit.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webViewDeposit, true);
        }

        Intent intent = getIntent();
        String owner = intent.getExtras().getString("owner");
        String bank = intent.getExtras().getString("bank");
        String account_num = intent.getExtras().getString("account_num");

        Uri intentData = intent.getData();
        String phone_num = SaveSharedPreference.getUserPN(this);

        postData = "owner=" + owner + "&bank=" + bank +
                "&account_num=" + account_num;

        if ( intentData == null ) {
//            PaymentInfo task = new PaymentInfo();
//            task.execute("https://" + IP_ADDRESS + "/payment.php", register_id, book_name, book_price,phone_num);

//            webViewTransaction.loadUrl("https://"+IP_ADDRESS +"/payment.php");
            Log.i(TAG,"start~~~~~");
            webViewDeposit.postUrl("https://"+IP_ADDRESS +"/payment_js.php",postData.getBytes());

        } else {
            //isp 인증 후 복귀했을 때 결제 후속조치
            String url = intentData.toString();
            Log.i(TAG,"ggg:"+ url);
            if ( url.startsWith(APP_SCHEME) ) {
                String redirectURL = url.substring(APP_SCHEME.length()+3);
                webViewDeposit.loadUrl(redirectURL);
                Log.i(TAG,"1: "+redirectURL);
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        String url = intent.toString();
        if ( url.startsWith(APP_SCHEME) ) {
            String redirectURL = url.substring(APP_SCHEME.length()+3);
            webViewDeposit.loadUrl(redirectURL);
            Log.i(TAG,"2: "+redirectURL);
        }
    }
}
