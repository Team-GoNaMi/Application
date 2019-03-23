package com.example.gonami.bookboxbook.BookMarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;

public class BookMarkListViewAdapter extends BaseAdapter {

    private ArrayList<String> bookList;

//    private ImageView ivBookImage;
//    private TextView tvBookName;
//    private TextView tvBookInfo;
//    private TextView tvSchoolNames;
//    private ImageButton ibBookMark;
//    private TextView tvBookPrice;

    private boolean checked = true;

    public BookMarkListViewAdapter(ArrayList<String> bookList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context parentContext = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book_list_mark, parent, false);
        }

        ImageView ivBookImage = convertView.findViewById(R.id.img_book);
        TextView tvBookName = convertView.findViewById(R.id.tv_book_name);
        TextView tvBookInfo = convertView.findViewById(R.id.tv_book_info);
        TextView tvSchoolNames = convertView.findViewById(R.id.tv_book_schoolname);
        final ImageButton ibBookMark = convertView.findViewById(R.id.ib_bookmark);
        TextView tvBookPrice = convertView.findViewById(R.id.tv_book_price);

        // TODO 북마크 해제 하면 북마크 리스트에서 삭제 --> 전체 DataCenter 만들어야 하나...??
        ibBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked) {
                    ibBookMark.setImageResource(R.drawable.ic_bookmark);
                    checked = false;
                }
                else {
                    ibBookMark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    checked = true;
                }
            }
        });

        String bookRegisterNum = bookList.get(position);

        // TODO DB에서 불러와서 해당 책 등록번호에 맞는 책 이미지, 책 이름, 책 정보 불러와서 띄우기
        ivBookImage.setImageAlpha(R.mipmap.ic_launcher);
        tvBookName.setText(bookRegisterNum);
        tvBookInfo.setText("차은호 / 겨루");
        tvSchoolNames.setText("중앙대 서울캠, 홍대");
        tvBookPrice.setText("50000원");


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 책 페이지로 넘어가야함
                Toast.makeText(parentContext, bookList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
