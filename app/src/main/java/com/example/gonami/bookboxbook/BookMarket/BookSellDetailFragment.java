package com.example.gonami.bookboxbook.BookMarket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gonami.bookboxbook.BookMark.BookMarkFragment;
import com.example.gonami.bookboxbook.BookMark.SendBookMarkData;
import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;
import com.example.gonami.bookboxbook.TransactionList.BuyListFragment;
import com.example.gonami.bookboxbook.TransactionList.SellListFragment;
import com.example.gonami.bookboxbook.TransactionList.TransactionListFragment;

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


public class BookSellDetailFragment extends Fragment implements MainActivity.OnBackPressedListener  {
    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "BookSellDetail";
    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(50, 50);

    private View thisView = null;

    private String book_register_id;
    private String from_fragment;

    private ArrayList<String> bookImage;
    private boolean checked;
    private String seller_id;

    private TextView tvBookName;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvPublishDate;
    private TextView tvOriginalPrice;
    private TextView tvPrice;
    private TextView tvLocation;

    private ImageButton ibBookmark;
    private Button btnBuy;


    //highlight해야될것들

    private TextView tv_book_state_highlight1_o;
    private TextView tv_book_state_highlight1_x;
    private TextView tv_book_state_highlight2_o;
    private TextView tv_book_state_highlight2_x;
    private TextView tv_book_state_writing1_o;
    private TextView tv_book_state_writing1_x;
    private TextView tv_book_state_writing2_o;
    private TextView tv_book_state_writing2_x;
    private TextView tv_book_state_clean_o;
    private TextView tv_book_state_clean_x;
    private TextView tv_book_state_name_o;
    private TextView tv_book_state_name_x;
    private TextView tv_book_state_damage1_o;
    private TextView tv_book_state_damage1_x;
    private TextView tv_book_state_damage2_o;
    private TextView tv_book_state_damage2_x;

    private LinearLayout linearLayout_img;
    private TextView memo;
    private TextView rating;

    public BookSellDetailFragment() {

    }

    public static BookSellDetailFragment newInstance(Bundle bundle) {
        BookSellDetailFragment fragment = new BookSellDetailFragment();
        Bundle args = bundle;
//        args.putString("BookRegisterID",resgister_id);
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
            thisView = inflater.inflate(R.layout.fragment_book_sell_detail, null);

        if (getArguments() != null) {
            book_register_id = getArguments().getString("BookRegisterID");
            from_fragment = getArguments().getString("from");
            Log.i(TAG, from_fragment);
        }

        Log.i(TAG, "gg: "+ SaveSharedPreference.getUserID(getContext()));
        return thisView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBookName = thisView.findViewById(R.id.tv_book_name);
        tvAuthor = thisView.findViewById(R.id.tv_book_author);
        tvPublisher = thisView.findViewById(R.id.tv_book_publisher);
        tvPublishDate = thisView.findViewById(R.id.tv_book_publish_date);
        tvOriginalPrice = thisView.findViewById(R.id.tv_book_original_price);
        tvPrice = thisView.findViewById(R.id.tv_book_price);
        tvLocation = thisView.findViewById(R.id.tv_book_bb_where);

        ibBookmark = thisView.findViewById(R.id.ib_bookmark);
        btnBuy = thisView.findViewById(R.id.btn_buy);
        tv_book_state_highlight1_o = thisView.findViewById(R.id.tv_book_state_highlight1_o);
        tv_book_state_highlight1_x = thisView.findViewById(R.id.tv_book_state_highlight1_x);
        tv_book_state_highlight2_o = thisView.findViewById(R.id.tv_book_state_highlight2_o);
        tv_book_state_highlight2_x = thisView.findViewById(R.id.tv_book_state_highlight2_x);
        tv_book_state_writing1_o = thisView.findViewById(R.id.tv_book_state_writing1_o);
        tv_book_state_writing1_x = thisView.findViewById(R.id.tv_book_state_writing1_x);
        tv_book_state_writing2_o = thisView.findViewById(R.id.tv_book_state_writing2_o);
        tv_book_state_writing2_x = thisView.findViewById(R.id.tv_book_state_writing2_x);
        tv_book_state_clean_o = thisView.findViewById(R.id.tv_book_state_clean_o);
        tv_book_state_clean_x = thisView.findViewById(R.id.tv_book_state_clean_x);
        tv_book_state_name_o = thisView.findViewById(R.id.tv_book_state_name_o);
        tv_book_state_name_x = thisView.findViewById(R.id.tv_book_state_name_x);
        tv_book_state_damage1_o = thisView.findViewById(R.id.tv_book_state_damage1_o);
        tv_book_state_damage1_x = thisView.findViewById(R.id.tv_book_state_damage1_x);
        tv_book_state_damage2_o = thisView.findViewById(R.id.tv_book_state_damage2_o);
        tv_book_state_damage2_x = thisView.findViewById(R.id.tv_book_state_damage2_x);

        linearLayout_img = thisView.findViewById(R.id.linearLayout_img);
        memo = thisView.findViewById(R.id.tv_book_state_memo);
        rating = thisView.findViewById(R.id.tv_rating);

        ibBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("HoiHoi", seller_id  + " / " +SaveSharedPreference.getUserID(getContext()));

                if (!seller_id.equals(SaveSharedPreference.getUserID(getContext()))) {
                    if (checked) {      // 북마크 해제
                        ibBookmark.setImageResource(R.drawable.ic_bookmark);
                        checked = false;
                        Log.i(TAG, "checked = " + checked);

                        String user_id = SaveSharedPreference.getUserID(getContext());
                        SendBookMarkData task = new SendBookMarkData();
                        task.execute("https://" + IP_ADDRESS + "/set-bookmark.php", user_id, book_register_id, "1");
                    } else {              // 북마크 등록
                        ibBookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                        checked = true;
                        Log.i(TAG, "checked = " + checked);

                        String user_id = SaveSharedPreference.getUserID(getContext());
                        SendBookMarkData task = new SendBookMarkData();
                        task.execute("https://" + IP_ADDRESS + "/set-bookmark.php", user_id, book_register_id, "2");
                    }
                }
                else {
                    Toast.makeText(getContext(), "본인 책이에요! 돌아가요~ 호이호이", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!seller_id.equals(SaveSharedPreference.getUserID(getContext()))) {
                    Intent Intent = new Intent(getActivity(), BuyActivity.class);
                    Intent.putExtra("book_regist_id", book_register_id);
                    Intent.putExtra("schools", tvLocation.getText());
                    Intent.putExtra("book_name",  tvBookName.getText());
                    Intent.putExtra("book_price",  tvPrice.getText());

                    getActivity().startActivity(Intent);
                }
                else {
                    Toast.makeText(getContext(), "본인 책이에요! 돌아가요~ 호이호이", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //DB
        String user_id = SaveSharedPreference.getUserID(getContext());
        GetRegistBookData task = new GetRegistBookData();
        task.execute("https://" + IP_ADDRESS + "/get-book-info.php", book_register_id, user_id);

    }

    @Override
    public void onBack() {
        Log.i("Back", "onBack() in BookSellDetailFragment");

        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.setonBackPressedListener(null);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        switch (from_fragment) {
            case "Search":
                SearchFragment searchFragment = SearchFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, searchFragment)
                        .commit();

                MainActivity.activeFragment = searchFragment;
                break;
            case "BookMark" :
                BookMarkFragment bookMarkFragment = BookMarkFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, bookMarkFragment)
                        .commit();

                MainActivity.activeFragment = bookMarkFragment;
                break;
            case "Sell" :
                TransactionListFragment transactionListFragment1 = TransactionListFragment.newInstance();
                SellListFragment sellListFragment = SellListFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, sellListFragment)
                        .commit();

                MainActivity.activeFragment = transactionListFragment1;
                break;
            case "Buy" :
                TransactionListFragment transactionListFragment2 = TransactionListFragment.newInstance();
                BuyListFragment buyListFragment = BuyListFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, buyListFragment)
                        .commit();

                MainActivity.activeFragment = transactionListFragment2;
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("Back", "onAttach()");

        ((MainActivity)context).setonBackPressedListener(this);

    }

    private class GetRegistBookData extends AsyncTask<String, Void, String> {

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
            String book_register_id = strings[1];
            String user_id = strings[2];
            String postParameters = "register_id=" + book_register_id + "& user_id=" + user_id;
            Log.i(TAG, "postParameters : " + postParameters);

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
                Log.i(TAG, sb.toString().trim());

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "GetRegisterData - Error :", e);
                errorString = e.toString();
            }
            return null;
        }

        private void showResult() {
            String TAG_SUCCESS = "success";
            String TAG_BUY_AVAIL = "buy_avail";
            String TAG_BOOK_MARK = "bookmark";

            String TAG_BOOK_NAME = "book_name";
            String TAG_AUTHOR = "author";
            String TAG_PUBLISHER = "publisher";
            String TAG_PUBLISH_DATE = "publish_date";
            String TAG_ORIGINAL_PRICE = "original_price";
            String TAG_SELLING_PRICE ="selling_price";

            String TAG_HIGHLIGHT = "underline";
            String TAG_WRITING = "writing";
            String TAG_COVER = "cover";
            String TAG_DAMAGE = "damage_page";

            String TAG_MEMO = "memo";
            String TAG_SELLER_ID = "seller_id";

            String TAG_SCHOOL = "school";
            String TAG_BOOK_IMAGE = "book_photo";

            //String TAG_RATING = "";

            boolean success;

            try {
                JSONObject jsonObject = new JSONObject(userJsonString);

                success = jsonObject.getBoolean(TAG_SUCCESS);
                Log.i(TAG, "success : " + success);
                if(success){
                    tvBookName.setText(jsonObject.getString(TAG_BOOK_NAME));
                    tvAuthor.setText(jsonObject.getString(TAG_AUTHOR));
                    tvPublisher.setText(jsonObject.getString(TAG_PUBLISHER));
                    tvPublishDate.setText(jsonObject.getString(TAG_PUBLISH_DATE));
                    tvOriginalPrice.setText(jsonObject.getString(TAG_ORIGINAL_PRICE));
                    tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    tvPrice.setText(jsonObject.getString(TAG_SELLING_PRICE));
                    memo.setText(jsonObject.getString(TAG_MEMO));

                    tvLocation.setText(jsonObject.getString(TAG_SCHOOL));

                    //이미지

                    String image_url = jsonObject.getString(TAG_BOOK_IMAGE);
                    Log.i(TAG, "my: "+image_url);
                    String[] split_image = image_url.split(",");
                    Log.i(TAG, "my: "+split_image[0]);
                    Log.i(TAG, "my: "+split_image[1]);
                    ImageView bookImage;
                    for(int i = 0; i<split_image.length;i++){
                        bookImage = new ImageView(getContext());
//                        bookImage.setLayoutParams(lp);
//                        bookImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        linearLayout_img.addView(bookImage);
                        Glide.with(getContext()).load(split_image[i]).into(bookImage);
                    }




                    // 북마크
                    checked = jsonObject.getBoolean(TAG_BOOK_MARK);
                    if (checked)        // 북마크 o
                        ibBookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    else                // 북마크 x
                        ibBookmark.setImageResource(R.drawable.ic_bookmark);

                    seller_id = jsonObject.getString(TAG_SELLER_ID);

                    // 구매 완료 책 구분
                    String buy_avail = jsonObject.getString(TAG_BUY_AVAIL);
                    if (buy_avail.equals("0")) {
                        btnBuy.setEnabled(false);
                        btnBuy.setText("구매 완료");
                    }
                    else if (buy_avail.equals("1")) {
                        btnBuy.setEnabled(true);
                        btnBuy.setText("구매하기");
                    }


                    switch (jsonObject.getInt(TAG_HIGHLIGHT)){
                        case 0:
                            tv_book_state_highlight1_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_highlight2_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_highlight1_x.setTypeface(null,Typeface.BOLD);
                            tv_book_state_highlight2_x.setTypeface(null,Typeface.BOLD);
                            break;

                        case 1:

                            tv_book_state_highlight1_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_highlight2_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_highlight1_o.setTypeface(null,Typeface.BOLD);
                            tv_book_state_highlight2_x.setTypeface(null,Typeface.BOLD);
                            break;
                        case 2:

                            tv_book_state_highlight1_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_highlight2_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_highlight1_x.setTypeface(null,Typeface.BOLD);
                            tv_book_state_highlight2_o.setTypeface(null,Typeface.BOLD);
                            break;
                        default:

                            tv_book_state_highlight1_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_highlight2_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_highlight1_o.setTypeface(null,Typeface.BOLD);
                            tv_book_state_highlight2_o.setTypeface(null,Typeface.BOLD);
                            break;
                    }
                    switch (jsonObject.getInt(TAG_WRITING)){
                        case 0:
                            tv_book_state_writing1_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_writing2_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_writing1_x.setTypeface(null,Typeface.BOLD);
                            tv_book_state_writing2_x.setTypeface(null,Typeface.BOLD);
                            break;
                        case 1:
                            tv_book_state_writing1_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_writing2_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_writing1_o.setTypeface(null,Typeface.BOLD);
                            tv_book_state_writing2_x.setTypeface(null,Typeface.BOLD);
                            break;
                        case 2:

                            tv_book_state_writing1_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_writing2_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_writing1_x.setTypeface(null,Typeface.BOLD);
                            tv_book_state_writing2_o.setTypeface(null,Typeface.BOLD);
                            break;
                        default:
                            tv_book_state_writing1_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_writing2_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_writing1_o.setTypeface(null,Typeface.BOLD);
                            tv_book_state_writing2_o.setTypeface(null,Typeface.BOLD);
                            break;
                    }
                    switch (jsonObject.getInt(TAG_COVER)){
                        case 0:
                            tv_book_state_clean_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_name_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_clean_x.setTypeface(null,Typeface.BOLD);
                            tv_book_state_name_x.setTypeface(null,Typeface.BOLD);
                            break;
                        case 1:
                            tv_book_state_clean_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_name_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_clean_o.setTypeface(null,Typeface.BOLD);
                            tv_book_state_name_x.setTypeface(null,Typeface.BOLD);
                            break;
                        case 2:

                            tv_book_state_clean_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_name_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_clean_x.setTypeface(null,Typeface.BOLD);
                            tv_book_state_name_o.setTypeface(null,Typeface.BOLD);
                            break;
                        default:
                            tv_book_state_clean_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_name_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_clean_o.setTypeface(null,Typeface.BOLD);
                            tv_book_state_name_o.setTypeface(null,Typeface.BOLD);
                            break;
                    }
                    switch (jsonObject.getInt(TAG_DAMAGE)){
                        case 0:
                            tv_book_state_damage1_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_damage2_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_damage1_x.setTypeface(null,Typeface.BOLD);
                            tv_book_state_damage2_x.setTypeface(null,Typeface.BOLD);

                            break;
                        case 1:
                            tv_book_state_damage1_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_damage2_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_damage1_o.setTypeface(null,Typeface.BOLD);
                            tv_book_state_damage2_x.setTypeface(null,Typeface.BOLD);
                            break;
                        case 2:
                            tv_book_state_damage1_x.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_damage2_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_damage1_x.setTypeface(null,Typeface.BOLD);
                            tv_book_state_damage2_o.setTypeface(null,Typeface.BOLD);
                            break;
                        default:
                            tv_book_state_damage1_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_damage2_o.setTextColor(Color.parseColor("#AFD982"));
                            tv_book_state_damage1_o.setTypeface(null,Typeface.BOLD);
                            tv_book_state_damage2_o.setTypeface(null,Typeface.BOLD);
                            break;
                    }
                }
                else {
                    Log.i(TAG, "falseeeeeeeeeeeee");
                }
            } catch (JSONException e) {
                Log.i(TAG, "showResult : ", e);
            }
        }

    }

}