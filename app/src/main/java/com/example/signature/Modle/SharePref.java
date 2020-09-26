package com.example.signature.Modle;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.example.signature.MyApplication;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class SharePref {
    private static final String MY_PREFS = "pathSignature";
    private static final String PATH_PRIVATE = "PathPri";
    private static final String PATH_PUBLIC = "PathPub";
    private static final String PASS_AES = "AESPassword";
    private static final String USE_FINGER = "UseFinger";
    private static final String COUNT_LOGIN = "countLogin";
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    private static void init() {
        if (mSharedPreferences == null)
            mSharedPreferences = MyApplication.getContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        editor = MyApplication.getContext().getSharedPreferences(SharePref.MY_PREFS, MODE_PRIVATE).edit();
        // MOVE_PRIVATE:  Only this app can access that SharePref. Protect data in this SharePref. 
    }

    // save Use finger
    public static void SetUseFinger(Boolean b) {
        init();
        editor.putBoolean(USE_FINGER, b);
        editor.apply();
    }

    // check use finger
    public static boolean AskUseFinger() {
        init();
        return mSharedPreferences.getBoolean(USE_FINGER, false);
    }

    // get Private key path
    public static String PathPri() {
        init();
        return mSharedPreferences.getString(PATH_PRIVATE, "");
    }

    // get Public Key path
    public static String PathPub() {
        init();
        return mSharedPreferences.getString(PATH_PUBLIC, "");
    }

    // save path public and private key
    public static void SaveKey(String pathPrivate, String pathPublic) {
        init();
        editor.putString(PATH_PRIVATE, pathPrivate);
        editor.putString(PATH_PUBLIC, pathPublic);
        editor.apply();
    }

    // check Password when enter app
    public static boolean CheckLogin(String password) {
        init();
        return password.equals(mSharedPreferences.getString(PASS_AES, "")) || Objects.requireNonNull(mSharedPreferences.getString(PASS_AES, "")).isEmpty();

    }

    // check the first time to enter app
    public static boolean CheckPassExsit() {
        init();
        return !Objects.requireNonNull(mSharedPreferences.getString(PASS_AES, "")).isEmpty();
    }

    // save password
    public static void AESPass(String password) {
        init();
        editor.putString(PASS_AES, password);
        editor.apply();
    }

    // select password
    public static String SellectAESPass() {
        init();
        return mSharedPreferences.getString(PASS_AES, "");
    }

    // save time wait for login
    public static void countLogin(long count) {
        init();
        editor.putLong(COUNT_LOGIN, count);
        editor.apply();
    }

    // pick time wait for login
    public static long SellectCountLogin() {
        init();
        return mSharedPreferences.getLong(COUNT_LOGIN, 432);
    }


}