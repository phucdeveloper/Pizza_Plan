package com.philipstudio.pizzaplan.model;

import java.io.Serializable;
import java.util.ArrayList;

public class TheLoaiMonAn implements Serializable {
    private String tenTheLoai;
    private ArrayList<MonAn> monAnArrayList;
    private int viewType;

    public TheLoaiMonAn(String tenTheLoai, ArrayList<MonAn> monAnArrayList, int viewType) {
        this.tenTheLoai = tenTheLoai;
        this.monAnArrayList = monAnArrayList;
        this.viewType = viewType;
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    public ArrayList<MonAn> getMonAnArrayList() {
        return monAnArrayList;
    }

    public void setMonAnArrayList(ArrayList<MonAn> monAnArrayList) {
        this.monAnArrayList = monAnArrayList;
    }

    public int getViewType() {
        return viewType;
    }
}
