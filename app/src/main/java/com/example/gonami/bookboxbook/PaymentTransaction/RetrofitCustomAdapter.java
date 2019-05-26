package com.example.gonami.bookboxbook.PaymentTransaction;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.JavaNetCookieJar;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * retrofit 커스터마이징을 위한 어댑터 클래스
 *  *
 *  - 무엇보다도 untrusted certificate 이슈 때문에 작성함.
 *
 * Created by LeeHyeonJae on 2017-06-27.
 */
public class RetrofitCustomAdapter {

    public static final int CONNECT_TIMEOUT = 10;
    public static final int WRITE_TIMEOUT = 15;
    public static final int READ_TIMEOUT = 20;
    private static OkHttpClient okHttpClient;
    private static RetrofitInterface retrofitInterface;

    /**
     * RetrofitInterface 객체를 생성하거나, 기 생성된 객체를 리턴한다.
     *
     * @return
     */
    public synchronized static RetrofitInterface getInstance(){

        if(retrofitInterface != null){ return retrofitInterface; }

        // http 로깅을 위한 인터셉터
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 쿠키매니저 설정 변경
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        okHttpClient = configureClient(new OkHttpClient().newBuilder()) // 인증서 무시 설정 적용
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .addInterceptor(httpLoggingInterceptor)
                .build();

        // Retrofit 설정
        retrofitInterface = new Retrofit.Builder()
                .baseUrl("https://openapi.open-platform.or.kr/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitInterface.class);

        return retrofitInterface;
    }

    /**
     * Allowing okHttpClient to use an untrusted certificate for SSL/HTTPS connection
     *
     *  - 이 설정을 하지 않으면 테스트서버 등에서 ssl handshake 단계에 막혀 요청이 진행되지 않는다.
     *
     * @param builder
     * @return
     */
    public static OkHttpClient.Builder configureClient(final OkHttpClient.Builder builder){

        final TrustManager[] certs = new TrustManager[]{ new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // keep empty
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // keep empty
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0]; // 빈 객체를 리턴하는 느낌
            }
        }};

        SSLContext ctx = null;

        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, certs, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true; // true로 변경함
            }
        };

        builder.sslSocketFactory(ctx.getSocketFactory()).hostnameVerifier(hostnameVerifier);

        return builder;
    }

}
