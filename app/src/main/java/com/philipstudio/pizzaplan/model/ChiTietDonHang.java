package com.philipstudio.pizzaplan.model;

import java.util.List;

public class ChiTietDonHang {
    private String idNguoiDung;
    private String idDonHang;
    private List<GioHang> listDanhSachGioHang;

    public ChiTietDonHang(String idNguoiDung, String idDonHang, List<GioHang> listDanhSachGioHang) {
        this.idNguoiDung = idNguoiDung;
        this.idDonHang = idDonHang;
        this.listDanhSachGioHang = listDanhSachGioHang;
    }

    public String getIdNguoiDung() {
        return idNguoiDung;
    }

    public String getIdDonHang() {
        return idDonHang;
    }

    public List<GioHang> getListDanhSachGioHang() {
        return listDanhSachGioHang;
    }
}
