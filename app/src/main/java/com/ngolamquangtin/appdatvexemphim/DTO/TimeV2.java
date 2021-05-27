package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TimeV2 implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("gio")
    @Expose
    private String gio;
    @SerializedName("idLichChieu")
    @Expose
    private Integer idlichchieu;

    public Integer getId() {
        return id;
    }

    public TimeV2 setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getGio() {
        return gio;
    }

    public TimeV2 setGio(String gio) {
        this.gio = gio;
        return this;
    }

    public Integer getIdlichchieu() {
        return idlichchieu;
    }

    public TimeV2 setIdlichchieu(Integer idlichchieu) {
        this.idlichchieu = idlichchieu;
        return this;
    }
}
