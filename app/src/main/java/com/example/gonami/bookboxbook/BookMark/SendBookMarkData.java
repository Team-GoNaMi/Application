package com.example.gonami.bookboxbook.BookMark;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendBookMarkData extends AsyncTask<String, Void, String> {
    String TAG = "BookMark";
    String errorString = null;

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.d(TAG, "response1 - " + s.length() + " : " + s);
    }

    @Override
    protected String doInBackground(String... strings) {

        String serverURL = strings[0];
        String user_id = strings[1];
        String register_id = strings[2];
        String state = strings[3];
        String postParameters = "user_id=" + user_id + "& register_id=" + register_id + "& state=" + state;
        Log.i(TAG, "postParameters : " + postParameters);

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
            Log.d(TAG, "SendBookMarkData - Error :", e);
            errorString = e.toString();
        }
        return null;
    }
}
