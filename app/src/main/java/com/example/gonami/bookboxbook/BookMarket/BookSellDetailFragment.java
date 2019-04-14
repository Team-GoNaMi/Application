package com.example.gonami.bookboxbook.BookMarket;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class BookSellDetailFragment extends Fragment implements MainActivity.OnBackPressedListener  {

    private View thisView = null;

    private String book_register_id;

    private SearchFragment searchFragment;


    private TextView tvBookName;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvPublishDate;
    private TextView tvOriginalPrice;
    private TextView tvPrice;
    private ImageButton ibBookmark;
    private Button btnBuy;

    //highlight해야될것들
    private TextView tv_book_state_highlighto;
    private TextView tv_book_state_highlightx;
    private TextView tv_book_state_writingso;
    private TextView tv_book_state_writingsx;
    private TextView tv_book_state_damagedo;
    private TextView tv_book_state_damagedx;
    private TextView tv_book_state_covero;
    private TextView tv_book_state_coverx;

    private LinearLayout linearLayout_img;
    private ArrayList<String> bookImage;

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
        }

        searchFragment = SearchFragment.newInstance();

        return thisView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBookName = thisView.findViewById(R.id.tv_book_name);
        tvAuthor = thisView.findViewById(R.id.tv_Author);
        tvPublisher = thisView.findViewById(R.id.tv_book_publisher);
        tvPublishDate = thisView.findViewById(R.id.tv_book_publish_date);
        tvOriginalPrice = thisView.findViewById(R.id.tv_original_price);
        tvPrice = thisView.findViewById(R.id.tv_price);
        ibBookmark = thisView.findViewById(R.id.ib_bookmark);
        btnBuy = thisView.findViewById(R.id.btn_buy);
        tv_book_state_highlighto = thisView.findViewById(R.id.tv_book_state_highlighto);
        tv_book_state_highlightx = thisView.findViewById(R.id.tv_book_state_highlightx);
        tv_book_state_writingso = thisView.findViewById(R.id.tv_book_state_writingso);
        tv_book_state_writingsx = thisView.findViewById(R.id.tv_book_state_writingsx);
        tv_book_state_damagedo = thisView.findViewById(R.id.tv_book_state_damagedo);
        tv_book_state_damagedx = thisView.findViewById(R.id.tv_book_state_damagedx);
        tv_book_state_covero = thisView.findViewById(R.id.tv_book_state_covero);
        tv_book_state_coverx = thisView.findViewById(R.id.tv_book_state_coverx);

        linearLayout_img = thisView.findViewById(R.id.linearLayout_img);
        memo = thisView.findViewById(R.id.tv_book_state_memo);
        rating = thisView.findViewById(R.id.tv_rating);

        tvBookName.setText(book_register_id);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Intent = new Intent(getActivity(), BuyActivity.class);
                Intent.putExtra("book_regist_id", book_register_id);
                getActivity().startActivity(Intent);
            }
        });
    }


    @Override
    public void onBack() {
        Log.i("Back", "onBack() in BookSellDetailFragment");

        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.setonBackPressedListener(null);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, searchFragment)
                .commit();

        MainActivity.activeFragment = searchFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("Back", "onAttach()");
        ((MainActivity)context).setonBackPressedListener(this);
    }
}

