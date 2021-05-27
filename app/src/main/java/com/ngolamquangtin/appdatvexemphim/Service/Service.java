package com.ngolamquangtin.appdatvexemphim.Service;

import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.DTO.Customer;
import com.ngolamquangtin.appdatvexemphim.DTO.CustomerV2;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.DTO.MovieDetail;
import com.ngolamquangtin.appdatvexemphim.DTO.MovieFavourite;
import com.ngolamquangtin.appdatvexemphim.DTO.Room;
import com.ngolamquangtin.appdatvexemphim.DTO.Seat;
import com.ngolamquangtin.appdatvexemphim.DTO.Ticker;
import com.ngolamquangtin.appdatvexemphim.DTO.TicketV2;
import com.ngolamquangtin.appdatvexemphim.DTO.XuatChieu;

import java.util.List;
import java.util.Queue;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Service {

    @GET("movies/getallmovie")
    Call<List<Movie>> getMovieDangChieu();

    @GET("loadphimsapchieu")
    Call<List<Movie>> getMovieSapChieu();

    @GET("tickets/getyourticketbyid")
    Call<List<TicketV2>>getListTicker(@Query("ngayhientai") String ngayhientai, @Query("thoigianhientai") String thoigianhientai, @Query("idcustomer") int iduser);

    @GET("cinemas/getallcinema")
    Call<List<Cinema>> getListCinema();

    @GET("loadphimyeuthich")
    Call<List<MovieFavourite>> getListMovieFavourite();

    @GET("loadallghe")
    Call<List<Seat>> getListGhe(@Query("idphong") int idphong);

//    @GET("loadchitietphim")
//    Call<MovieDetail> getMovieDetail(@Query("idmovie") int idmovie);
    
    @GET("loadxuatchieu")
    Call<List<XuatChieu>> getXuatChieu(@Query("idphim") int idphim, @Query("idrap") int idrap, @Query("ngayhientai") String ngayhientai);

    @GET("loadphong")
    Call<List<Room>> getPhong(@Query("suatchieu") int idsuatchieu, @Query("idphim") int idphim, @Query("idrap") int idrap, @Query("ngayhientai") String ngayhientai);

    @PUT("loadghe")
    @FormUrlEncoded
    Call<List<Seat>> getSeat(@Field("idphong")int idphong, @Field("rapphim") int idrapphim, @Field("idphim") int idphim, @Field("idsuatchieu") int idsuatchieu, @Field("ngaydathientai") String ngayhientai);

    @POST("authencation/logincustomer")
    @FormUrlEncoded
    Call<CustomerV2> Login(@Field("account") String account , @Field("password") String password);

    @GET("authencation/validateemailandnumberphone")
    Call<Integer> validateEmailAndNumberPhone(@Query("email") String email, @Query("numberphone") String numberphone);

    // nếu sử dụng @Body thì không sử dụng được @FormUrlEncoded
    @POST("authencation/register")
    Call<CustomerV2> Register(@Body CustomerV2 customer);

    @POST("updatepassuser")
    Call<Customer> updatePassUser(@Body Customer customer);

    @POST("capnhatthongtinkhach")
    Call<Customer> updateTTUser(@Body Customer customer);
}


