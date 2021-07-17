package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
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
    private int thoigian;
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
    @SerializedName("suatchieus")
    @Expose
    private ArrayList<TimeV2> suatchieus = null;

    @SerializedName("nameCinema")
    @Expose
    private String nameCinema;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("idShowTime")
    @Expose
    private String idShowTime;
    @SerializedName("idCinema")
    @Expose
    private String idCinema;

    public String getNameCinema() {
        return nameCinema;
    }

    public void setNameCinema(String nameCinema) {
        this.nameCinema = nameCinema;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIdShowTime() {
        return idShowTime;
    }

    public void setIdShowTime(String idShowTime) {
        this.idShowTime = idShowTime;
    }

    public String getIdCinema() {
        return idCinema;
    }

    public void setIdCinema(String idCinema) {
        this.idCinema = idCinema;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenphim() {
        return tenphim;
    }

    public void setTenphim(String tenphim) {
        this.tenphim = tenphim;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getAnhbia() {
        return anhbia;
    }

    public void setAnhbia(String anhbia) {
        this.anhbia = anhbia;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getThoigian() {
        return thoigian;
    }

    public void setThoigian(int thoigian) {
        this.thoigian = thoigian;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getNgayKhoiChieu() {
        return ngayKhoiChieu;
    }

    public void setNgayKhoiChieu(String ngayKhoiChieu) {
        this.ngayKhoiChieu = ngayKhoiChieu;
    }

    public List<MovieType> getLoaiphims() {
        return loaiphims;
    }

    public void setLoaiphims(List<MovieType> loaiphims) {
        this.loaiphims = loaiphims;
    }

    public ArrayList<TimeV2> getSuatchieus() {
        return suatchieus;
    }

    public void setSuatchieus(ArrayList<TimeV2> suatchieus) {
        this.suatchieus = suatchieus;
    }
}
