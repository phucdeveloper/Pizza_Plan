package com.philipstudio.pizzaplan.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

public class VitriUtils {
    private SharedPreferences preferences;

    public VitriUtils(Context context) {
        preferences = context.getSharedPreferences("vitri", Context.MODE_PRIVATE);
    }

    public void setVitriNguoiDung(double lat, double lng){
        SharedPreferences.Editor editor = preferences.edit();
        String vido = String.valueOf(lat);
        String kinhdo = String.valueOf(lng);
        editor.putString("vido", vido);
        editor.putString("kinhdo", kinhdo);
        editor.apply();
    }

    public LatLng getVitriNguoiDung(){
        double lat = 0;
        double lng = 0;
        String vido = preferences.getString("vido", null);
        String kinhdo = preferences.getString("kinhdo", null);

        if (vido != null && kinhdo != null){
            lat = Double.parseDouble(vido);
            lng = Double.parseDouble(kinhdo);
        }
        return new LatLng(lat, lng);
    }
}
