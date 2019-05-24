package com.example.gonami.bookboxbook.MyPage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.example.gonami.bookboxbook.DataModel.SaveSharedPreference;
import com.example.gonami.bookboxbook.Login.LoginActivity;
import com.example.gonami.bookboxbook.MainActivity;
import com.example.gonami.bookboxbook.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class MyPageListViewAdapter extends BaseAdapter {

    private static String IP_ADDRESS = "bookboxbook.duckdns.org";
    private static String TAG = "MyPageAdapter";

    private ArrayList<String> menu = new ArrayList<String>();

    public MyPageListViewAdapter(ArrayList<String> menu) {
        this.menu = menu;
    }

    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public Object getItem(int position) {
        return menu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        LayoutInflater inflater = null;

        if (convertView == null) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mypage_menu, parent, false);

            if(position == 0){
                convertView = inflater.inflate(R.layout.alarm_button, parent, false);
                ToggleButton toggleButton = convertView.findViewById(R.id.toggleButton);
            }
        }
        if (position != 0) {
            TextView tvMenuName = convertView.findViewById(R.id.tv_menu_name);
            tvMenuName.setText(menu.get(position));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, menu.get(position), Toast.LENGTH_SHORT).show();
                if (position == 1){         // 회원 수정
                    ChangePersonalInfoFragment changePersonalInfoFragment = new ChangePersonalInfoFragment();
                    Bundle bundle = new Bundle();
//                    bundle.putString("BookRegisterID", bookInfo.getRegister_id());
//                    changePersonalInfoFragment = ChangePersonalInfoFragment.newInstance(bundle);

                    FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, changePersonalInfoFragment)
                            .commit();

                    MainActivity.activeFragment = changePersonalInfoFragment;

                }else if (position == 2) {      // 로그 아웃
                    DialogInterface.OnClickListener logoutListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SaveSharedPreference.logout(context);
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                    };
                    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    };
                    new AlertDialog.Builder(context)
                            .setTitle("로그아웃 하시겠습니까?")
                            .setNegativeButton("확인", logoutListener)
                            .setPositiveButton("취소", cancelListener)
                            .show();

                }else if(position==3){          // 회원 탈퇴
                    final String member_id = SaveSharedPreference.getUserID(context);
                    DialogInterface.OnClickListener dropListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DeleteMember task = new DeleteMember();
                            Log.i(TAG, "id:" + member_id);
                            Log.i(TAG, ">>>>" + SaveSharedPreference.getUserName(context));
                            task.execute("https://" + IP_ADDRESS + "/delete-member.php", member_id);

                            SaveSharedPreference.logout(context);
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                    };
                    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    };
                    new AlertDialog.Builder(context)
                            .setTitle("탈퇴 하시겠습니까?")
                            .setNegativeButton("확인", dropListener)
                            .setPositiveButton("취소", cancelListener)
                            .show();
                }

                SaveSharedPreference.logout(context);

            }
        });

        return convertView;
    }
    private class DeleteMember extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "POST response1  - " + result);

//            SaveSharedPreference.logout();
//            Intent intent = new Intent(context, LoginActivity.class);
//            context.startActivity(intent);
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverURL = (String)strings[0];
            String postParameters = "member_id=" + strings[1];

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.i(TAG, "POST response code2 - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    Log.i(TAG, "OKAY");
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                    Log.i(TAG, String.valueOf(responseStatusCode));

                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                Log.i(TAG, "////"+sb.toString());
                return sb.toString();

            } catch (Exception e) {
                Log.i(TAG, "DeleteData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }
}
