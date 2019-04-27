package com.example.gonami.bookboxbook.TransactionList;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;

public class BuyListViewAdapter extends BaseAdapter {

    private ArrayList<String> bookList;


    public BuyListViewAdapter(ArrayList<String> buyList) { this.bookList = buyList; }

    public int getCount() { return bookList.size(); }

    public Object getItem(int position) { return bookList.get(position); }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
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
        tvBookInfo.setText("남유선 / 큐브");
        tvSchoolNames.setText("중앙대 서울캠, 부천대");
        tvOriginPrice.setText("25000원");
        tvOriginPrice.setPaintFlags(tvOriginPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        tvBookPrice.setText("5000원");
        btnBookState.setText("판매자가 북박스 예약 중");

        return convertView;
    }
}
