package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDetail extends Movie {

//    private String mota;
//    private String tenloai;
//    private String ngaykhoichieu;
//    private String trailer;


    @SerializedName("Trailer")
    @Expose
    private String trailer;

    @SerializedName("TenLoai")
    @Expose
    private String tenloai;
    @SerializedName("MoTa")
    @Expose
    private String mota;
    @SerializedName("NgayKhoiChieu")
    @Expose
    private String ngaykhoichieu;



    public String getTrailer() {
        return trailer;
    }

    public MovieDetail setTrailer(String trailer) {
        this.trailer = trailer;
        return this;
    }

    public String getTenloai() {
        return tenloai;
    }

    public MovieDetail setTenloai(String tenloai) {
        this.tenloai = tenloai;
        return this;
    }

    public String getMota() {
        return mota;
    }

    public MovieDetail setMota(String mota) {
        this.mota = mota;
        return this;
    }

    public String getNgaykhoichieu() {
        return ngaykhoichieu;
    }

    public MovieDetail setNgaykhoichieu(String ngaykhoichieu) {
        this.ngaykhoichieu = ngaykhoichieu;
        return this;
    }

    public MovieDetail() {
    }

    public MovieDetail(String trailer, String tenloai, String mota, String ngaykhoichieu) {
        this.trailer = trailer;
        this.tenloai = tenloai;
        this.mota = mota;
        this.ngaykhoichieu = ngaykhoichieu;
    }
}
