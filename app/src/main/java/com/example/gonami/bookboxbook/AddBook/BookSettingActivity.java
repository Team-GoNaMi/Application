package com.example.gonami.bookboxbook.AddBook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BookSettingActivity extends AppCompatActivity{

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "BookSettingg";

    private static final int IMAGE_HIGHT = 500;
    private static final int IMAGE_WIDTH = 500;

    private CheckBox check_underline1;
    private CheckBox check_underline2;
    private CheckBox check_writing1;
    private CheckBox check_writing2;

    private CheckBox check_clean;
    private CheckBox check_name;
    private CheckBox check_damage1;
    private CheckBox check_damage2;

    private LinearLayout layout;
    private ImageButton btn_addphoto;

    private ImageView imageView;
    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private Uri albumURI, photoURI;
    private String mCurrentPhotoPath;
    private ArrayList<String> photoPath;
    private EditText ed_memo;
    private EditText ed_price;

    private Spinner spin_school;
    private LinearLayout linear_school;

    private LinearLayout linear_element1;
    private LinearLayout linear_element2;

    private Boolean empty1 = true;
    private Boolean empty2 = true;

    private TextView text_school1;
    private ImageButton btn_cancle1;
    private TextView text_school2;
    private ImageButton btn_cancle2;
    private String school_element;
    private Button btn_regist;

    //DB에 넣을 값들
    private int underline = 0;
    private int writing = 0;
    private int cover = 0;
    private int damage_page = 0;
    private String memo = "";
    private ArrayList<String> bookImage;
    private ArrayList<String> school;
    private String register_id;
    private ArrayAdapter<String> adapter;


    private String selling_price = "";
    private String seller_id = "";
    private BookInformation registBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_setting);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.school));

        check_underline1 = findViewById(R.id.check_underline1);
        check_underline2= findViewById(R.id.check_underline2);
        check_writing1 = findViewById(R.id.check_writing1);
        check_writing2 = findViewById(R.id.check_writing2);
        check_clean = findViewById(R.id.check_clean);
        check_name = findViewById(R.id.check_name);
        check_damage1 = findViewById(R.id.check_damage1);
        check_damage2 = findViewById(R.id.check_damage2);
        spin_school = findViewById(R.id.spin_school);
        spin_school.setAdapter(adapter);
        linear_school = findViewById(R.id.linear_school);

        ed_memo = findViewById(R.id.ed_memo);
        ed_price = findViewById(R.id.ed_price);
        btn_addphoto = findViewById(R.id.btn_addphoto);

        btn_regist = findViewById(R.id.btn_regist);

        layout = findViewById(R.id.linearLayout);

        bookImage = new ArrayList<String>();
        school = new ArrayList<String>();

        photoPath = new ArrayList<String>();

        registBook = (BookInformation) this.getIntent().getSerializableExtra("registBook");
        Log.i("get","getregistBook"+ registBook.getFirstBookImage());

    }

    protected void onResume() {
        super.onResume();

        btn_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoPath.size() < 3)
                    makeDialog();
                else
                    Toast.makeText(getBaseContext(), "이미지는 최대 3개까지 업로드 가능합니다.",Toast.LENGTH_SHORT).show();
            }
        });
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                check_box_value();
                selling_price = ed_price.getText().toString();
                memo = ed_memo.getText().toString();
                seller_id = SaveSharedPreference.getUserID(BookSettingActivity.this);
                LocalDateTime register_date = LocalDateTime.now();
                String date = register_date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                register_id = date + "-" + seller_id;

                if(empty1 == false){
                    school.add(text_school1.getText().toString());
                }
                if(empty2 == false){
                    school.add(text_school2.getText().toString());
                }
                // 사진 서버에 올리기
                Log.d("Photo", "Photo Upload Task Start");
                String ImageUploadURL = "https://" + IP_ADDRESS + "/insert-photo.php";

                for (int i = 0; i < photoPath.size(); i++) {
                    Log.i(TAG,"hhhhhhhhhhhhh:"+photoPath.get(i));
                    Log.i(TAG,"hh:"+register_id);
                    ImageUploadTask imageUploadTask = new ImageUploadTask();
                    imageUploadTask.execute(ImageUploadURL, photoPath.get(i), register_id, String.valueOf(i));
                }

                for (int i = 0; i < photoPath.size(); i++) {
                    bookImage.add("https://" + IP_ADDRESS + "/photos/" + register_id + "/" + i + ".jpg");
                    Log.i(TAG, "@@@" + "https://" + IP_ADDRESS + "/photos/" + register_id + "/" + i + ".jpg");
                }

                // 책 정보 입력
                registBook.setBookInformation(register_id, seller_id, school, selling_price, bookImage,
                        underline, writing, cover, damage_page, memo);
                String concatt = registBook.toString();
                Log.i(TAG, ">>>>>>>>>>>>>\n"+ concatt);

                // 디비에 넣기
                InsertBookData task = new InsertBookData();
                task.execute("https://" + IP_ADDRESS + "/insert-book.php", registBook.toString());

                Log.i(TAG, "Added book in db");

                Intent intent = new Intent(BookSettingActivity.this, MainActivity.class);
                BookSettingActivity.this.startActivity(intent);

            }
        });

        spin_school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    school_element = spin_school.getItemAtPosition(position).toString();

                    if(empty1 == true){
                        text_school1 = new TextView(BookSettingActivity.this);
                        btn_cancle1 = new ImageButton(BookSettingActivity.this);
                        btn_cancle1.setPadding(20, 0, 0, 0);

                        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(70, 50);

                        btn_cancle1.setImageResource(R.drawable.ic_delete);
                        btn_cancle1.setBackgroundColor(Color.WHITE);
                        btn_cancle1.setLayoutParams(lp);

                        text_school1.setText(school_element);

                        linear_element1 = new LinearLayout(BookSettingActivity.this);
                        linear_element1.addView(text_school1);
                        linear_element1.addView(btn_cancle1);
                        linear_school.addView(linear_element1);
                        empty1 = false;
                        btn_cancle1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                linear_element1.removeAllViews();
                                empty1 = true;
                            }
                        });
                    }
                    else if(empty1 == false && empty2 == true){
                        text_school2 = new TextView(BookSettingActivity.this);
                        btn_cancle2 = new ImageButton(BookSettingActivity.this);
                        btn_cancle2.setPadding(20, 0, 0, 0);

                        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(70, 50);

                        btn_cancle2.setImageResource(R.drawable.ic_delete);
                        btn_cancle2.setBackgroundColor(Color.WHITE);
                        btn_cancle2.setLayoutParams(lp);

                        text_school2.setText(school_element);

                        linear_element2 = new LinearLayout(BookSettingActivity.this);
                        linear_element2.addView(text_school2);
                        linear_element2.addView(btn_cancle2);
                        linear_school.addView(linear_element2);

                        empty2 = false;

                        btn_cancle2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                linear_element2.removeAllViews();
                                empty2 = true;
                            }
                        });
                    }
                    else if(empty1 == false && empty2 == false){
                        Toast.makeText(BookSettingActivity.this, "2개이상 추가하지 못합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void makeDialog(){

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(BookSettingActivity.this);

        alt_bld.setTitle("사진 업로드").setCancelable(
                false).setNegativeButton("사진촬영",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        takePhoto();
                    }
                }).setPositiveButton("앨범선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        selectAlbum();
                    }
                }).setNeutralButton("취소   ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();

    }

    private void selectAlbum(){
        //앨범 열기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, FROM_ALBUM);

    }

    public void takePhoto(){

        // 촬영 후 이미지 가져옴
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null){
                File photoFile = null;
                try{
                    photoFile = createImageFile();

                }catch (IOException e){
                    e.printStackTrace();
                }
                if(photoFile!=null){
                    Log.i(TAG, "*****" + photoFile.toString());
                    Log.i(TAG, "****" + getPackageName());
                    photoURI = FileProvider.getUriForFile(this,getPackageName(),photoFile);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, FROM_CAMERA);
                }
            }

        }else{
            Log.v("알림", "저장공간에 접근 불가능");
            return;
        }
    }

    private File createImageFile() throws IOException{
        String imageFileName = System.currentTimeMillis() + "-";
        File imageFile= null;

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(!storageDir.exists()){
            storageDir.mkdirs();

        }
        imageFile = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        photoPath.add(mCurrentPhotoPath);
        return imageFile;

    }

    public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Log.i(TAG, "%%%%%%%%%%%%%%%%%%%" +String.valueOf(f.length()));
        Toast.makeText(this,"사진이 저장되었습니다",Toast.LENGTH_SHORT).show();

    }

    // Bitmap 을 저장하는 메소드
    private void saveCropImage(Bitmap bitmap, String filePath) {
        File copyFile = new File(filePath);
        BufferedOutputStream bos = null;
        try {
            copyFile.createNewFile();
            bos = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); // 이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축
            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(IMAGE_HIGHT, IMAGE_WIDTH);
        imageView = new ImageView(this);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setRotation(90);
        switch (requestCode){
            case FROM_ALBUM : {
                //앨범에서 가져오기
                if(data.getData()!=null){
                    try{
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(Uri.parse(data.getData().toString()),
                                filePathColumn,null,null, null);

                        if(cursor != null){
                            cursor.moveToFirst();
///////***********************absolute value 추가해야된다..
                            mCurrentPhotoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            photoPath.add(mCurrentPhotoPath);
                            cursor.close();
                        }
//                        bookImage.add(data.getData().toString());
                        imageView.setImageURI(data.getData());
                        layout.addView(imageView);
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.v("알림","앨범에서 가져오기 에러");
                    }
                }
                break;
            }


            case FROM_CAMERA : {
                //촬영
                try{
                    galleryAddPic();
                    Log.i(TAG, ">>>>>> uri : "+photoURI.toString());
//                    bookImage.add(photoURI.toString());
                    imageView.setImageURI(photoURI);
                    layout.addView(imageView);

                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void check_box_value(){
        //underline
        if(check_underline1.isChecked()) {
            if(check_underline2.isChecked()){//둘 다 ok
                underline = 3;
            }
            else {//1번만 ok
                underline = 1;
            }
        }
        else{
            if(!check_underline2.isChecked()){//둘 다 not
                underline = 0;
            }
            else {//2번만 ok
                underline = 2;
            }
        }
        //writing
        if(check_writing1.isChecked()) {
            if(check_writing2.isChecked()){//둘 다 ok
                writing = 3;
            }
            else {//1번만 ok
                writing = 1;
            }
        }
        else{
            if(!check_writing2.isChecked()){//둘 다 not
                writing = 0;
            }
            else {//2번만 ok
                writing = 2;
            }
        }

        //cover
        if(check_clean.isChecked()) {
            if(check_name.isChecked()){//둘 다 ok
                cover = 3;
            }
            else {//1번만 ok
                cover = 1;
            }
        }
        else{
            if(!check_name.isChecked()){//둘 다 not
                cover = 0;
            }
            else {//2번만 ok
                cover = 2;
            }
        }

        //damage_page
        if(check_damage1.isChecked()) {
            if(check_damage2.isChecked()){//둘 다 ok
                damage_page = 3;
            }
            else {//1번만 ok
                damage_page = 1;
            }
        }
        else{
            if(!check_damage2.isChecked()){//둘 다 not
                damage_page = 0;
            }
            else {//2번만 ok
                damage_page = 2;
            }
        }
    }

    private  class ImageUploadTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                JSONObject jsonObject = ImageParser.uploadImage(params[0],params[1], params[2], params[3]);
                if (jsonObject != null) {
                    return jsonObject.getString("result").equals("success");
                }
            } catch (JSONException e) {
                Log.i(TAG, "Error3 : " + e.getLocalizedMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                Log.i(TAG, "이미지 파일 업로드 성공");
            }
            else {
                Log.i(TAG, "이미지 파일 업로드 실패");
            }
        }
    }

    private class InsertBookData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "POST response1  - " + result);
            if (result.length() == 0) {
                Toast.makeText(getBaseContext(), "책 등록 완료", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverURL = (String)strings[0];
            String postParameters = (String)strings[1];

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

    public String getSchool(String school) {
        String key = "bcad9a7ff9219a1bb57dbf6353f2e262";

        StringBuffer sb = new StringBuffer();

        try {
            String text = URLEncoder.encode(school, "UTF-8");

            String apiURL = "https://www.career.go.kr/cnet/openapi/getOpenApi?apiKey="+key+"&svcType=api&svcCode=SCHOOL&contentType=xml&gubun=univ_list&searchSchulNm="+text;

            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

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

                        if (tag.equals("content")) ; //첫번째 검색 결과
                        else if (tag.equals("schoolName")) {
                            xpp.next();
                            sb.append(xpp.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                            sb.append("\n");
                        } else if (tag.equals("region")) {
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



