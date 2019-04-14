package com.example.gonami.bookboxbook.BookMarket;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;
import java.util.Locale;

public class BookSearchListViewAdapter extends BaseAdapter {

    private ArrayList<ViewHolder> bookList;

    private ImageView ivBookImage;
    private TextView tvBookName;
    private TextView tvBookInfo;
    private TextView tvSchoolNames;
    private TextView tvBookOriginPrice;
    private TextView tvBookPrice;

    public BookSearchListViewAdapter(ArrayList<ViewHolder> bookList) {
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
           // viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book_list, parent, false);

            ivBookImage = (ImageView)convertView.findViewById(R.id.img_book);
            tvBookName = convertView.findViewById(R.id.tv_book_name);
            tvBookInfo = convertView.findViewById(R.id.tv_book_info);
            tvSchoolNames = convertView.findViewById(R.id.tv_book_schoolname);
            tvBookOriginPrice = convertView.findViewById(R.id.tv_book_original_price);
            tvBookPrice = convertView.findViewById(R.id.tv_book_price);

           // convertView.setTag(viewHolder);
        }
        else {
           // viewHolder = (ViewHolder)convertView.getTag();

        }


        // TODO DB에서 불러와서 해당 책 등록번호에 맞는 책 이미지, 책 이름, 책 정보 불러와서 띄우기
        ivBookImage.setImageAlpha(R.mipmap.ic_launcher);
        tvBookName.setText(bookList.get(position).getTvBookName());
        tvBookInfo.setText("차은호 / 겨루");
        tvSchoolNames.setText("중앙대 서울캠, 숙명여대");
        tvBookOriginPrice.setText("20원");
        tvBookPrice.setText("4000원");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 책 페이지로 넘어가야함
//                Intent intent = new Intent(context, BookSellingPage.class);
//                context.startActivity(intent);

            }
        });

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        bookList.clear();
        // TODO 마저 이어서..
    }

}
