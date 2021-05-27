package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieFavourite {
    //    private  int id;
//    private String tenphim;
//    private int thoigian;
//    private String hinh;
//    private String mota;
//
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public MovieFavourite(String tenphim, int thoigian, String hinh, String mota) {
//        this.tenphim = tenphim;
//        this.thoigian = thoigian;
//        this.hinh = hinh;
//        this.mota = mota;
//    }
//
//    public MovieFavourite() {
//    }
//
//    public String getTenphim() {
//        return tenphim;
//    }
//
//    public void setTenphim(String tenphim) {
//        this.tenphim = tenphim;
//    }
//
//    public int getThoigian() {
//        return thoigian;
//    }
//
//    public void setThoigian(int thoigian) {
//        this.thoigian = thoigian;
//    }
//
//    public String getHinh() {
//        return hinh;
//    }
//
//    public void setHinh(String hinh) {
//        this.hinh = hinh;
//    }
//
//    public String getMota() {
//        return mota;
//    }
//
//    public void setMota(String mota) {
//        this.mota = mota;
//    }
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("TenPhim")
    @Expose
    private String tenphim;
    @SerializedName("ThoiGian")
    @Expose
    private Integer thoigian;
    @SerializedName("Hinh")
    @Expose
    private String hinh;
    @SerializedName("MoTa")
    @Expose
    private String mota;

    public Integer getId() {
        return id;
    }

    public MovieFavourite setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTenphim() {
        return tenphim;
    }

    public MovieFavourite setTenphim(String tenphim) {
        this.tenphim = tenphim;
        return this;
    }

    public Integer getThoigian() {
        return thoigian;
    }

    public MovieFavourite setThoigian(Integer thoigian) {
        this.thoigian = thoigian;
        return this;
    }

    public String getHinh() {
        return hinh;
    }

    public MovieFavourite setHinh(String hinh) {
        this.hinh = hinh;
        return this;
    }

    public String getMota() {
        return mota;
    }

    public MovieFavourite setMota(String mota) {
        this.mota = mota;
        return this;
    }
}
