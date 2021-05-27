package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tenPhim")
    @Expose
    private String tenphim;
    @SerializedName("hinh")
    @Expose
    private String hinh;
    @SerializedName("anhBia")
    @Expose
    private String anhbia;
    @SerializedName("trangThai")
    @Expose
    private String status;
    @SerializedName("thoiGian")
    @Expose
    private Integer thoigian;
    @SerializedName("trailer")
    @Expose
    private String trailer;
    @SerializedName("moTa")
    @Expose
    private String mota;
    @SerializedName("ngayKhoiChieu")
    @Expose
    private String ngayKhoiChieu;
    @SerializedName("loaiphims")
    @Expose
    private List<MovieType> loaiphims = null;

    public Integer getId() {
        return id;
    }

    public Movie setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTenphim() {
        return tenphim;
    }

    public Movie setTenphim(String tenphim) {
        this.tenphim = tenphim;
        return this;
    }

    public Integer getThoigian() {
        return thoigian;
    }

    public Movie setThoigian(Integer thoigian) {
        this.thoigian = thoigian;
        return this;
    }

    public String getHinh() {
        return hinh;
    }

    public Movie setHinh(String hinh) {
        this.hinh = hinh;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Movie setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getAnhbia() {
        return anhbia;
    }

    public Movie setAnhbia(String anhbia) {
        this.anhbia = anhbia;
        return this;
    }

    public String getTrailer() {
        return trailer;
    }

    public Movie setTrailer(String trailer) {
        this.trailer = trailer;
        return this;
    }

    public String getMota() {
        return mota;
    }

    public Movie setMota(String mota) {
        this.mota = mota;
        return this;
    }

    public String getNgayKhoiChieu() {
        return ngayKhoiChieu;
    }

    public Movie setNgayKhoiChieu(String ngayKhoiChieu) {
        this.ngayKhoiChieu = ngayKhoiChieu;
        return this;
    }

    public List<MovieType> getLoaiphims() {
        return loaiphims;
    }

    public Movie setLoaiphims(List<MovieType> loaiphims) {
        this.loaiphims = loaiphims;
        return this;
    }
}
