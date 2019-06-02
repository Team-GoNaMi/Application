package com.example.gonami.bookboxbook.TransactionList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gonami.bookboxbook.BookMarket.BookSellDetailFragment;
import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.BookTradeInformation;
import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;
import com.example.gonami.bookboxbook.RecognizeCode.QRActivity;
import com.example.gonami.bookboxbook.TransactionProcess.RateActivity;
import com.example.gonami.bookboxbook.TransactionProcess.ReportActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BuyListViewAdapter extends BaseAdapter {

    private ArrayList<BookInformation> bookList;
    private ArrayList<BookTradeInformation> tradeList;

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "BuyListViewAdapter";

    public BuyListViewAdapter(ArrayList<BookInformation> buyList, ArrayList<BookTradeInformation> tradeList) {
        this.bookList = buyList;
        this.tradeList = tradeList;
    }

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
        TextView tvBookOriginPrice = convertView.findViewById(R.id.tv_original_price);
        TextView tvBookPrice = convertView.findViewById(R.id.tv_book_price);

        final Button btnBookState = convertView.findViewById(R.id.btn_book_state);

        final BookInformation bookInfo = bookList.get(position);
        final BookTradeInformation bookTrade = tradeList.get(position);

        Glide.with(parentContext).load(bookInfo.getFirstBookImage()).into(ivBookImage);

        tvBookName.setText(bookInfo.getBookName());
        tvBookInfo.setText(bookInfo.getAuthor() + " / " +bookInfo.getPublisher());
        tvSchoolNames.setText(bookInfo.getSchoolString());
        tvBookOriginPrice.setText(bookInfo.getOriginal_price() + "원");
        tvBookOriginPrice.setPaintFlags(tvBookOriginPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        tvBookPrice.setText(bookInfo.getSellingPrice() + "원");
        btnBookState.setText("판매자가 북박스 예약 중");

        switch (bookTrade.getStatus()){
            //북박스예약
            case 1:
                btnBookState.setText("북박스 예약중");
                btnBookState.setClickable(false);
                break;
            //예약정보(책넣어주세요)
            case 2:
                btnBookState.setText("책 넣는 중");
                btnBookState.setClickable(false);
                break;
                //qr 나와야되여
            case 3:
                btnBookState.setText("책을 가져가 주세요");
                btnBookState.setClickable(true);
                break;
            case 4:
                btnBookState.setText("구매 확정해 주세요");
                btnBookState.setClickable(true);
                break;

                //TODO 거래정보 보여주면 좋을듯
           case 5:
                btnBookState.setText("거래완료");
                btnBookState.setClickable(false);
                break;
            case 6:
                btnBookState.setText("거래완료");
                btnBookState.setClickable(false);
                break;

            case 7:
                btnBookState.setText("신고접수중");
                btnBookState.setClickable(false);
                break;

            case 8:
                btnBookState.setText("거래 취소");
                btnBookState.setClickable(false);
                break;

        }
//판매자 -> state id를 받아오면 될듯
        btnBookState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bookTrade.getStatus()){
                    //책을 가져가주세요
                    case 3:
                        Intent intent = new Intent(parentContext, QRActivity.class);

                        intent.putExtra("register_id", bookInfo.getRegister_id());
                        intent.putExtra("ISBN", bookInfo.getISBN());
                        intent.putExtra("role", false);
                        parentContext.startActivity(intent);
                        break;
                    //구매확정해주세요 //팝업창이면 될듯 //평가로 넘어갑니당
                    case 4:
                        AlertDialog.Builder buyComplite = new AlertDialog.Builder(parentContext);

                        buyComplite.setTitle("구매를 확정하시겠습니까?(10분) 아닐경우 신고하기로 넘어갑니다.").setCancelable(
                                false).setNegativeButton("신고하기",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(parentContext, ReportActivity.class);
                                        intent.putExtra("register_id", bookInfo.getRegister_id());
                                        parentContext.startActivity(intent);
                                    }
                                }).setPositiveButton("네",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int id) {
                                        //평가로 넘어가거나
                                        Intent intent = new Intent(parentContext, RateActivity.class);
                                        intent.putExtra("register_id", bookInfo.getRegister_id());
                                        parentContext.startActivity(intent);
                                    }
                                });
                        AlertDialog alert = buyComplite.create();
                        alert.show();
                        break;
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 책 페이지로 넘어가야함
                BookSellDetailFragment bookSellDetailFragment;
                Bundle bundle = new Bundle();
                bundle.putString("BookRegisterID", bookInfo.getRegister_id());
                bundle.putString("from", "Buy");
                bookSellDetailFragment = BookSellDetailFragment.newInstance(bundle);

                Log.i("buyList", bookInfo.getRegister_id());

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
