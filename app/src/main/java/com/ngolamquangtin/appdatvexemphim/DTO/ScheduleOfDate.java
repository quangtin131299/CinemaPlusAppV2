package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ScheduleOfDate {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ngay")
    @Expose
    private String ngay;
    @SerializedName("phim")
    @Expose
    private ArrayList<Movie> movies = null;

    public ScheduleOfDate() {
    }

    public ScheduleOfDate(Integer id, String ngay, ArrayList<Movie> movies) {
        this.id = id;
        this.ngay = ngay;
        this.movies = movies;
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

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
