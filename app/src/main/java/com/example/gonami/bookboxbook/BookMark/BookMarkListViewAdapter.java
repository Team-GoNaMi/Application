package com.example.gonami.bookboxbook.BookMark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gonami.bookboxbook.BookMarket.BookSellDetailFragment;
import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BookMarkListViewAdapter extends BaseAdapter {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private String TAG = "BookMarkAdapter";
    private Bitmap bitmap;
    private ArrayList<BookInformation> bookList;

    public BookMarkListViewAdapter(ArrayList<BookInformation> bookList) {
        this.bookList = bookList;
//        this.removedMarks = new ArrayList<Integer>();
    }

//    public ArrayList<Integer> getRemovedMarks() {
//        return removedMarks;
//    }

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

        final ImageView ivBookImage = convertView.findViewById(R.id.img_book);
        TextView tvBookName = convertView.findViewById(R.id.tv_book_name);
        TextView tvBookInfo = convertView.findViewById(R.id.tv_book_info);
        TextView tvSchoolNames = convertView.findViewById(R.id.tv_book_schoolname);
        TextView tvBookOriginalPrice = convertView.findViewById(R.id.tv_book_original_price);
        TextView tvBookPrice = convertView.findViewById(R.id.tv_book_price);

        final ImageButton ibBookMark = convertView.findViewById(R.id.ib_bookmark);

        final BookInformation bookInfo = bookList.get(position);

//        if (bookInfo.isImageExist()) {
//            Glide.with(parentContext).load(bookInfo.getFirstBookImage()).into(ivBookImage);
//        }
        Glide.with(parentContext).load(bookInfo.getFirstBookImage()).into(ivBookImage);

        tvBookName.setText(bookInfo.getBookName());
        tvBookInfo.setText(bookInfo.getAuthor() + " / " +bookInfo.getPublisher());
        tvSchoolNames.setText(bookInfo.getSchoolString());
        tvBookOriginalPrice.setText(bookInfo.getOriginal_price() + "원");
        tvBookPrice.setText(bookInfo.getSellingPrice() + "원");

        // 북마크 해제 하면 북마크 리스트에서 삭제
        ibBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookInfo.isBookmark()) {      // 북마크 해제
                    ibBookMark.setImageResource(R.drawable.ic_bookmark);
                    bookInfo.convertBookmark();
                    Log.i(TAG, bookInfo.getBookName() +" : " + bookInfo.isBookmark());

                    String user_id = SaveSharedPreference.getUserID(parentContext);
                    String register_id = bookInfo.getRegister_id();
                    SendBookMarkData task = new SendBookMarkData();
                    task.execute("https://" + IP_ADDRESS + "/set-bookmark.php", user_id, register_id, "1");
                }
                else {              // 북마크 등록
                    ibBookMark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    bookInfo.convertBookmark();
                    Log.i(TAG, bookInfo.getBookName() +" : " + bookInfo.isBookmark());

                    String user_id = SaveSharedPreference.getUserID(parentContext);
                    String register_id = bookInfo.getRegister_id();
                    SendBookMarkData task = new SendBookMarkData();
                    task.execute("https://" + IP_ADDRESS + "/set-bookmark.php", user_id, register_id, "2");
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 책 상세 페이지로 넘어감
                BookSellDetailFragment bookSellDetailFragment;
                Bundle bundle = new Bundle();
                bundle.putString("BookRegisterID", bookInfo.getRegister_id());
                bookSellDetailFragment = BookSellDetailFragment.newInstance(bundle);

                Log.i("SellList", bookInfo.getRegister_id());

                FragmentManager fragmentManager = ((MainActivity)parentContext).getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, bookSellDetailFragment)
                        .commit();

                MainActivity.activeFragment = bookSellDetailFragment;
            }
        });

        return convertView;
    }

}
