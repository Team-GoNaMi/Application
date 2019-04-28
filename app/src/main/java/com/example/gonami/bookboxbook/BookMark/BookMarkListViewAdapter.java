package com.example.gonami.bookboxbook.BookMark;

import android.content.Context;
import android.os.Bundle;
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

import com.example.gonami.bookboxbook.BookMarket.BookSellDetailFragment;
import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;

public class BookMarkListViewAdapter extends BaseAdapter {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private String TAG = "BookMarkAdapter";

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

        ImageView ivBookImage = convertView.findViewById(R.id.img_book);
        TextView tvBookName = convertView.findViewById(R.id.tv_book_name);
        TextView tvBookInfo = convertView.findViewById(R.id.tv_book_info);
        TextView tvSchoolNames = convertView.findViewById(R.id.tv_book_schoolname);
        TextView tvBookOriginalPrice = convertView.findViewById(R.id.tv_book_original_price);
        TextView tvBookPrice = convertView.findViewById(R.id.tv_book_price);

        final ImageButton ibBookMark = convertView.findViewById(R.id.ib_bookmark);

        final BookInformation bookInfo = bookList.get(position);

        ivBookImage.setImageAlpha(R.mipmap.ic_launcher);        // TODO 책 이미지
        tvBookName.setText(bookInfo.getBookName());
        tvBookInfo.setText(bookInfo.getAuthor() + " / " +bookInfo.getPublisher());
        tvSchoolNames.setText("중앙대 서울캠, 홍대");           // TODO 거래 장소
        tvBookOriginalPrice.setText(bookInfo.getOriginal_price() + "원");
        tvBookPrice.setText(bookInfo.getSellingPrice() + "원");

        // TODO 북마크 해제 하면 북마크 리스트에서 삭제
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

//    private class SendBookMarkData extends AsyncTask<String, Void, String> {
//
//        String errorString = null;
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            Log.d(TAG, "response1 - " + s.length() + " : " + s);
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            String serverURL = strings[0];
//            String user_id = strings[1];
//            String register_id = strings[2];
//            String state = strings[3];
//            String postParameters = "user_id=" + user_id + "& register_id=" + register_id + "& state=" + state;
//            Log.i(TAG, "postParameters : " + postParameters);
//
//            try {
//                URL url = new URL(serverURL);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//                httpURLConnection.setReadTimeout(5000);
//                httpURLConnection.setConnectTimeout(5000);
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.connect();
//
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                outputStream.write(postParameters.getBytes("UTF-8"));
//                outputStream.flush();
//                outputStream.close();
//
//
//                int responseStatusCode = httpURLConnection.getResponseCode();
//                Log.d(TAG, "response code2 - " + responseStatusCode);
//
//                InputStream inputStream;
//                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
//                    inputStream = httpURLConnection.getInputStream();
//                }
//                else{
//                    inputStream = httpURLConnection.getErrorStream();
//                }
//
//
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                StringBuilder sb = new StringBuilder();
//                String line;
//
//                while((line = bufferedReader.readLine()) != null){
//                    sb.append(line);
//                }
//
//                bufferedReader.close();
//
//                return sb.toString().trim();
//
//            } catch (Exception e) {
//                Log.d(TAG, "SendBookMarkData - Error :", e);
//                errorString = e.toString();
//            }
//            return null;
//        }
//    }

}
