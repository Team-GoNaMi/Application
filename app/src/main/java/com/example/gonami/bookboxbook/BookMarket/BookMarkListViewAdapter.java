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

    private Context context;
    private ArrayList<String> bookList;

    private ImageView ivBookImage;
    private TextView tvBookName;
    private TextView tvBookInfo;
    private TextView tvSchoolNames;
    private ImageButton ibBookMark;
    private TextView tvBookPrice;

    private boolean checked = true;

    public BookMarkListViewAdapter(Context context, ArrayList<String> bookList) {
        this.context = context;
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

        ivBookImage = convertView.findViewById(R.id.img_book);
        tvBookName = convertView.findViewById(R.id.tv_book_name);
        tvBookInfo = convertView.findViewById(R.id.tv_book_info);
        tvSchoolNames = convertView.findViewById(R.id.tv_book_schoolname);
        ibBookMark = convertView.findViewById(R.id.ib_bookmark);
        tvBookPrice = convertView.findViewById(R.id.tv_book_price);


        // TODO 왜야 맨 밑에 아이만 북마크 표시가 되는 거시야ㅠㅠㅠㅠ
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
