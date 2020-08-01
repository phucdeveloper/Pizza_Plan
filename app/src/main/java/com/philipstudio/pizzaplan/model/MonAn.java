package com.philipstudio.pizzaplan.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class MonAn implements Serializable {
    private String idMonAn;
    private String tenMonAn;
    private String anh;
    private String nguyenLieu;
    private double gia;

    public MonAn(){}

    public MonAn(String idMonAn, String tenMonAn, String anh, String nguyenLieu, double gia) {
        this.idMonAn = idMonAn;
        this.tenMonAn = tenMonAn;
        this.anh = anh;
        this.nguyenLieu = nguyenLieu;
        this.gia = gia;
    }

    public String getIdMonAn() {
        return idMonAn;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public String getAnh() {
        return anh;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public String getNguyenLieu() {
        return nguyenLieu;
    }

    public void setNguyenLieu(String nguyenLieu) {
        this.nguyenLieu = nguyenLieu;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }
}
