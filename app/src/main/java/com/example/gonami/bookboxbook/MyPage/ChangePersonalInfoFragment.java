package com.example.gonami.bookboxbook.MyPage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

public class ChangePersonalInfoFragment extends Fragment {
    private EditText ed_user_name;
    private EditText ed_user_id; // 수정불가
    private EditText ed_user_password;
    private EditText ed_user_password_check;
    private EditText ed_user_phone_num;
    private EditText ed_user_school;
    private Button btn_cancel_change_info;
    private Button btn_change_info;

    private MyPageFragment myPageFragment;
    private View thisView = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (thisView == null)
            thisView = inflater.inflate(R.layout.fragment_change_personal_info, null);

        return thisView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ed_user_name = view.findViewById(R.id.ed_user_name);
        ed_user_id = view.findViewById(R.id.ed_user_id);
        ed_user_password = view.findViewById(R.id.ed_user_password);
        ed_user_password_check = view.findViewById(R.id.ed_user_password_check);
        ed_user_phone_num = view.findViewById(R.id.ed_user_phone_num);
        ed_user_school = view.findViewById(R.id.ed_user_school);
        btn_cancel_change_info = view.findViewById(R.id.btn_cancel_change_info);
        btn_change_info = view.findViewById(R.id.btn_change_info);

//TODO 예외처리 or 변경된 것 있는지 비교?
        btn_cancel_change_info.setOnClickListener(new View.OnClickListener() {
            final Context context = thisView.getContext();

            @Override
            public void onClick(View v) {
//                ChangePersonalInfoFragment changePersonalInfoFragment = new ChangePersonalInfoFragment();
//
//                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
//
//                fragmentManager.beginTransaction()
//                        .replace(R.id.frame_layout, changePersonalInfoFragment)
//                        .commit();
//
//                MainActivity.activeFragment = changePersonalInfoFragment;

            }
        });
        ed_user_school.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btn_change_info.performClick();
                    return true;
                }
                return false;
            }
        });

    }


}
