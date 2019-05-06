package com.example.gonami.bookboxbook.TransactionList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.DataModel.BookInformation;
import com.example.gonami.bookboxbook.DataModel.BookTradeInformation;
import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.R;

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

public class BuyListFragment extends Fragment {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "BuyList";

    private View thisView = null;

    private ListView buyListView;
    private BuyListViewAdapter buyListAdapter;

    private ArrayList<String> buyList;
//    private ArrayList<BookInformation> buyList;
//    private ArrayList<BookTradeInformation> tradeList;

    public BuyListFragment() {

    }

    public static BuyListFragment newInstance() {
        BuyListFragment fragment = new BuyListFragment();
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
            thisView = inflater.inflate(R.layout.fragment_list_buy, null);

        return thisView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buyList = new ArrayList<String>();

        //Test
        buyList.add("이것들이 바로");
        buyList.add("내가 산 책");
        buyList.add("입니당!");

        buyListView = (ListView)thisView.findViewById(R.id.lv_buy_list);
        buyListAdapter = new BuyListViewAdapter(buyList);
        buyListView.setAdapter(buyListAdapter);


        //실제
        // 사용자 아이디 받아오기
//        String user_id = SaveSharedPreference.getUserID(getContext());

        // DB에서 불러와서 ArrayList에 저장
//        buyList = new ArrayList<BookInformation>();
//        tradeList = new ArrayList<BookTradeInformation>();
//        GetBuyBookData task = new GetBuyBookData();
//        //파일이름바꾸기
//        task.execute("https://" + IP_ADDRESS + "/get-book-trade.php", user_id);
//
//        buyListView = (ListView)thisView.findViewById(R.id.lv_buy_list);
//        buyListAdapter = new BuyListViewAdapter(buyList, tradeList);
//        buyListView.setAdapter(buyListAdapter);
    }
//
//    private class GetBuyBookData extends AsyncTask<String, Void, String> {
//
//        String errorString = null;
//
//        private String userJsonString;
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            Log.d(TAG, "response1 - " + s.length() + " : " + s);
//
//            if (s.length() == 0){
//                Log.i(TAG, errorString);
//            }
//            else {
//                userJsonString = s;
//                showResult();
//            }
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String serverURL = strings[0];
//            String user_id = strings[1];
//            String postParameters = "user_id=" + user_id;
//
//            Log.i(TAG, "user_id : " + user_id);
//
//            try {
//                URL url = new URL(serverURL);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
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
//                Log.d(TAG, "GetSellBookData - Error : ", e);
//                errorString = e.toString();
//            }
//            return null;
//        }
//
//        private void showResult() {
//            String TAG_BASIC = "book_list";
//            String TAG_REGISTER_ID = "register_id";
//            String TAG_BOOK_NAME = "book_name";
//            String TAG_AUTHOR = "author";
//            String TAG_PUBLISHER = "publisher";
//            String TAG_ORIGINAL_PRICE = "original_price";
//            String TAG_SELLING_PRICE ="selling_price";
//            String TAG_SCHOOL = "school";
//            String TAG_BOOK_IMAGE = "book_image";
//            String TAG_STATE = "state";
//
//
//
//            try {
//                JSONObject jsonObject = new JSONObject(userJsonString);
//                JSONArray jsonArray = jsonObject.getJSONArray(TAG_BASIC);
//
//                if (jsonArray.length() != 0) {
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject item = jsonArray.getJSONObject(i);
//
//                        BookInformation bookInformation = new BookInformation(item.getString(TAG_REGISTER_ID), item.getString(TAG_BOOK_NAME),
//                                item.getString(TAG_AUTHOR), item.getString(TAG_PUBLISHER),
//                                item.getString(TAG_ORIGINAL_PRICE), item.getString(TAG_SELLING_PRICE), false, item.getString(TAG_SCHOOL), item.getString(TAG_BOOK_IMAGE));
//                        buyList.add(bookInformation);
//                        Log.i(TAG, buyList.get(i).getBookName());
//
//                        BookTradeInformation bookTradeInformation = new BookTradeInformation(item.getString(TAG_REGISTER_ID), "", "", item.getInt(TAG_STATE));
//                        tradeList.add(bookTradeInformation);
//                        Log.i(TAG, tradeList.get(i).getBookRegister_id() + tradeList.get(i).getStatus());
//                    }
//                }
//                else {
//                    Toast.makeText(getContext(), "구매한 책이 없습니다.", Toast.LENGTH_SHORT).show();
//                }
//
//                // 어뎁터 생성
//                buyListAdapter = new SellListViewAdapter(buyList, tradeList);
//                buyListView.setAdapter(buyListAdapter);
//
//            } catch (JSONException e) {
//                Log.i(TAG, "showResult : ", e);
//            }
//        }
//    }
//
}
