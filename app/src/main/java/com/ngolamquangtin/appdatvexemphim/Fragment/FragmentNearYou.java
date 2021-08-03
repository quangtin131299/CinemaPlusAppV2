package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.astritveliu.boom.Boom;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ngolamquangtin.appdatvexemphim.Activity.ChooseSessionActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.CinemaActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.DetalsMovieActivity;
import com.ngolamquangtin.appdatvexemphim.Adapter.MovieNearYouAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.MoviePageAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNearYou extends Fragment {

    String currentTime, currentDate;
    Button btnFastBooking;
    TextView txtNameMovie, txtMessage, txtDuration,txtOpenDate;
    MoviePageAdapter moviePageAdapter;
    ViewPager2 viewpageMovie;
    SwipeRefreshLayout refeshMovieNowShow;
    ArrayList<Movie> movies = new ArrayList<>();
    Movie movieSelect;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearyou, container, false);

        addControls(view);

        initViewPagerMovie();

        addEvents();

        loadDataMovieNowShow(currentTime, currentDate);

        return view;
    }


    public void initViewPagerMovie() {
        viewpageMovie.setClipToPadding(false);
        viewpageMovie.setClipChildren(false);
        viewpageMovie.setOffscreenPageLimit(3);

        viewpageMovie.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        viewpageMovie.setAdapter(moviePageAdapter);

        CompositePageTransformer transformer = new CompositePageTransformer();

        transformer.addTransformer(new MarginPageTransformer(5));

        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull  View page, float position) {
                float v = 1 - Math.abs(position);

                page.setScaleY(0.8f + v * 0.2f);
            }
        });

        viewpageMovie.setPageTransformer(transformer);
    }

    private void addEvents() {

        btnFastBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog btnDialogOption = new BottomSheetDialog(getActivity(), R.style.dialogQR);

                btnDialogOption.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.dialogbottom_option_booking, null));

                ConstraintLayout contrainBookingMovie = btnDialogOption.findViewById(R.id.contrainmovie);
                ConstraintLayout contrainBookingCinema = btnDialogOption.findViewById(R.id.contrainbookingcinema);
                ConstraintLayout contrainDetroyOption = btnDialogOption.findViewById(R.id.contraindetroy);

                contrainBookingMovie.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentToScreenChoosesion = new Intent(getActivity(), ChooseSessionActivity.class);

                        intentToScreenChoosesion.putExtra("ID_MOVIE", movieSelect.getId());
                        intentToScreenChoosesion.putExtra("IMAGE_MOVIE", movieSelect.getHinh());
                        intentToScreenChoosesion.putExtra("TEN_PHIM", movieSelect.getTenphim());

                        startActivity(intentToScreenChoosesion);
                    }
                });

                contrainBookingCinema.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnDialogOption.dismiss();

                        Intent intentToScreenCinema = new Intent(getActivity(), CinemaActivity.class);

                        intentToScreenCinema.putExtra("ID_MOVIE", movieSelect.getId());
                        intentToScreenCinema.putExtra("IMAGE_MOVIE", movieSelect.getHinh());
                        intentToScreenCinema.putExtra("TEN_PHIM", movieSelect.getTenphim());

                        startActivity(intentToScreenCinema);
                    }
                });

                contrainDetroyOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnDialogOption.dismiss();
                    }
                });

                new Boom(contrainBookingMovie);
                new Boom(contrainBookingCinema);
                new Boom(contrainDetroyOption);

                btnDialogOption.show();
            }
        });

        refeshMovieNowShow.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                movies.clear();
                moviePageAdapter.notifyDataSetChanged();
                loadDataMovieNowShow(currentTime, currentDate);
            }
        });

        viewpageMovie.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Movie movie = movies.get(position);

                if(movie != null){
                    movieSelect = movie;

                    txtNameMovie.setText(movie.getTenphim());
                    txtOpenDate.setText("Khởi chiếu ngày: " + Util.formatDateServerToClient(movie.getNgayKhoiChieu()));

                    txtDuration.setText("Thời gian: " + movie.getThoigian() +" phút");
                }
            }
        });

        new Boom(btnFastBooking);
    }

    private void addControls(View view) {
        currentTime = Util.getCurrentTime();
        currentDate = Util.formatDateClientToServer(Util.formatDateByCalendar(Calendar.getInstance()));
        txtDuration = view.findViewById(R.id.txtduration);
        txtOpenDate = view.findViewById(R.id.txtopendate);
        btnFastBooking = view.findViewById(R.id.btnfastbooking);
        txtNameMovie = view.findViewById(R.id.txtnamemovie);
        moviePageAdapter = new MoviePageAdapter(getActivity(), movies);
        txtMessage = view.findViewById(R.id.txtngatketnoi);
        viewpageMovie = view.findViewById(R.id.viewpagermovie);
        refeshMovieNowShow = view.findViewById(R.id.refeshmovienearyou);
    }

    public void loadDataMovieNowShow(String currentTime, String currentDate) {
        refeshMovieNowShow.setRefreshing(true);
        Service service = RetrofitUtil.getService(getActivity());
        Call<List<Movie>> listCall = service.getMovieDangChieu(currentTime,currentDate);
        listCall.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                refeshMovieNowShow.setRefreshing(false);
                movies.clear();

                if (response.body() != null && response.body().size() != 0) {
                    txtMessage.setVisibility(View.INVISIBLE);
                    movies.addAll(response.body());

                }else{
                    txtMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("Hiện không có phim đang chiếu");
                }

                moviePageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                if (txtMessage.getVisibility() != View.VISIBLE) {
                    refeshMovieNowShow.setRefreshing(false);

                    txtMessage.setVisibility(View.VISIBLE);
                }
                call.clone().enqueue(this);

            }
        });
    }

    public void searchMovie(String keyWord,  String typeName , String countryName, String cinemaType,int isFilter){
        Service service = RetrofitUtil.getService(getActivity());
        Call<ArrayList<Movie>> call = service.searchMovie(keyWord, typeName,countryName,1, cinemaType,isFilter);

        call.enqueue(new Callback<ArrayList<Movie>>() {
            @Override
            public void onResponse(Call<ArrayList<Movie>> call, Response<ArrayList<Movie>> response) {
                movies.clear();
                
                if(response.body() != null && response.body().size() != 0){
                    txtMessage.setVisibility(View.INVISIBLE);


                    movies.addAll(response.body());

                }else{
                    txtMessage.setText("Không tìm thấy phim");
                    txtMessage.setVisibility(View.VISIBLE);
                }

                moviePageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Movie>> call, Throwable t) {
                txtMessage.setText("Bạn bị ngắt kết nối");
                txtMessage.setVisibility(View.VISIBLE);
            }
        });

    }
}

