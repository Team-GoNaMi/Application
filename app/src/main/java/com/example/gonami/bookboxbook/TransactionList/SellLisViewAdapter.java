package com.example.gonami.bookboxbook.TransactionList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;

public class SellLisViewAdapter extends BaseAdapter {

    private ArrayList<String> bookList;

    public SellLisViewAdapter(ArrayList<String> sellList) { this.bookList = sellList; }

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
        final Context parentContext = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book_list_transaction, parent, false);
        }

        ImageView ivBookImage = convertView.findViewById(R.id.img_book);
        TextView tvBookName = convertView.findViewById(R.id.tv_book_name);
        TextView tvBookInfo = convertView.findViewById(R.id.tv_book_info);
        TextView tvSchoolNames = convertView.findViewById(R.id.tv_book_schoolname);
        final Button btnBookState = convertView.findViewById(R.id.btn_book_state);
        TextView tvOriginPrice = convertView.findViewById(R.id.tv_original_price);
        TextView tvBookPrice = convertView.findViewById(R.id.tv_book_price);

        String bookRegisterNum = bookList.get(position);

        // TODO DB에서 불러와서 해당 책 등록번호에 맞는 책 이미지, 책 이름, 책 정보, 거래정보 등 불러와서 띄우기
        ivBookImage.setImageAlpha(R.mipmap.ic_launcher);
        tvBookName.setText(bookRegisterNum);
        tvBookInfo.setText("이승윤 / 플레디스");
        tvSchoolNames.setText("중앙대 서울캠, 숭실대");
        tvOriginPrice.setText("5000원");
        tvOriginPrice.setPaintFlags(tvOriginPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        tvBookPrice.setText("5000원");
        btnBookState.setText("책을 가져가주세욤!");
        return convertView;
    }
}
