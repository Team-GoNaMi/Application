package com.example.gonami.bookboxbook;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.gonami.bookboxbook.AddBook.AddActivity;
import com.example.gonami.bookboxbook.BookMarket.BookMarkFragment;
import com.example.gonami.bookboxbook.BookMarket.BookSellFragment;
import com.example.gonami.bookboxbook.BookMarket.SearchFragment;
import com.example.gonami.bookboxbook.MyPage.MyPageFragment;
import com.example.gonami.bookboxbook.TransactionList.TransactionListFragment;

import static android.support.design.widget.BottomNavigationView.*;

public class MainActivity extends AppCompatActivity {

    private SearchFragment searchFragment;
    private BookMarkFragment bookMarkFragment;
    private AddActivity addActivity;
    private TransactionListFragment transactionListFragment;
    private MyPageFragment myPageFragment;

    private BookSellFragment bookSellFragment;

    private BottomNavigationView bottomNavigationView;
    private Fragment activeFragment;
    private FrameLayout frameLayout;

    public FragmentManager fragmentManager;

    private long backKeyPressedTime;    // 앱 종료 위한 백 버튼 누른 시간

    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener(){

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            if ((menuItem.getItemId() == R.id.navigation_search && activeFragment == searchFragment) ||
                    (menuItem.getItemId() == R.id.navigation_bookmark && activeFragment == bookMarkFragment) ||
//                    (menuItem.getItemId() == R.id.navigation_add && activeFragment == addFragment) ||
                    (menuItem.getItemId() == R.id.navigation_list && activeFragment == transactionListFragment) ||
                    (menuItem.getItemId() == R.id.navigation_mypage && activeFragment == myPageFragment)) {
                return false;
            }

            switch (menuItem.getItemId()) {
                case R.id.navigation_search:
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_layout, searchFragment, "Search")
                            .commit();
                    activeFragment = searchFragment;
                    return true;
                case R.id.navigation_bookmark:
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_layout, bookMarkFragment, "BookMark")
                            .commit();
                    activeFragment = bookMarkFragment;
                    return true;
                case R.id.navigation_add:
//                    fragmentManager.beginTransaction()
//                            .addToBackStack(null)
//                            .replace(R.id.frame_layout, addFragment, "Add")
//                            .commit();
//                    activeFragment = addFragment;
                    Intent intent=new Intent(MainActivity.this, AddActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_list:
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_layout, transactionListFragment, "TransactionList")
                            .commit();
                    activeFragment = transactionListFragment;
                    return true;
                case R.id.navigation_mypage:
                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_layout, myPageFragment, "MyPage")
                            .commit();
                    activeFragment = myPageFragment;
                    return true;
            }
            bottomNavigationView.setSelectedItemId(menuItem.getItemId());
            return false;
        }
    };

//    private boolean loadFragment(Fragment fragment) {
//        if (fragment != null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.frame_layout, fragment)
//                    .commit();
//        }
//        return false;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent userInfoIntent = new Intent(this.getIntent());
        String user_id = userInfoIntent.getStringExtra("id");

        searchFragment = SearchFragment.newInstance();
        bookMarkFragment = BookMarkFragment.newInstance();
        transactionListFragment = TransactionListFragment.newInstance();
        myPageFragment = MyPageFragment.newInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();

        setFragmentManager();

    }

    private void setFragmentManager() {
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame_layout, searchFragment, "Search")
                .commit();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        activeFragment = searchFragment;
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        }
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
//            super.onBackPressed();
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            this.finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

}
