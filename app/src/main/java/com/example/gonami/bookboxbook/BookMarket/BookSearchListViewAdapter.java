package com.example.gonami.bookboxbook.BookMarket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;
import java.util.Locale;

public class BookSearchListViewAdapter extends BaseAdapter {

    private ArrayList<String> bookList;
    private ViewHolder viewHolder;

    public BookSearchListViewAdapter(ArrayList<String> bookList) {
        this.bookList = bookList;
    }

    public class ViewHolder {
        ImageView ivBookImage;
        TextView tvBookName;
        TextView tvBookInfo;
        TextView tvSchoolNames;
        TextView tvBookOriginPrice;
        TextView tvBookPrice;
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
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book_list, parent, false);

            viewHolder.ivBookImage = (ImageView)convertView.findViewById(R.id.img_book);
            viewHolder.tvBookName = convertView.findViewById(R.id.tv_book_name);
            viewHolder.tvBookInfo = convertView.findViewById(R.id.tv_book_info);
            viewHolder.tvSchoolNames = convertView.findViewById(R.id.tv_book_schoolname);
            viewHolder.tvBookOriginPrice = convertView.findViewById(R.id.tv_book_original_price);
            viewHolder.tvBookPrice = convertView.findViewById(R.id.tv_book_price);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String bookRegisterNum = bookList.get(position);

        // TODO DB에서 불러와서 해당 책 등록번호에 맞는 책 이미지, 책 이름, 책 정보 불러와서 띄우기
        viewHolder.ivBookImage.setImageAlpha(R.mipmap.ic_launcher);
        viewHolder.tvBookName.setText(bookRegisterNum);
        viewHolder.tvBookInfo.setText("차은호 / 겨루");
        viewHolder.tvSchoolNames.setText("중앙대 서울캠, 숙명여대");
        viewHolder.tvBookOriginPrice.setText("20원");
        viewHolder.tvBookPrice.setText("4000원");

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
