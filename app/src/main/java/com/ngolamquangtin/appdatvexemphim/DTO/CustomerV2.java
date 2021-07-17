package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerV2 implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("hoTen")
    @Expose
    private String hoTen;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("ngaySinh")
    @Expose
    private String ngaySinh;
    @SerializedName("sdt")
    @Expose
    private String sdt;
    @SerializedName("taiKhoan")
    @Expose
    private String taiKhoan;
    @SerializedName("matKhau")
    @Expose
    private String matKhau;
    @SerializedName("ngayDangKy")
    @Expose
    private String ngayDangKy;
    @SerializedName("anhDaiDien")
    @Expose
    private String anhDaiDien;

    public Integer getId() {
        return id;
    }

    public CustomerV2 setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getHoTen() {
        return hoTen;
    }

    public CustomerV2 setHoTen(String hoTen) {
        this.hoTen = hoTen;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CustomerV2 setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public CustomerV2 setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
        return this;
    }

    public String getSdt() {
        return sdt;
    }

    public CustomerV2 setSdt(String sdt) {
        this.sdt = sdt;
        return this;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public CustomerV2 setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
        return this;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public CustomerV2 setMatKhau(String matKhau) {
        this.matKhau = matKhau;
        return this;
    }

    public String getNgayDangKy() {
        return ngayDangKy;
    }

    public CustomerV2 setNgayDangKy(String ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
        return this;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public CustomerV2 setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
        return this;
    }
}
