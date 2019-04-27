package com.example.gonami.bookboxbook.AddBook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.BookMarket.BookSellDetailFragment;
import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
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
    ////////////////커리어넷 api
    private EditText ed_memo;
    private EditText ed_price;
    private SearchView searchView;

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


    private String selling_price = "";
    private String seller_id = "";

    private BookInformation registBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_setting);

        check_underline1 = findViewById(R.id.check_underline1);
        check_underline2= findViewById(R.id.check_underline2);
        check_writing1 = findViewById(R.id.check_writing1);
        check_writing2 = findViewById(R.id.check_writing2);
        check_clean = findViewById(R.id.check_clean);
        check_name = findViewById(R.id.check_name);
        check_damage1 = findViewById(R.id.check_damage1);
        check_damage2 = findViewById(R.id.check_damage2);

        ed_memo = findViewById(R.id.ed_memo);
        ed_price = findViewById(R.id.ed_price);
        btn_addphoto = findViewById(R.id.btn_addphoto);
        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("학교를 검색하세요");

        btn_regist = findViewById(R.id.btn_regist);

        layout = findViewById(R.id.linearLayout);

        bookImage = new ArrayList<String>();
        school = new ArrayList<String>();

        registBook = (BookInformation) this.getIntent().getSerializableExtra("registBook");
        Log.i("get","getregistBook"+ registBook.getBook_image().toString());
//        new Thread() {
//            public void run() {
//                String schoolResult = getSchool("중앙대학교");
//
//                Bundle bun = new Bundle();
//                bun.putString("DATA", schoolResult);
//
//                Message msg = handler.obtainMessage();
//                msg.setData(bun);
//                handler.sendMessage(msg);
//            }
//        }.start();


    }

//
//    private void getPermission(){
//        PermissionListener permissionlistener = new PermissionListener() {
//
//            @Override
//
//            public void onPermissionGranted() {
//
//                Toast.makeText(BookSettingActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//
//            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//
//                Toast.makeText(BookSettingActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//
//            }
//
//        };
//
//        new TedPermission(this)
//
//                .setPermissionListener(permissionlistener)
//
//                .setDeniedMessage("If you reject permission,you can not use this service" +
//                        "\n\nPlease turn on permissions at [Setting] > [Permission] ")
//                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
//
//                .check();
//    }
//
    protected void onResume() {
        super.onResume();

        //앨범선택, 사진촬영, 취소 다이얼로그 생성
        btn_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeDialog();
            }
        });
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookSettingActivity.this, MainActivity.class);
                BookSettingActivity.this.startActivity(intent);

                //값에 넣어줌
                check_box_value();
                selling_price = ed_price.getText().toString();
                memo = ed_memo.getText().toString();
                seller_id = SaveSharedPreference.getUserID(BookSettingActivity.this);
                LocalDateTime register_date = LocalDateTime.now();
                String date = register_date.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                register_id = date + "-" + seller_id;
                registBook.setBookInformation(register_id, seller_id, school, selling_price, bookImage,
                underline, writing, cover, damage_page, memo);
                String concatt = registBook.toString();
                Log.i(TAG, ">>>>>>>>>>>>>\n"+ concatt);


                // 디비에 넣기
                InsertBookData task = new InsertBookData();

                task.execute("https://" + IP_ADDRESS + "/insert-book.php", registBook.toString());
//
                Log.i(TAG, "Added book in db");

                /**
                 * TODO
                 * 책 추가가 끝나고 입력한 정보를 보여주는 상세 페이지 창을 BookSellDetailFragment로 하면
                 * MainActivity에서 받는 Back Listener 때문에 안됨!
                 * 따라서 판매자의 상세 정보 창을 보여주고 싶다면, 새로운 Fragment 또는 Activity를 생성해야한다.
                 * 밑의 주석처리한 코드는 잘못된 코드임! 참고 사항으로 남겨둔 것이므로 지우지 말 것
                 */

//                String book_register_id = register_id;
//                Bundle bundle = new Bundle();
//                bundle.putString("BookRegisterID", book_register_id);
//                BookSellDetailFragment bookSellDetailFragment = BookSellDetailFragment.newInstance(bundle);
//
//                FragmentManager fragmentManager = getSupportFragmentManager();
//
//                fragmentManager.beginTransaction()
//                               .replace(R.id.frame_layout, bookSellDetailFragment)
//                               .commit();
//                finish();

            }
        });


    }
    private void makeDialog(){

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(BookSettingActivity.this);

        alt_bld.setTitle("사진 업로드").setCancelable(
                false).setPositiveButton("사진촬영",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 사진 촬영 클릭
                        takePhoto();
                    }
                }).setNeutralButton("앨범선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        //앨범에서 선택
                        selectAlbum();
                    }
                }).setNegativeButton("취소   ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 취소 클릭. dialog 닫기.
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
        String imageFileName = System.currentTimeMillis() + ".jpg";
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
        return imageFile;

    }
    public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"사진이 저장되었습니다",Toast.LENGTH_SHORT).show();

    }
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(IMAGE_HIGHT, IMAGE_WIDTH);
        imageView = new ImageView(this);
//        imageView.getLayoutParams().height = IMAGE_WIDTH;
//        imageView.getLayoutParams().width = IMAGE_WIDTH;
//        imageView.requestLayout();
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setRotation(90);
        imageView.setBackgroundColor(Color.GREEN);
        switch (requestCode){
            case FROM_ALBUM : {
                //앨범에서 가져오기
                if(data.getData()!=null){
                    try{
                        File albumFile = null;
                        albumFile = createImageFile();
                        albumURI = Uri.fromFile(albumFile);
                        bookImage.add(data.getData().toString());
                        imageView.setImageURI(data.getData());
                        layout.addView(imageView);
                        //cropImage();
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
//이미지뷰에 이미지셋팅
                    bookImage.add(photoURI.toString());
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

    private class InsertBookData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(BookSettingActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.i(TAG, "POST response1  - " + result);
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
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String schoolResult = bun.getString("DATA");

            String[] splitResult = schoolResult.split("\\n");
            //splitResult[2] = image
//            ed_name.setText(splitResult[1]);
//            ed_isbn.setText(splitResult[2]);
            Log.i("result","ggg"+splitResult.toString());
        }
    };
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



