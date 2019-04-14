package com.example.gonami.bookboxbook.BookMarket;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;


public class BookSellDetailFragment extends Fragment implements MainActivity.OnBackPressedListener  {

    private View thisView = null;

    private String book_register_id;

    private SearchFragment searchFragment;


    private TextView tvBookName;

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

        tvBookName.setText(book_register_id);
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

