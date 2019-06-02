package com.example.gonami.bookboxbook.TransactionProcess;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterBankAccountActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private String TAG = "BankAccount";

    private EditText edBankInfo;
    private EditText edBankAccountNum;
    private EditText edBankAccountOwner;
    private Button btnBankResComplete;

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

        Intent intent = new Intent(this.getIntent());
        book_register_id = intent.getExtras().getString("register_id");
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnBankResComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edBankInfo.getText().length() == 0) {
                    Toast.makeText(RegisterBankAccountActivity.this, "은행을 입력하세요!", Toast.LENGTH_SHORT).show();
                    edBankInfo.requestFocus();
                    return;
                }

                if (edBankAccountNum.getText().length() == 0) {
                    Toast.makeText(RegisterBankAccountActivity.this, "계좌번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    edBankAccountNum.requestFocus();
                    return;
                }

                if (edBankAccountOwner.getText().length() == 0) {
                    Toast.makeText(RegisterBankAccountActivity.this, "예금주명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    edBankAccountOwner.requestFocus();
                    return;
                }

                bankInfo = edBankInfo.getText().toString();
                bankAccountNum = edBankAccountNum.getText().toString();
                bankAccountOwner = edBankAccountOwner.getText().toString();

                //TODO 예금주 일치?
                RegisterBankAccountActivity.InsertAccountData task = new RegisterBankAccountActivity.InsertAccountData();
                task.execute("https://" + IP_ADDRESS + "/insert-account.php", bankInfo, bankAccountNum, book_register_id);
                Intent intent = new Intent(RegisterBankAccountActivity.this, MainActivity.class);
                intent.putExtra("from", "TransactionList");
                RegisterBankAccountActivity.this.startActivity(intent);
//                Intent Intent = new Intent(RegisterBankAccountActivity.this, DepositActivity.class);
//                //회원 아이디 넘겨야할까?
//                // Intent.putExtra("registBook", registBook);
//                Intent.putExtra("owner", bankAccountOwner);
//                Intent.putExtra("bank", bankInfo);
//                Intent.putExtra("account_num", bankAccountNum);
//                RegisterBankAccountActivity.this.startActivity(Intent);
                finish();
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
