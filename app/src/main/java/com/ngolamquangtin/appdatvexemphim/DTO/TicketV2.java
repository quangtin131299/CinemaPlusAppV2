package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TicketV2 implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ngayDat")
    @Expose
    private String ngayDat;
    @SerializedName("idSuat")
    @Expose
    private Integer idSuat;
    @SerializedName("idGhe")
    @Expose
    private Integer idGhe;
    @SerializedName("idPhim")
    @Expose
    private Integer idPhim;
    @SerializedName("idKhachHang")
    @Expose
    private Integer idKhachHang;
    @SerializedName("idRap")
    @Expose
    private Integer idRap;
    @SerializedName("idHoaDon")
    @Expose
    private Integer idHoaDon;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("idPhong")
    @Expose
    private Integer idPhong;
    @SerializedName("idPhong2")
    @Expose
    private RoomV2 phong;
    @SerializedName("idHoaDon2")
    @Expose
    private BillV2 hoadon;
    @SerializedName("idRap2")
    @Expose
    private Cinema rap;
    @SerializedName("idKhachHang2")
    @Expose
    private CustomerV2 khachhang;
    @SerializedName("idSuat2")
    @Expose
    private TimeV2 suatchieu;
    @SerializedName("idPhim2")
    @Expose
    private Movie phim;
    @SerializedName("idGhe2")
    @Expose
    private SeatV2 ghe;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public Integer getIdSuat() {
        return idSuat;
    }

    public void setIdSuat(Integer idSuat) {
        this.idSuat = idSuat;
    }

    public Integer getIdGhe() {
        return idGhe;
    }

    public void setIdGhe(Integer idGhe) {
        this.idGhe = idGhe;
    }

    public Integer getIdPhim() {
        return idPhim;
    }

    public void setIdPhim(Integer idPhim) {
        this.idPhim = idPhim;
    }

    public Integer getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(Integer idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public Integer getIdRap() {
        return idRap;
    }

    public void setIdRap(Integer idRap) {
        this.idRap = idRap;
    }

    public Integer getIdHoaDon() {
        return idHoaDon;
    }

    public void setIdHoaDon(Integer idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIdPhong() {
        return idPhong;
    }

    public void setIdPhong(Integer idPhong) {
        this.idPhong = idPhong;
    }

    public RoomV2 getPhong() {
        return phong;
    }

    public void setPhong(RoomV2 phong) {
        this.phong = phong;
    }

    public BillV2 getHoadon() {
        return hoadon;
    }

    public void setHoadon(BillV2 hoadon) {
        this.hoadon = hoadon;
    }

    public Cinema getRap() {
        return rap;
    }

    public void setRap(Cinema rap) {
        this.rap = rap;
    }

    public CustomerV2 getKhachhang() {
        return khachhang;
    }

    public void setKhachhang(CustomerV2 khachhang) {
        this.khachhang = khachhang;
    }

    public TimeV2 getSuatchieu() {
        return suatchieu;
    }

    public void setSuatchieu(TimeV2 suatchieu) {
        this.suatchieu = suatchieu;
    }

    public Movie getPhim() {
        return phim;
    }

    public void setPhim(Movie phim) {
        this.phim = phim;
    }

    public SeatV2 getGhe() {
        return ghe;
    }

    public void setGhe(SeatV2 ghe) {
        this.ghe = ghe;
    }
}
