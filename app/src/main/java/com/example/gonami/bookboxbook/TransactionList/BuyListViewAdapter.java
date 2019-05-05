package com.example.gonami.bookboxbook.TransactionList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gonami.bookboxbook.BookMarket.BookSellDetailFragment;
import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.BookTradeInformation;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;
import com.example.gonami.bookboxbook.RecognizeCode.QRActivity;
import com.example.gonami.bookboxbook.TransactionProcess.RateActivity;

import java.util.ArrayList;

public class BuyListViewAdapter extends BaseAdapter {

    private ArrayList<String> bookList;
//    private ArrayList<BookInformation> bookList;
//    private ArrayList<BookTradeInformation> tradeList;
//    private android.content.Intent Intent;

    public BuyListViewAdapter(ArrayList<String> buyList) { this.bookList = buyList; }

//    public BuyListViewAdapter(ArrayList<BookInformation> buyList, ArrayList<BookTradeInformation> tradeList) {
//        this.bookList = buyList;
//        this.tradeList = tradeList;
//    }
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

//실제
//        final BookInformation bookInfo = bookList.get(position);
//
//        Glide.with(parentContext).load(bookInfo.getFirstBookImage()).into(ivBookImage);
//
//        tvBookName.setText(bookInfo.getBookName());
//        tvBookInfo.setText(bookInfo.getAuthor() + " / " +bookInfo.getPublisher());
//        tvSchoolNames.setText(bookInfo.getSchoolString());
//        tvOriginPrice.setText(bookInfo.getOriginal_price() + "원");
//        tvOriginPrice.setPaintFlags(tvOriginPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
//        tvBookPrice.setText(bookInfo.getSellingPrice() + "원");
//
//        final BookTradeInformation bookTrade = tradeList.get(position);
//
//        switch (bookTrade.getStatus()){
//            //북박스 예약중
//            case 1:
//                btnBookState.setText("북박스 예약중");
//                btnBookState.setClickable(false);
//                break;
//
//            //책을 가져가주세요
//            case 4:
//                btnBookState.setText("책을 가져가주세요");
//                btnBookState.setClickable(true);
//                break;
//            //구매확정해주세요
//            case 5:
//                btnBookState.setText("구매확정");
//                btnBookState.setClickable(true);
//                break;
//            //평가
//            case 7:
//                btnBookState.setText("평가");
//                btnBookState.setClickable(true);
//                break;
//
//        }
////판매자 -> state id를 받아오면 될듯
//        btnBookState.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (bookTrade.getStatus()){
//                    //책을 가져가주세요
//                    case 4:
//                        Intent = new Intent(parentContext, QRActivity.class);
//                        parentContext.startActivity(Intent);
//                        break;
//                    //구매확정해주세요 //팝업창이면 될듯
//                    case 5:
//                        AlertDialog.Builder buyComplite = new AlertDialog.Builder(parentContext);
//
//                        buyComplite.setTitle("구매를 확정하시겠습니까?(10분)").setCancelable(
//                                false).setNegativeButton("아니요",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        //경고창한번더? 신고하기해주세요
//                                    }
//                                }).setPositiveButton("네",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialogInterface, int id) {
//                                        //평가로 넘어가거나
//                                    }
//                                });
//                        AlertDialog alert = buyComplite.create();
//                        alert.show();
//                        break;
//                    //평가
//                    case 7:
//                        Intent = new Intent(parentContext, RateActivity.class);
//                        parentContext.startActivity(Intent);
//                        break;
//                }
//            }
//        });
//
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 책 페이지로 넘어가야함
//                BookSellDetailFragment bookSellDetailFragment;
//                Bundle bundle = new Bundle();
//                bundle.putString("BookRegisterID", bookInfo.getRegister_id());
//                bookSellDetailFragment = BookSellDetailFragment.newInstance(bundle);
//
//                Log.i("buyList", bookInfo.getRegister_id());
//
//                FragmentManager fragmentManager = ((MainActivity)parentContext).getSupportFragmentManager();
//
//                fragmentManager.beginTransaction()
//                        .replace(R.id.frame_layout, bookSellDetailFragment)
//                        .commit();
//
//                MainActivity.activeFragment = bookSellDetailFragment;
//            }
//        });
//
//
//        return convertView;
    }
}
