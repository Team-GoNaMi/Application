package com.example.gonami.bookboxbook.DataModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class SaveSharedPreference {

    private static String TAG = "SaveSharedPreference";

    static final String PREF_AUTO_LOGIN = "autoLogin";
    static final String PREF_USER_ID = "userID";
    static final String PREF_USER_PW = "userPW";
    static final String PREF_NAME = "name";
    static final String PREF_PN = "phonenum";


    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // 계정 정보 저장
    public static void setUserID(Context context, Boolean autologin, String userID, String userPW, String name, String pn) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(PREF_AUTO_LOGIN, autologin);
        editor.putString(PREF_USER_ID, userID);
        editor.putString(PREF_USER_PW, userPW);
        editor.putString(PREF_NAME, name);
        editor.putString(PREF_PN, pn);
        editor.commit();
    }

    public static void updatePW(Context context, String userPW) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(PREF_USER_PW);
        editor.commit();
        editor.putString(PREF_USER_PW, userPW);
        editor.commit();

    }

    public static void updatePN(Context context, String userPN) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(PREF_PN);
        editor.commit();
        editor.putString(PREF_PN, userPN);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static Boolean getAutoLogin(Context context) {
        Log.i(TAG, "Auto login");
        return getSharedPreferences(context).getBoolean(PREF_AUTO_LOGIN, false);
    }

    public static String getUserID(Context context) {
        Log.i(TAG, "Get ID");
        return getSharedPreferences(context).getString(PREF_USER_ID, "");
    }

    public static String getUserPW(Context context) {
        Log.i(TAG, "Get PW");
        return getSharedPreferences(context).getString(PREF_USER_PW, "");
    }

    public static String getUserName(Context context) {
        Log.i(TAG, "Get Name");
        return getSharedPreferences(context).getString(PREF_NAME, "");
    }

    public static String getUserPN(Context context) {
        Log.i(TAG, "Get Phonenum");
        return getSharedPreferences(context).getString(PREF_PN, "");
    }

    public static void logout(Context context) {
        Log.i(TAG, "Log out");
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}
