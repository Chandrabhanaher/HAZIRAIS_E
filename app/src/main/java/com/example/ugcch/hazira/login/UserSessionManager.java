package com.example.ugcch.hazira.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class UserSessionManager {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "receiptApp";
    private static final String IS_USER_LOGIN = "loggedIn";
    public static final String KEY_NAME = "user_code";

    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String name){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public boolean checkLogin(){
        if(!this.isUserLoggedIn()){

            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            return true;
        }
        return false;
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        return user;
    }

    public void logoutUser(){
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.remove("user_code");
        editor.clear();
        editor.apply();
        editor.commit();
        Intent i = new Intent(_context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
