package com.example.gonami.bookboxbook.AddBook;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ImageParser {

    private static String TAG = "ImageParser";

    public static JSONObject uploadImage(String imageUploadUrl, String sourceImageFiles, String idx) {

        String[] imageFiles = sourceImageFiles.split(",");

        try {
            File sourceFile = new File(imageFiles[0]);
            Log.i(TAG, imageUploadUrl);
            Log.i(TAG, "File...::::" + sourceFile + " : " + sourceFile.exists());
            String filename = imageFiles[0].substring(imageFiles[0].lastIndexOf("/")+1);
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            // OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uploaded_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .addFormDataPart("idx", "0")
                    .build();

            Request request = new Request.Builder()
                    .url(imageUploadUrl)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.e(TAG, "json result: " + res);
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(TAG, "Error2: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }
}