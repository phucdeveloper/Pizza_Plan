package com.philipstudio.pizzaplan.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DonHang {
    private String idDonHang;
    private String idNguoiDung;
    private String thoigian;
    private String diadiem;
    private double tongtien;
    private String trangthai;

    public DonHang(String idDonHang, String idNguoiDung, String thoigian, String diadiem, double tongtien, String trangthai) {
        this.idDonHang = idDonHang;
        this.idNguoiDung = idNguoiDung;
        this.thoigian = thoigian;
        this.diadiem = diadiem;
        this.tongtien = tongtien;
        this.trangthai = trangthai;
    }

    public String getIdDonHang() {
        return idDonHang;
    }

    public String getIdNguoiDung() {
        return idNguoiDung;
    }

    public String getThoigian() {
        return thoigian;
    }

    public String getDiadiem() {
        return diadiem;
    }

    public double getTongtien() {
        return tongtien;
    }

    public String getTrangthai() {
        return trangthai;
    }
}