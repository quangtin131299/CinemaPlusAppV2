package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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
    @SerializedName("suatchieus")
    @Expose
    private List<TimeV2> gio = new ArrayList<>();
    @SerializedName("phim")
    @Expose
    private Movie moive;

    public Schedule() {
    }

    public Schedule(Integer id, String ngay, Integer idRap, List<TimeV2> gio, Movie moive) {
        this.id = id;
        this.ngay = ngay;
        this.idRap = idRap;
        this.gio = gio;
        this.moive = moive;
    }

    public Movie getMoive() {
        return moive;
    }

    public void setMoive(Movie moive) {
        this.moive = moive;
    }

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

    public List<TimeV2> getGio() {
        return gio;
    }

    public void setGio(List<TimeV2> gio) {
        this.gio = gio;
    }
}
