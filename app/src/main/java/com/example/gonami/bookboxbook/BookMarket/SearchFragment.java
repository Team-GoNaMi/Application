package com.example.gonami.bookboxbook.BookMarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gonami.bookboxbook.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private View thisView = null;

    private ListView bookListView;
    private BookSearchListViewAdapter bookSearchListViewAdapter;
    private ArrayList<String> searchList;


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

//        Button testbtn = thisView.findViewById(R.id.btn_test_sell);
//        testbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().beginTransaction()
//                                    .addToBackStack(null)
//                                    .replace(R.)
//                                    .commit();
//            }
//        });


//        searchList = new ArrayList<String>();
//
//        //Test
//        TextView id = thisView.findViewById(R.id.tv_user_id);
//        TextView pw = thisView.findViewById(R.id.tv_user_password);
//
//        id.setText("");
//        pw.setText("");
//
//        bookListView = thisView.findViewById(R.id.lv_book_market);
//
//        etSearchBook = thisView.findViewById(R.id.et_search_book);
//        etSearchBook.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });



//        searchView = thisView.findViewById(R.id.sv_book_search);
//        searchView.setQueryHint("책 이름 검색");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(getContext(), query, Toast.LENGTH_LONG).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getContext(), newText, Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });

    }
}
