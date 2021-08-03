package com.ngolamquangtin.appdatvexemphim.Service;

import com.ngolamquangtin.appdatvexemphim.DTO.BillV2;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.DTO.Comment;
import com.ngolamquangtin.appdatvexemphim.DTO.Country;
import com.ngolamquangtin.appdatvexemphim.DTO.Customer;
import com.ngolamquangtin.appdatvexemphim.DTO.CustomerV2;
import com.ngolamquangtin.appdatvexemphim.DTO.MessageResponse;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.DTO.MovieType;
import com.ngolamquangtin.appdatvexemphim.DTO.PopCorn;
import com.ngolamquangtin.appdatvexemphim.DTO.RoomV2;
import com.ngolamquangtin.appdatvexemphim.DTO.Schedule;
import com.ngolamquangtin.appdatvexemphim.DTO.ScheduleOfDate;
import com.ngolamquangtin.appdatvexemphim.DTO.SeatV2;
import com.ngolamquangtin.appdatvexemphim.DTO.Ticker;
import com.ngolamquangtin.appdatvexemphim.DTO.TickerBook;
import com.ngolamquangtin.appdatvexemphim.DTO.TicketV2;
import com.ngolamquangtin.appdatvexemphim.DTO.TokenClient;

import java.util.ArrayList;
import java.util.List;

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
    Call<List<Movie>> getMovieDangChieu(@Query("currentTime") String currentTime, @Query("currentDate") String currentDate);

    @GET("movies/getmoviecomingsoon")
    Call<List<Movie>> getMovieSapChieu();

    @GET("tickets/getyourticketbyid")
    Call<List<TicketV2>> getListTicker(@Query("currentDate") String ngayhientai
            , @Query("idcustomer") int iduser);

    @GET("cinemas/getallcinema")
    Call<List<Cinema>> getListCinema();

    @GET("cinemas/getcinemabymovieid")
    Call<List<Cinema>> getCinemaByMovieId(@Query("idMovie") int idMoive);

    @GET("movies/getfavorite")
    Call<List<Movie>> getListMovieFavourite();

    @GET("seat/getallseatofroom")
    Call<List<SeatV2>> getSeats(@Query("idMovie") int idMovie
            , @Query("currentDate") String currentDate
            , @Query("idRoom") int idRoom
            , @Query("idShowTime") int idShowTime
            , @Query("idCinema") int idCinema);

//    @GET("loadchitietphim")
//    Call<MovieDetail> getMovieDetail(@Query("idmovie") int idmovie);

    @GET("schedule/getschedulebycinema")
    Call<Schedule> getShowTimes(@Query("idMovie") int idphim
            , @Query("idCinema") int idrap
            , @Query("currentDate") String ngayhientai);

    @GET("room/getroomofnowmovie")
    Call<RoomV2> getPhong(@Query("idMovie") int idMovie
            , @Query("idCinema") int idCinema
            , @Query("currentDate") String currentDate
            , @Query("idShowTime") int idShowTime);

    @GET("seat/getallseatbooking")
    Call<List<SeatV2>> getSeatBookings(@Query("idMovie") int idMovie
            , @Query("currentDate") String currentDate
            , @Query("idRoom") int idRoom
            , @Query("idShowTime") int idShowTime
            , @Query("idCinema") int idCinema);

    @POST("authencation/logincustomer")
    @FormUrlEncoded
    Call<CustomerV2> Login(@Field("account") String account, @Field("password") String password);

    @GET("authencation/validateemailandnumberphone")
    Call<Integer> validateEmailAndNumberPhone(@Query("email") String email
            , @Query("numberphone") String numberphone);

    // nếu sử dụng @Body thì không sử dụng được @FormUrlEncoded
    @POST("authencation/register")
    Call<CustomerV2> Register(@Body CustomerV2 customer);

    @POST("updatepassuser")
    Call<Customer> updatePassUser(@Body Customer customer);

    @PUT("authencation/updateinforuser")
    Call<CustomerV2> updateTTUser(@Body CustomerV2 customer);

    @POST("tickets/processtickerbooking")
    Call<Integer> processTickerBooking(@Body TickerBook tickerBooking);

    @GET("comment/getcommentbymovie")
    Call<List<Comment>> getAllCommentOfMovieId(@Query("idMovie") int idMoive);

    @POST("comment/addnewcomment")
    Call<MessageResponse> addNewComment(@Body Comment newComment);

    @GET("schedule/getscheduleofdate")
    Call<ScheduleOfDate> getScheduleOfDate(@Query("idCinema") int idCinema
                                         , @Query("currentDate") String currentDate);

    @GET("type/getalltype")
    Call<ArrayList<MovieType>> getAllType();

    @GET("movies/searchmovie")
    Call<ArrayList<Movie>> searchMovie(@Query("keyWord") String keyWord
                                        , @Query("typeName") String typeId
                                        , @Query("countryName") String countryId
                                        , @Query("isMovieNowShow") int isMovieNowShow
                                        , @Query("cinemaName") String idCinema
                                        , @Query("isFilter") int isFilter );

    @GET("cinemas/searchnearme")
    Call<ArrayList<Cinema>> searchCinemaNearMe(@Query("keyWord") String keyWord
                                             , @Query("idCinemas") Integer[] idCinemas);

    @GET("cinemas/searchallcinema")
    Call<ArrayList<Cinema>> searchAllMovie(@Query("keyWord") String keyWord);

    @GET("movies/getallmoviebycinemaid")
    Call<ArrayList<Movie>> getAllMovieByCinemaId(@Query("idCinema") int idCinema);

    @GET("country/getcountrymovie")
    Call<ArrayList<Country>> getCountryOfMovie();

    @GET("popcorn/getallpopcorn")
    Call<ArrayList<PopCorn>> getAllPopCorn();

    @GET("cinemas/getcinemasuggestion")
    Call<ArrayList<Cinema>> getCinemaSuggest(@Query("idCinemas") Integer[] idCinemas
                                            , @Query("idMovie") int idMovie);
    @GET("movies/getmovieofticker")
    Call<ArrayList<Movie>> getMovieOfTicker(@Query("idCustomer") int idCustomer);

    @GET("bill/getallbillofcustomer")
    Call<ArrayList<BillV2>> getAllBillByCustomerId(@Query("idCustomer") int idCustomer);

    @GET("movies/searchallmovie")
    Call<ArrayList<Movie>> searchAllMovie(@Query("keyWord") String keyWord
                                        , @Query("typeName") String typeName
                                        , @Query("countryName") String countryName
                                        , @Query("cinemaName") String cinemaName);

    @GET("authencation/updateImageProfile")
    Call<Integer> updateImageProfile(@Query("idCustomer") int idCustomer, @Query("imgProfileUrl") String imgProfileUrl);

    @POST("tokenclient/addnewtokenclient")
    Call<Integer> addNewTokenClient(@Body TokenClient token);

    @PUT("tickets/updatestatuscancel")
    Call<Integer> updateStatusCancelTicker(@Body TicketV2 ticker);

    @PUT("authencation/updatepassword")
    @FormUrlEncoded()
    Call<MessageResponse> updatePassWord(@Field("idCustomer") Integer idCustomer, @Field("newPass") String newPassWord);
}


