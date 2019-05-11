package com.example.gonami.bookboxbook.TransactionList;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gonami.bookboxbook.BookMarket.BookSellDetailFragment;
import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.BookTradeInformation;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;
import com.example.gonami.bookboxbook.RecognizeCode.QRActivity;
import com.example.gonami.bookboxbook.TransactionProcess.BookBoxBookActivity;
import com.example.gonami.bookboxbook.TransactionProcess.RegisterBankAccountActivity;

import java.util.ArrayList;

public class SellListViewAdapter extends BaseAdapter {

    private ArrayList<BookInformation> bookList;
    private ArrayList<BookTradeInformation> tradeList;
    private Intent Intent;

    public SellListViewAdapter(ArrayList<BookInformation> sellList, ArrayList<BookTradeInformation> tradeList) {
        this.bookList = sellList;
        this.tradeList = tradeList;
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

        final Button btnBookState = convertView.findViewById(R.id.btn_book_state);

        final BookInformation bookInfo = bookList.get(position);

        Glide.with(parentContext).load(bookInfo.getFirstBookImage()).into(ivBookImage);

        tvBookName.setText(bookInfo.getBookName());
        tvBookInfo.setText(bookInfo.getAuthor() + " / " +bookInfo.getPublisher());
        tvSchoolNames.setText(bookInfo.getSchoolString());
        tvBookOriginPrice.setText(bookInfo.getOriginal_price() + "원");
        tvBookOriginPrice.setPaintFlags(tvBookOriginPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        tvBookPrice.setText(bookInfo.getSellingPrice() + "원");

        final BookTradeInformation bookTrade = tradeList.get(position);

        switch (bookTrade.getStatus()){
            //거래전(판매가능)
            case 0:
                btnBookState.setText("판매가능");
                btnBookState.setClickable(false);
                break;

            //북박스예약
            case 1:
                btnBookState.setText("북박스예약");
                btnBookState.setClickable(true);
                break;
            //예약정보(책넣어주세요)
            case 3:
                btnBookState.setText("책을 넣어주세요");
                btnBookState.setClickable(true);
                break;
            //계좌번호
            case 6:
                btnBookState.setText("계좌번호입력");
                btnBookState.setClickable(true);
                break;
            //입금전
            case 8:
                btnBookState.setText("입금중입니다");
                btnBookState.setClickable(false);
                break;
            //입금완료
            //TODO 거래정보 보여주면 좋을듯
            case 9:
                btnBookState.setText("입금완료");
                btnBookState.setClickable(false);
                break;

        }
//판매자 -> state id를 받아오면 될듯
        btnBookState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bookTrade.getStatus()){
                    //북박스예약
                    case 1:
                        Intent = new Intent(parentContext, BookBoxBookActivity.class);
                        Intent.putExtra("school", bookInfo.getSchool());
                        Intent.putExtra("register_id", bookInfo.getRegister_id());
                        parentContext.startActivity(Intent);
                        break;
                    //예약정보(책넣어주세요)
                    case 3:
                        Intent = new Intent(parentContext, QRActivity.class);
//                        Intent.putExtra("selectSchool",)
                        //선택한 학교, 북박스 번호,
                        Intent.putExtra("register_id", bookInfo.getRegister_id());
                        Intent.putExtra("ISBN", bookInfo.getISBN());
                        parentContext.startActivity(Intent);
                        break;
                    //계좌번호
                    case 6:
                        Intent = new Intent(parentContext, RegisterBankAccountActivity.class);
                        parentContext.startActivity(Intent);
                        break;

//                    //입금완료
//                    //TODO 거래정보 보여주면 좋을듯 팝업창..
//                    case 9:
//                        break;
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
                bundle.putString("from", "Sell");
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
