package com.philipstudio.pizzaplan.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class GioHang implements Serializable {
    private String id;
    private MonAn monAn;
    private int soluong;

    public GioHang() {
    }

    public GioHang(String id, MonAn monAn, int soluong) {
        this.id = id;
        this.monAn = monAn;
        this.soluong = soluong;
    }

    public String getId() {
        return id;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public int getSoluong() {
        return soluong;
    }
}
