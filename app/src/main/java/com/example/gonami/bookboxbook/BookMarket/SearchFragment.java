package com.example.gonami.bookboxbook.BookMarket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gonami.bookboxbook.DataModel.BookInformation;
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

public class SearchFragment extends Fragment {
    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "Search";

    private String searchWord = "";
    private String school = "";

    private View thisView = null;

    private ListView bookListView;
    private BookSearchListViewAdapter bookSearchListViewAdapter;

    private ArrayList<BookInformation> bookList;

//    private SearchView searchView;
    private EditText etSearchBook;
    private ImageButton btnSearch;
    private Spinner spinSearch;
    private ArrayAdapter adapter;

    public SearchFragment() {

    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
//        args.putString();
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
            thisView = inflater.inflate(R.layout.fragment_search, null);

        return thisView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.school));
        bookList = new ArrayList<BookInformation>();
        GetRegisterBookData task = new GetRegisterBookData();
        task.execute("https://" + IP_ADDRESS + "/get-book-search.php", searchWord, school);

        bookListView = view.findViewById(R.id.lv_book_market);
        etSearchBook = view.findViewById(R.id.et_search_book);
        btnSearch = view.findViewById(R.id.btn_search);
        spinSearch = view.findViewById(R.id.spin_search);
        spinSearch.setPrompt("학교선택");
        spinSearch.setAdapter(adapter);
        bookSearchListViewAdapter = new BookSearchListViewAdapter(bookList);
        bookListView.setAdapter(bookSearchListViewAdapter);
        etSearchBook.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btnSearch.performClick();
                    return true;
                }
                return false;
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO 디비에서 검색한다
                GetRegisterBookData task = new GetRegisterBookData();
                task.execute("https://" + IP_ADDRESS + "/get-book-search.php", searchWord, school);

            }
        });
        spinSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    school = "all";
                }
                else {
                    school = spinSearch.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class GetRegisterBookData extends AsyncTask<String, Void, String> {

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
            String search_word = strings[1];
            String school = strings[2];

            String postParameters = "searchWord=" + search_word + "& school=" + school;
            Log.i(TAG, "searchWord : " + postParameters);

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
                Log.d(TAG, "GetRegisterBookData - Error :", e);
                errorString = e.toString();
            }
            return null;
        }

        private void showResult() {
            String TAG_BASIC = "basic";
            String TAG_REGISTER_ID = "register_id";
            String TAG_BOOK_NAME = "book_name";
            String TAG_AUTHOR = "author";
            String TAG_PUBLISHER = "publisher";
            String TAG_ORIGINAL_PRICE = "original_price";
            String TAG_SELLING_PRICE ="selling_price";
            String TAG_SCHOOL = "school";
            String TAG_BOOK_IMAGE = "book_image";

            try {
                JSONObject jsonObject = new JSONObject(userJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_BASIC);

                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);

                        BookInformation bookInformation = new BookInformation(item.getString(TAG_REGISTER_ID), item.getString(TAG_BOOK_NAME),
                                item.getString(TAG_AUTHOR), item.getString(TAG_PUBLISHER),
                                item.getString(TAG_ORIGINAL_PRICE), item.getString(TAG_SELLING_PRICE), false, item.getString(TAG_SCHOOL), item.getString(TAG_BOOK_IMAGE));
                        bookList.add(bookInformation);
                        Log.i(TAG, bookList.get(i).getBookName());
                    }
                }
                else {
                    Toast.makeText(getContext(), "책 목록이 없습니다.", Toast.LENGTH_SHORT).show();
                }

                // 어뎁터 생성
                bookSearchListViewAdapter = new BookSearchListViewAdapter(bookList);
                bookListView.setAdapter(bookSearchListViewAdapter);

            } catch (JSONException e) {
                Log.i(TAG, "showResult : ", e);
            }
        }

    }


}
