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

    public BillV2 setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNgaylaphoadon() {
        return ngaylaphoadon;
    }

    public BillV2 setNgaylaphoadon(String ngaylaphoadon) {
        this.ngaylaphoadon = ngaylaphoadon;
        return this;
    }

    public Integer getThanhtien() {
        return thanhtien;
    }

    public BillV2 setThanhtien(Integer thanhtien) {
        this.thanhtien = thanhtien;
        return this;
    }

    public Integer getIdkhachhang() {
        return idkhachhang;
    }

    public BillV2 setIdkhachhang(Integer idkhachhang) {
        this.idkhachhang = idkhachhang;
        return this;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public BillV2 setTrangthai(String trangthai) {
        this.trangthai = trangthai;
        return this;
    }
}
