package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ticker implements Serializable {
//    private int id;
//    private String tenrap;
//    private String ngaydat;
//    private String tenphong;
//    private String tenphim;
//    private String tenghe;
//    private String gio;
//
//    private String diachirap;
//    private String hinhphim;
//    private String thoigianphim;
//
//    public String getDiachirap() {
//        return diachirap;
//    }
//
//    public void setDiachirap(String diachirap) {
//        this.diachirap = diachirap;
//    }
//
//    public String getHinhphim() {
//        return hinhphim;
//    }
//
//    public void setHinhphim(String hinhphim) {
//        this.hinhphim = hinhphim;
//    }
//
//    public String getThoigianphim() {
//        return thoigianphim;
//    }
//
//    public void setThoigianphim(String thoigianphim) {
//        this.thoigianphim = thoigianphim;
//    }
//
//    public Ticker() {
//    }
//
//    public Ticker(int id, String tenrap, String ngaydat, String tenphim, String tenghe, String gio) {
//        this.id = id;
//        this.tenrap = tenrap;
//        this.ngaydat = ngaydat;
//        this.tenphim = tenphim;
//        this.tenghe = tenghe;
//        this.gio = gio;
//    }
//
//    public String getTenphong() {
//        return tenphong;
//    }
//
//    public void setTenphong(String tenphong) {
//        this.tenphong = tenphong;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getTenrap() {
//        return tenrap;
//    }
//
//    public void setTenrap(String tenrap) {
//        this.tenrap = tenrap;
//    }
//
//    public String getNgaydat() {
//        return ngaydat;
//    }
//
//    public void setNgaydat(String ngaydat) {
//        this.ngaydat = ngaydat;
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
//    public String getTenghe() {
//        return tenghe;
//    }
//
//    public void setTenghe(String tenghe) {
//        this.tenghe = tenghe;
//    }
//
//    public String getGio() {
//        return gio;
//    }
//
//    public void setGio(String gio) {
//        this.gio = gio;
//    }
@SerializedName("TenPhong")
@Expose
private String tenphong;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("DiaChi")
    @Expose
    private String diachirap;
    @SerializedName("Hinh")
    @Expose
    private String hinhphim;
    @SerializedName("ThoiGian")
    @Expose
    private String thoigianphim;
    @SerializedName("ID")
    @Expose
    private int id;
    @SerializedName("TenRap")
    @Expose
    private String tenrap;
    @SerializedName("TenPhim")
    @Expose
    private String tenphim;
    @SerializedName("NgayDat")
    @Expose
    private String ngaydat;
    @SerializedName("TenGhe")
    @Expose
    private String tenghe;
    @SerializedName("Gio")
    @Expose
    private String gio;

    public String getTenphong() {
        return tenphong;
    }

    public Ticker setTenphong(String tenphong) {
        this.tenphong = tenphong;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Ticker setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getDiachirap() {
        return diachirap;
    }

    public Ticker setDiachirap(String diachirap) {
        this.diachirap = diachirap;
        return this;
    }

    public String getHinhphim() {
        return hinhphim;
    }

    public Ticker setHinhphim(String hinhphim) {
        this.hinhphim = hinhphim;
        return this;
    }

    public String getThoigianphim() {
        return thoigianphim;
    }

    public Ticker setThoigianphim(String thoigianphim) {
        this.thoigianphim = thoigianphim;
        return this;
    }

    public int getId() {
        return id;
    }

    public Ticker setId(int id) {
        this.id = id;
        return this;
    }

    public String getTenrap() {
        return tenrap;
    }

    public Ticker setTenrap(String tenrap) {
        this.tenrap = tenrap;
        return this;
    }

    public String getTenphim() {
        return tenphim;
    }

    public Ticker setTenphim(String tenphim) {
        this.tenphim = tenphim;
        return this;
    }

    public String getNgaydat() {
        return ngaydat;
    }

    public Ticker setNgaydat(String ngaydat) {
        this.ngaydat = ngaydat;
        return this;
    }

    public String getTenghe() {
        return tenghe;
    }

    public Ticker setTenghe(String tenghe) {
        this.tenghe = tenghe;
        return this;
    }

    public String getGio() {
        return gio;
    }

    public Ticker setGio(String gio) {
        this.gio = gio;
        return this;
    }
}
