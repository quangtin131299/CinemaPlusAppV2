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


    @Override
    public String getTrailer() {
        return trailer;
    }

    @Override
    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    @Override
    public String getMota() {
        return mota;
    }

    @Override
    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getNgaykhoichieu() {
        return ngaykhoichieu;
    }

    public void setNgaykhoichieu(String ngaykhoichieu) {
        this.ngaykhoichieu = ngaykhoichieu;
    }
}
