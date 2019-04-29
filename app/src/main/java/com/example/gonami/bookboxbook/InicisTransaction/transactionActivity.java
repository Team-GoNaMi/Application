//package com.example.gonami.bookboxbook.InicisTransaction;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.example.gonami.bookboxbook.R;
//public class transactionActivity extends AppCompatActivity {
//
//    private WebView webview_transaction;
//    private WebSettings webSettings;
//    private static final String APP_SCHEME = "iamporttest://";
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_transaction);
//
//        webview_transaction = findViewById(R.id.webview_transaction);
//        webview_transaction.setWebViewClient(new InicisWebViewClient(this));
//        webSettings = webview_transaction.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
//
//
//    }
//
//}
