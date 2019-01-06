package com.example.zhanyuan.finalapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * define the class to manager the sharePreference file
 */
public class SharedPrefenrenceHelper {

    private Context mContext;
    SharedPreferences sp;

    public SharedPrefenrenceHelper(Context mContext) {
        this.mContext = mContext;
         sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
    }

    /**
     *  define method to save the date using key-value pair
     * @param key
     * @param value
     */
    public void save(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * define method for read the value using key
     * @param key
     * @return
     */
    public String read(String key){
        String data;
        data = sp.getString(key, "0");
        return data;
    }




}
