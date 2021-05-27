package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schedule {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ngay")
    @Expose
    private String ngay;
    @SerializedName("idRap")
    @Expose
    private Integer idRap;
    @SerializedName("phimLichchieus")
    @Expose
    private List<TimeV2> gio = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public Integer getIdRap() {
        return idRap;
    }

    public void setIdRap(Integer idRap) {
        this.idRap = idRap;
    }

    public List<PhimLichchieu> getPhimLichchieus() {
        return phimLichchieus;
    }

    public void setPhimLichchieus(List<PhimLichchieu> phimLichchieus) {
        this.phimLichchieus = phimLichchieus;
    }


}
