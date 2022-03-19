package com.example.myapplication;

import java.io.Serializable;

public class NhanVien implements Serializable {
    private int maso;
    private String hoten;
    private String gioitinh;
    private String donvi;
    private byte[] imgView;

    public NhanVien(int maso, String hoten, String gioitinh, String donvi, byte[] imgView) {
        this.maso = maso;
        this.hoten = hoten;
        this.gioitinh = gioitinh;
        this.donvi = donvi;
        this.imgView = imgView;
    }

    public int getMaso() {
        return maso;
    }

    public void setMaso(int maso) {
        this.maso = maso;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }

    public byte[] getImgView() {
        return imgView;
    }

    public void setImgView(byte[] imgView) {
        this.imgView = imgView;
    }

    @Override
    public String toString() {
        return "- Mã số: " + maso +
                "\n- Họ tên: " + hoten +
                "\n- Giới tính: " + gioitinh +
                "\n- Đơn vị: " + donvi;
    }
}
