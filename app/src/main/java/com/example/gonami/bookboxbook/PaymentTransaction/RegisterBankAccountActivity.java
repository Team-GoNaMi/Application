package com.example.gonami.bookboxbook.PaymentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterBankAccountActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private String TAG = "BankAccount";

    private EditText edBankInfo;
    private EditText edBankAccountNum;
    private EditText edBankAccountOwner;
    private Button btnBankResComplete;


    private TextView test;
    //DB
    private String bankInfo;
    private String bankAccountNum;
    private String bankAccountOwner;
    private String book_register_id;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_bank_account);
        edBankInfo = findViewById(R.id.ed_bank_info);
        edBankAccountNum = findViewById(R.id.ed_bank_account_num);
        edBankAccountOwner = findViewById(R.id.ed_bank_account_owner);
        btnBankResComplete = findViewById(R.id.btn_bank_res_complete);

        test = findViewById(R.id.test);

//        Intent intent = new Intent(this.getIntent());
//        book_register_id = intent.getExtras().getString("register_id");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        btnBankResComplete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (edBankInfo.getText().length() == 0) {
//                    Toast.makeText(RegisterBankAccountActivity.this, "은행을 입력하세요!", Toast.LENGTH_SHORT).show();
//                    edBankInfo.requestFocus();
//                    return;
//                }
//
//                if (edBankAccountNum.getText().length() == 0) {
//                    Toast.makeText(RegisterBankAccountActivity.this, "계좌번호를 입력하세요!", Toast.LENGTH_SHORT).show();
//                    edBankAccountNum.requestFocus();
//                    return;
//                }
//
//                if (edBankAccountOwner.getText().length() == 0) {
//                    Toast.makeText(RegisterBankAccountActivity.this, "예금주명을 입력하세요.", Toast.LENGTH_SHORT).show();
//                    edBankAccountOwner.requestFocus();
//                    return;
//                }
//
//                bankInfo = edBankInfo.getText().toString();
//                bankAccountNum = edBankAccountNum.getText().toString();
//                bankAccountOwner = edBankAccountOwner.getText().toString();
////TODO 예금주 일치?
//                RegisterBankAccountActivity.InsertAccountData task = new RegisterBankAccountActivity.InsertAccountData();
//                task.execute("https://" + IP_ADDRESS + "/insert-account.php", bankInfo, bankAccountNum, book_register_id);
//
//                finish();
//            }
//        });
        btnBankResComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                transferDeposit2();
            }
        });
    }

    private void transferDeposit2(){

        // 입금이체(계좌번호) 파라미터 형태
        /*
        {
            "wd_pass_phrase": "1111",
            "wd_print_content": "출금인자 01",
            "tran_dtime": "20170710141024",
            "req_cnt": "1",
            "req_list": [{
                "tran_no": 1,
                "bank_code_std": "097",
                "account_num": "112233",
                "account_holder_name": "김말똥",
                "print_content": "입금계좌인자01",
                "tran_amt": "1700"
                }
            ]
        }
        */

//        String token = getToken();
        String token = "5a965cd7-0ec3-4312-a7aa-dc8da4838e18";
        Map params = new LinkedHashMap<>();
        params.put("wd_pass_phrase", "0100000001");
        params.put("wd_print_content","출금인자 01");
        params.put("tran_dtime", "20170710141024");
        params.put("req_cnt", "1"); // 구현 시간 문제상 일단 한 건만 보내기로 함

        List<Map> innerMapList = new ArrayList<>();
        Map innerMap = new LinkedHashMap<>();
        innerMap.put("tran_no", "1");
        innerMap.put("bank_code_std", "079");
        innerMap.put("account_num", "112233");
        innerMap.put("account_holder_name", "홍길동");
        innerMap.put("print_content", "입금계좌인자01");
        innerMap.put("tran_amt", "1700");
        innerMap.put("cms_no", "123456789123");
        innerMapList.add(innerMap);
        params.put("req_list", innerMapList);

        Call<Map> call = RetrofitCustomAdapter.getInstance().transferDeposit2(token, params);
        // retrofit 비동기 호출 (동기호출시 NetworkOnMainThreadException 발생)
        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {

                // API 호출 결과 출력
                test.setText(response.body().toString());

            }
            @Override
            public void onFailure(Call<Map> call, Throwable t) {

                t.printStackTrace();
            }
        });


    }
    /**
     * 토큰 발급
     */
    private void getToken(){

//        String type = this.getArguments().getString("invokerType");
//        String redirectUriKey = (StringUtil.isNotBlank(type) && "APP".equals(type)) ? "APP_CALLBACK_URL" : "WEB_CALLBACK_URL";

        Map params = new LinkedHashMap<>();
//        params.put("code", authcode);
        params.put("client_id", "l7xx9093aa52f0974ec1b0e4133472c61d7f");
        params.put("client_secret", "738cbba9ef90481fa3356665821c4898");
//        params.put("redirect_uri", StringUtil.getPropStringForEnv(redirectUriKey));
        params.put("grant_type", "authorization_code");

        Call<Map> call = RetrofitCustomAdapter.getInstance().token(params);
        // retrofit 비동기 호출 (동기호출시 NetworkOnMainThreadException 발생)
        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {

                // API 호출 결과 출력
                test.setText(response.body().toString());

            }
            @Override
            public void onFailure(Call<Map> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }
    private class InsertAccountData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "POST response1  - " + result);
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverURL = (String)strings[0];
            String postParameters = "bank_info=" + strings[1] +"&account_num=" + strings[2] + "&register_id=" + strings[3];
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
