package com.example.gonami.bookboxbook.TransactionList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.BookTradeInformation;
import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.R;
import com.example.gonami.bookboxbook.RecognizeCode.QRActivity;
import com.example.gonami.bookboxbook.TransactionProcess.BookBoxBookActivity;
import com.example.gonami.bookboxbook.TransactionProcess.RateActivity;
import com.example.gonami.bookboxbook.TransactionProcess.RegisterBankAccountActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SellListFragment extends Fragment {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "SellList";

    private View thisView = null;

    private LinearLayout linearLayout;
    private ListView sellListView;
    private SellListViewAdapter sellListViewAdapter;

    private ArrayList<BookInformation> sellList;
    private ArrayList<BookTradeInformation> tradeList;


    public SellListFragment() {

    }

    public static SellListFragment newInstance() {
        SellListFragment fragment = new SellListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (thisView == null)
            thisView = inflater.inflate(R.layout.fragment_list_sell, null);

        return thisView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // 사용자(판매자) 아이디 받아오기
        String seller_id = SaveSharedPreference.getUserID(getContext());

        // DB에서 불러와서 ArrayList에 저장
        sellList = new ArrayList<BookInformation>();
        tradeList = new ArrayList<BookTradeInformation>();

        GetSellBookData task = new GetSellBookData();
        task.execute("https://" + IP_ADDRESS + "/get-book-trade.php", seller_id);

        sellListView = thisView.findViewById(R.id.lv_sell_list);
        sellListViewAdapter = new SellListViewAdapter(sellList, tradeList);
        sellListView.setAdapter(sellListViewAdapter);

        linearLayout = thisView.findViewById(R.id.linear_layout);
        linearLayout.setVisibility(View.INVISIBLE);

        Log.i(TAG, ">>>" + tradeList.size());
    }


    private class GetSellBookData extends AsyncTask<String, Void, String> {

        String errorString = null;

        private String userJsonString;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "response1 - " + s.length() + " : " + s);

            if (s.length() == 0){
                Log.i(TAG, errorString);
            }
            else {
                userJsonString = s;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverURL = strings[0];
            String user_id = strings[1];
            String postParameters = "user_id=" + user_id + "& state=1";

            Log.i(TAG, "postParamenters : " + postParameters);

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code2 - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "GetSellBookData - Error : ", e);
                errorString = e.toString();
            }
            return null;
        }

        private void showResult() {
            String TAG_BASIC = "book_list";
            String TAG_REGISTER_ID = "register_id";
            String TAG_ISBN = "isbn";
            String TAG_BOOK_NAME = "book_name";
            String TAG_AUTHOR = "author";
            String TAG_PUBLISHER = "publisher";
            String TAG_ORIGINAL_PRICE = "original_price";
            String TAG_SELLING_PRICE ="selling_price";
            String TAG_SCHOOL = "school";
            String TAG_SELECTED_SCHOOL = "selected_school";
            String TAG_BOOK_IMAGE = "book_image";
            String TAG_STATE = "state";



            try {
                JSONObject jsonObject = new JSONObject(userJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_BASIC);

                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);

                        BookInformation bookInformation = new BookInformation(item.getString(TAG_REGISTER_ID), item.getString(TAG_ISBN), item.getString(TAG_BOOK_NAME), item.getString(TAG_AUTHOR),
                                item.getString(TAG_PUBLISHER), item.getString(TAG_ORIGINAL_PRICE), item.getString(TAG_SELLING_PRICE),
                                false, item.getString(TAG_SCHOOL),item.getString(TAG_SELECTED_SCHOOL), item.getString(TAG_BOOK_IMAGE));
                        sellList.add(bookInformation);
                        Log.i(TAG, sellList.get(i).getBookName());

                        BookTradeInformation bookTradeInformation = new BookTradeInformation(item.getString(TAG_REGISTER_ID), "", "", item.getInt(TAG_STATE));
                        tradeList.add(bookTradeInformation);
                        Log.i(TAG, tradeList.get(i).getBookRegister_id() + tradeList.get(i).getStatus());
                    }
                }
                else {
                    if (sellList.size()==0) {
                        linearLayout.setVisibility(View.VISIBLE);

                        Log.i(TAG, "판매 목록 없음");
                        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        TextView no_list = new TextView(getContext());
                        no_list.setText("판매한 책이 없습니다.");
                        no_list.setGravity(Gravity.CENTER);
                        no_list.setLayoutParams(lp);

                        linearLayout.addView(no_list);
                    }
                }

                // 어뎁터 생성
                sellListViewAdapter = new SellListViewAdapter(sellList, tradeList);
                sellListView.setAdapter(sellListViewAdapter);

            } catch (JSONException e) {
                Log.i(TAG, "showResult : ", e);
            }
        }
    }
}
