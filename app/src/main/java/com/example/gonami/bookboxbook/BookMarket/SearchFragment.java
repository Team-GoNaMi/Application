package com.example.gonami.bookboxbook.BookMarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private View thisView = null;

    private ListView bookListView;
    private BookSearchListViewAdapter bookSearchListViewAdapter;
    private ArrayList<String> searchList;

    private BookSellActivity bookSellActivity;
    private BookSellFragment bookSellFragment;
//    private OnFragmentInteractionListener mListener;
//    private int detail_request = 515;


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

        searchList = new ArrayList<String>();

        searchList.add("책 검색 페이지");
        searchList.add("맞나용???");

        bookListView = thisView.findViewById(R.id.lv_book_market);
        bookSearchListViewAdapter = new BookSearchListViewAdapter(searchList);
        bookListView.setAdapter(bookSearchListViewAdapter);

        Button testbtn = thisView.findViewById(R.id.btn_test_sell);
        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), BookSellActivity.class);
//                startActivity(intent);

                String book_register_id = searchList.get(0);
                Bundle bundle = new Bundle();
                bundle.putString("BookRegisterID", book_register_id);
                bookSellFragment = BookSellFragment.newInstance(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                fragmentManager.beginTransaction()
                               .replace(R.id.frame_layout, bookSellFragment)
                               .commit();

                MainActivity.activeFragment = bookSellFragment;

            }
        });

    }


}
