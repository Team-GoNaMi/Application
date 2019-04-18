package com.example.gonami.bookboxbook.BookMarket;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;
import java.util.Locale;

public class BookSearchListViewAdapter extends BaseAdapter {

    private ArrayList<BookInformation> bookList;
    private BookInformation bookInfo;

    private ImageView ivBookImage;
    private TextView tvBookName;
    private TextView tvBookInfo;
    private TextView tvSchoolNames;
    private TextView tvBookOriginPrice;
    private TextView tvBookPrice;

    public BookSearchListViewAdapter(ArrayList<BookInformation> bookList) {
        this.bookList = bookList;
    }



    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book_list, parent, false);
        }

        ivBookImage = (ImageView)convertView.findViewById(R.id.img_book);
        tvBookName = convertView.findViewById(R.id.tv_book_name);
        tvBookInfo = convertView.findViewById(R.id.tv_book_info);
        tvSchoolNames = convertView.findViewById(R.id.tv_book_schoolname);
        tvBookOriginPrice = convertView.findViewById(R.id.tv_book_original_price);
        tvBookPrice = convertView.findViewById(R.id.tv_book_price);


        // TODO DB에서 불러와서 해당 책 등록번호에 맞는 책 이미지, 책 이름, 책 정보 불러와서 띄우기
        bookInfo = bookList.get(position);

        ivBookImage.setImageAlpha(R.mipmap.ic_launcher);    // 책 이미지
        tvBookName.setText(bookInfo.getBookName());
        tvBookInfo.setText(bookInfo.getAuthor() + " / " +bookInfo.getPublisher());
        tvSchoolNames.setText("중앙대 서울캠, 숙명여대");     // 거래 장소
        tvBookOriginPrice.setText(bookInfo.getOriginal_price() + "원");
        tvBookPrice.setText(bookInfo.getSellingPrice() + "원");

        Log.i("Search", String.valueOf(this.getCount()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 책 페이지로 넘어가야함
//                Intent intent = new Intent(context, BookSellingPage.class);
//                context.startActivity(intent);
                Toast.makeText(context, bookInfo.getBookName(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        bookList.clear();
        // TODO 마저 이어서.. -> 뭘 해.....???ㅠㅠㅠ
    }

}
