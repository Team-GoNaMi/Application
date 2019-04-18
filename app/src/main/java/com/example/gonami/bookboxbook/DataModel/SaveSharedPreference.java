package com.example.gonami.bookboxbook.DataModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static final String PREF_AUTO_LOGIN = "autoLogin";
    static final String PREF_USER_ID = "userID";
    static final String PREF_USER_PW = "userPW";
    static final String PREF_NAME = "name";


    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // 계정 정보 저장
    public static void setUserID(Context context, Boolean autologin, String userID, String userPW, String name) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(PREF_AUTO_LOGIN, autologin);
        editor.putString(PREF_USER_ID, userID);
        editor.putString(PREF_USER_PW, userPW);
        editor.putString(PREF_NAME, name);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static Boolean getAutoLogin(Context context) {
        return getSharedPreferences(context).getBoolean(PREF_AUTO_LOGIN, false);
    }

    public static String getUserID(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_ID, "");
    }

    public static String getUserPW(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_PW, "");
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(PREF_NAME, "");
    }

    public static void logout(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}
