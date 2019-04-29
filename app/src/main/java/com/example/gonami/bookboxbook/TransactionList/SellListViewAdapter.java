package com.example.gonami.bookboxbook.TransactionList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gonami.bookboxbook.BookMarket.BookSellDetailFragment;
import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SellListViewAdapter extends BaseAdapter {

    private ArrayList<BookInformation> bookList;

    private Button btnBookState;
    private Bitmap bitmap;

    public SellListViewAdapter(ArrayList<BookInformation> sellList) { this.bookList = sellList; }

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

        final ImageView ivBookImage = convertView.findViewById(R.id.img_book);
        TextView tvBookName = convertView.findViewById(R.id.tv_book_name);
        TextView tvBookInfo = convertView.findViewById(R.id.tv_book_info);
        TextView tvSchoolNames = convertView.findViewById(R.id.tv_book_schoolname);
        TextView tvBookOriginPrice = convertView.findViewById(R.id.tv_original_price);
        TextView tvBookPrice = convertView.findViewById(R.id.tv_book_price);

        btnBookState = convertView.findViewById(R.id.btn_book_state);

        final BookInformation bookInfo = bookList.get(position);

        if (bookInfo.isImageExist()) {

            new Thread() {
                @SuppressLint("HandlerLeak")
                Handler handler = new Handler() {
                    public void handleMessage(Message msg) {
                        ivBookImage.setImageBitmap(bitmap);
                    }
                };
                public void run() {


                    try {
                        URL url = new URL(bookInfo.getFirstBookImage());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream inputStream = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bundle bun = new Bundle();
                    bun.putString("DATA", "image");

                    Message msg = handler.obtainMessage();
                    msg.setData(bun);
                    handler.sendMessage(msg);

                }
            }.start();

        }
        tvBookName.setText(bookInfo.getBookName());
        tvBookInfo.setText(bookInfo.getAuthor() + " / " +bookInfo.getPublisher());
        tvSchoolNames.setText(bookInfo.getSchoolString());
        tvBookOriginPrice.setText(bookInfo.getOriginal_price() + "원");
        tvBookOriginPrice.setPaintFlags(tvBookOriginPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        tvBookPrice.setText(bookInfo.getSellingPrice() + "원");

        btnBookState.setText("책을 가져가주세욤!");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 책 페이지로 넘어가야함
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
