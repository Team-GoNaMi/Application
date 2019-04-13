package com.example.gonami.bookboxbook.AddBook;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gonami.bookboxbook.DataCenter.BookInformation;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;
import com.example.gonami.bookboxbook.TransactionList.SellListFragment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class BookSettingActivity extends AppCompatActivity{

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

////////////////커리어넷 api
    private EditText ed_memo;
    private EditText ed_price;


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

    private String price;
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
        //ed_price = findViewById(R.id.ed_price);
        btn_addphoto = findViewById(R.id.btn_addphoto);

        btn_regist = findViewById(R.id.btn_regist);

        layout = findViewById(R.id.linearLayout);

        bookImage = new ArrayList<String>();
        school = new ArrayList<String>();

        registBook = (BookInformation) this.getIntent().getSerializableExtra("registBook");
    }


    protected void onResume() {
        super.onResume();

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookSettingActivity.this, MainActivity.class);
                BookSettingActivity.this.startActivity(intent);

                //값에 넣어줌
                check_box_value();
              //  memo = ed_memo.getText().toString();
              //  price = ed_price.getText().toString();
                register_id = LocalDateTime.now().toString() + seller_id;

                registBook.setBookInformation(register_id, seller_id, school, price, bookImage,
                underline, writing, cover, damage_page, memo);
                finish();
            }
        });

        btn_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imagebook = new ImageView(this);
        imagebook.setImageURI(data.getData());
        layout.addView(imagebook);
        bookImage.add(String.valueOf(data.getData()));

        //Uri.parse() 데이터베이스에서 이미지 불러올때

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
}



