package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BillV2 implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ngY")
    @Expose
    private String ngaylaphoadon;
    @SerializedName("thanhTien")
    @Expose
    private Integer thanhtien;
    @SerializedName("idKhachHang")
    @Expose
    private Integer idkhachhang;
    @SerializedName("trangThai")
    @Expose
    private String trangthai;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNgaylaphoadon() {
        return ngaylaphoadon;
    }

    public void setNgaylaphoadon(String ngaylaphoadon) {
        this.ngaylaphoadon = ngaylaphoadon;
    }

    public Integer getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(Integer thanhtien) {
        this.thanhtien = thanhtien;
    }

    public Integer getIdkhachhang() {
        return idkhachhang;
    }

    public void setIdkhachhang(Integer idkhachhang) {
        this.idkhachhang = idkhachhang;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }
}
