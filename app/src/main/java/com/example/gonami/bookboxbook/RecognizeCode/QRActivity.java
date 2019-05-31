package com.example.gonami.bookboxbook.RecognizeCode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class QRActivity extends AppCompatActivity {

    private static String TAG = "QR";
    private static String IP_ADDRESS = "bookboxbook.duckdns.org";

    private ImageView img_qr;
    private TextView tv_bb_location;
    private TextView tv_bb_num;
    private TextView tv_date;

    private String bb_location;
    private String bb_num;
    private String bb_date;

    private Bitmap bit_qr;
    private String register_id;
    private String isbn;
    private Boolean role;
    private String member_id;
    private String insertData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        img_qr = findViewById(R.id.img_qr);

        Intent intent = new Intent(this.getIntent());
        register_id = intent.getExtras().getString("register_id");
        isbn = intent.getExtras().getString("ISBN");
        role = intent.getExtras().getBoolean("role");
        member_id = SaveSharedPreference.getUserID(getBaseContext());

        if (role) {     // seller
            insertData = String.format("%s$$$%s$$$%s", isbn, register_id, member_id);
        }
        else {          // buyer
            insertData = String.format("%s$$$%s", register_id, member_id);
        }
        tv_bb_location = findViewById(R.id.tv_where);
        tv_bb_num = findViewById(R.id.tv_bb_num);
        tv_date = findViewById(R.id.tv_date);

        Log.i(TAG,"insertData:" + insertData);
        bit_qr = generateQRCode(insertData);
        img_qr.setImageBitmap(bit_qr);

        // DB에서 불러와
        GetBookBoxData task = new GetBookBoxData();
        task.execute("https://" + IP_ADDRESS + "/get-reserve-bb.php", register_id);


    }

    public static Bitmap generateQRCode(String contents) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Bitmap bitmap = null;
        try {
            bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 1000, 1000));
        }catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap toBitmap(BitMatrix matrix){
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bmp.setPixel(i, j, matrix.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    private class GetBookBoxData extends AsyncTask<String, Void, String> {

        String errorString = null;

        private String userJsonString;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "response1 - " + s.length() + " : " + s);

            if (s.length() == 0){
                Log.i(TAG, errorString);
            }
            else {
                userJsonString = s;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverURL = strings[0];
            String register_id = strings[1];
            String postParameters = "register_id=" + register_id;

            Log.i(TAG, "register_id : " + register_id);

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code2 - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "GetSellBookData - Error : ", e);
                errorString = e.toString();
            }
            return null;
        }

        private void showResult() {

            String TAG_BOX_ID = "box_id";
            String TAG_DATE = "date";
            String[] temp;
            try {


                JSONObject jsonObject = new JSONObject(userJsonString);

                temp = jsonObject.getString(TAG_BOX_ID).split("_");
                bb_location = temp[0];
                bb_num = temp[1];

                bb_date = jsonObject.getString(TAG_DATE);

                tv_bb_location.setText(bb_location);
                tv_bb_num.setText(bb_num);
                tv_date.setText(bb_date);

            } catch (JSONException e) {
                Log.i(TAG, "showResult : ", e);
            }
        }
    }
}