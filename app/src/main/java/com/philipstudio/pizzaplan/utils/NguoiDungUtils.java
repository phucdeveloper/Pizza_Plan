package com.philipstudio.pizzaplan.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class NguoiDungUtils {
    private SharedPreferences preferences;

    public NguoiDungUtils(Context context) {
        preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public void setIdUser(String id){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("idNguoiDung", id);
        editor.apply();
    }

    public String getIdUser(){
        String id = preferences.getString("idNguoiDung", null);
        return id;
    }
}
