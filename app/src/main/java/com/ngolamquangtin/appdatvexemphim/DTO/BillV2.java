package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BillV2 implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ngY")
    @Expose
    private String ngaylaphoadon;
    @SerializedName("thanhtien")
    @Expose
    private Integer thanhtien;
    @SerializedName("idKhachHang")
    @Expose
    private Integer idkhachhang;
    @SerializedName("trangThai")
    @Expose
    private String trangthai;

    @SerializedName("ptThanhToan")
    @Expose
    private String ptThanhToan;
    @SerializedName("vedats")
    @Expose
    private ArrayList<TicketV2> ticketV2s;
    @SerializedName("bapnuocs")
    @Expose
    private ArrayList<PopCorn> popCorns;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNgaylaphoadon() {
        return ngaylaphoadon;
    }

    public void setNgaylaphoadon(String ngaylaphoadon) {
        this.ngaylaphoadon = ngaylaphoadon;
    }

    public Integer getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(Integer thanhtien) {
        this.thanhtien = thanhtien;
    }

    public Integer getIdkhachhang() {
        return idkhachhang;
    }

    public void setIdkhachhang(Integer idkhachhang) {
        this.idkhachhang = idkhachhang;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getPtThanhToan() {
        return ptThanhToan;
    }

    public void setPtThanhToan(String ptThanhToan) {
        this.ptThanhToan = ptThanhToan;
    }

    public ArrayList<TicketV2> getTicketV2s() {
        return ticketV2s;
    }

    public void setTicketV2s(ArrayList<TicketV2> ticketV2s) {
        this.ticketV2s = ticketV2s;
    }

    public ArrayList<PopCorn> getPopCorns() {
        return popCorns;
    }

    public void setPopCorns(ArrayList<PopCorn> popCorns) {
        this.popCorns = popCorns;
    }
}
