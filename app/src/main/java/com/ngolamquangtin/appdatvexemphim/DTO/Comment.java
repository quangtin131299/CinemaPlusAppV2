package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("noiDung")
    @Expose
    private String Content;
    @SerializedName("idKhachHang")
    @Expose
    private Integer idCustomer;
    @SerializedName("hoTen")
    @Expose
    private String nameCustomer;
    @SerializedName("anhDaiDien")
    @Expose
    private String avatar;
    @SerializedName("ngayDang")
    @Expose
    private String datePost;
    @SerializedName("idPhim")
    @Expose
    private Integer idMovie;

    public Integer getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(Integer idMovie) {
        this.idMovie = idMovie;
    }

    public Comment() {
    }

    public Comment(Integer id, String content, Integer idCustomer, String nameCustomer, String avatar, String datePost, Integer idMovie) {
        this.id = id;
        this.Content = content;
        this.idCustomer = idCustomer;
        this.nameCustomer = nameCustomer;
        this.avatar = avatar;
        this.datePost = datePost;
        this.idMovie = idMovie;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Integer getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Integer idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }
}
