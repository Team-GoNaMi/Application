package com.example.gonami.bookboxbook.BookMarket;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;

public class BookSearchListViewAdapter extends BaseAdapter {

    private ArrayList<BookInformation> bookList;

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
    public View getView(int position, View convertView, final ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book_list, parent, false);
        }

        ImageView ivBookImage = (ImageView)convertView.findViewById(R.id.img_book);
        TextView tvBookName = convertView.findViewById(R.id.tv_book_name);
        TextView tvBookInfo = convertView.findViewById(R.id.tv_book_info);
        TextView tvSchoolNames = convertView.findViewById(R.id.tv_book_schoolname);
        TextView tvBookOriginPrice = convertView.findViewById(R.id.tv_book_original_price);
        TextView tvBookPrice = convertView.findViewById(R.id.tv_book_price);

        final BookInformation bookInfo = bookList.get(position);

        if(bookInfo.getBook_image() != null){
            Log.i("gg", "북이미지가 널이 아님");
            ivBookImage.setImageURI(Uri.parse(bookInfo.getFirstImage()));    // 책 이미지
            Log.i("gg", "uri는" + Uri.parse(bookInfo.getFirstImage()));

        }

        tvBookName.setText(bookInfo.getBookName());
        tvBookInfo.setText(bookInfo.getAuthor() + " / " +bookInfo.getPublisher());
        tvSchoolNames.setText("중앙대 서울캠, 숙명여대");     // TODO 거래 장소
        tvBookOriginPrice.setText(bookInfo.getOriginal_price() + "원");
        tvBookOriginPrice.setPaintFlags(tvBookOriginPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        tvBookPrice.setText(bookInfo.getSellingPrice() + "원");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 책 페이지로 넘어가야함
                BookSellDetailFragment bookSellDetailFragment;
                Bundle bundle = new Bundle();
                bundle.putString("BookRegisterID", bookInfo.getRegister_id());
                bookSellDetailFragment = BookSellDetailFragment.newInstance(bundle);

                Log.i("Search", bookInfo.getRegister_id());

                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, bookSellDetailFragment)
                        .commit();

                MainActivity.activeFragment = bookSellDetailFragment;
            }
        });

        return convertView;
    }

}
