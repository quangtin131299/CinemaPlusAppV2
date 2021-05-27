package com.ngolamquangtin.appdatvexemphim.DTO;

public class Bill {

    private int id;
    private String ngay;
    private int thanhtien;
    private int idkhach;
    private String trangthai;

    public Bill() {
    }

    public Bill(int id, String ngay, int thanhtien, int idkhach, String trangthai) {
        this.id = id;
        this.ngay = ngay;
        this.thanhtien = thanhtien;
        this.idkhach = idkhach;
        this.trangthai = trangthai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public int getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(int thanhtien) {
        this.thanhtien = thanhtien;
    }

    public int getIdkhach() {
        return idkhach;
    }

    public void setIdkhach(int idkhach) {
        this.idkhach = idkhach;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }
}
