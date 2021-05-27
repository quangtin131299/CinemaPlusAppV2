package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class XuatChieu {

    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("Ngay")
    @Expose
    private String ngay;

    @SerializedName("Gio")
    @Expose
    private String thoigian;

    public XuatChieu() {
    }

    public XuatChieu(Integer id, String ngay, String thoigian) {
        this.id = id;
        this.ngay = ngay;
        this.thoigian = thoigian;
    }

    public int getId() {
        return id;
    }

    public XuatChieu setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNgay() {
        return ngay;
    }

    public XuatChieu setNgay(String ngay) {
        this.ngay = ngay;
        return this;
    }

    public String getThoigian() {
        return thoigian;
    }

    public XuatChieu setThoigian(String thoigian) {
        this.thoigian = thoigian;
        return this;
    }
}
