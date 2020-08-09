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

    public void setAccountUser(String id, String email, String anhdaidien, String matkhau, String tennguoidung,
                          String diachi, String ngaysinh, String gioitinh){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("idNguoiDung", id);
        editor.putString("emailOrPhonenumber", email);
        editor.putString("anh", anhdaidien);
        editor.putString("matkhau", matkhau);
        editor.putString("tennguoidung", tennguoidung);
        editor.putString("diachi", diachi);
        editor.putString("ngaysinh", ngaysinh);
        editor.putString("gioitinh", gioitinh);
        editor.apply();
    }

    public String getAccountUser(){
        String id = preferences.getString("idNguoiDung", null);
        return id;
    }
}
