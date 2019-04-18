package com.example.gonami.bookboxbook.BookMarket;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

    private View thisView = null;
    private String searchWord = "";

    private ListView bookListView;
    private BookSearchListViewAdapter bookSearchListViewAdapter;

    private ArrayList<String> searchList;

    private Button testBtn;

    private ArrayList<BookInformation> bookList;
    //adapter에 넣을것
    private String tv_book_name;
    private String tv_book_info;//저자/출판사
    private String  tv_book_original_price;
    private String  tv_book_price;

    private String tv_book_schoolname;

//    private SearchView searchView;
    private EditText etSearchBook;
    private Button btnSearch;

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

        bookList = new ArrayList<BookInformation>();
        bookListView = view.findViewById(R.id.lv_book_market);

        GetRegisterBookData task = new GetRegisterBookData();
        task.execute("https://" + IP_ADDRESS + "/get-book-search.php", searchWord);

        bookSearchListViewAdapter = new BookSearchListViewAdapter(bookList);
        bookListView.setAdapter(bookSearchListViewAdapter);


// Test if it shows book detail
//        testBtn = thisView.findViewById(R.id.testBtn);
//
//        testBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), BookSellDetailFragment.class);
//                //startActivity(intent);
//                BookSellDetailFragment bookSellDetailFragment;
//
//                Bundle bundle = new Bundle();
//                bundle.putString("BookRegisterID", "20190417222503-1");
//                bookSellDetailFragment = BookSellDetailFragment.newInstance(bundle);
//
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//
//                fragmentManager.beginTransaction()
//                               .replace(R.id.frame_layout, bookSellDetailFragment)
//                               .commit();
//
//                MainActivity.activeFragment = bookSellDetailFragment;
//
//            }
//        });


//        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent = new Intent(getContext(), BookSellDetailFragment.class);
//                //startActivity(intent);
//                BookSellDetailFragment bookSellDetailFragment;
//
//                Bundle bundle = new Bundle();
//                bundle.putString("BookRegisterID", "20190415231107-1");
//                bookSellDetailFragment = BookSellDetailFragment.newInstance(bundle);
//
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//
//                fragmentManager.beginTransaction()
//                        .replace(R.id.frame_layout, bookSellDetailFragment)
//                        .commit();
//
//                MainActivity.activeFragment = bookSellDetailFragment;
//            }
//        });
    }
    private class GetRegisterBookData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        private String userJsonString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(),
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
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
            String postParameters = "searchWord=" + search_word;
            Log.i(TAG, "searchWord : " + search_word);

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
                Log.d(TAG, "GetUserData : Error ", e);
                errorString = e.toString();
            }
            return null;
        }

        private void showResult() {
            String TAG_BASIC = "basic";
            String TAG_SUCCESS = "success";
            String TAG_REGISTER_ID = "register_id";
            String TAG_BOOK_NAME = "book_name";
            String TAG_AUTHOR = "author";
            String TAG_PUBLISHER = "publisher";
            String TAG_ORIGINAL_PRICE = "original_price";
            String TAG_SELLING_PRICE ="selling_price";

            boolean success;
            try {
                JSONObject jsonObject = new JSONObject(userJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_BASIC);

                for(int i = 0; i<jsonArray.length();i++){
                    JSONObject item = jsonArray.getJSONObject(i);

                    BookInformation bookInformation = new BookInformation(item.getString(TAG_BOOK_NAME),
                            item.getString(TAG_AUTHOR), item.getString(TAG_PUBLISHER),
                            item.getString(TAG_ORIGINAL_PRICE), item.getString(TAG_SELLING_PRICE));
                    bookList.add(bookInformation);
//                        bookSearchListViewAdapter.addItem(bookInformation);
                    Log.i(TAG, bookList.get(i).getBookName());
                }

            } catch (JSONException e) {
                Log.i(TAG, "showResult : ", e);
            }
        }

    }


}
