package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.ngolamquangtin.appdatvexemphim.Adapter.MoviePageAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCommingSoon extends Fragment {

    TextView txtMess, txtNameMovie,txtOpenDate, txtTime;
    SwipeRefreshLayout refreshlayout;
    MoviePageAdapter moviePageAdapter;
    ViewPager2 viewpageMovie;
    ArrayList<Movie> movies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commingsoon, container, false);

        addControls(view);

        initViewPagerMovie();

        addEvents();

        return view;
    }

    private void initViewPagerMovie() {
        viewpageMovie.setClipToPadding(false);
        viewpageMovie.setClipChildren(false);
        viewpageMovie.setOffscreenPageLimit(3);

        viewpageMovie.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        viewpageMovie.setAdapter(moviePageAdapter);

        CompositePageTransformer transformer = new CompositePageTransformer();

        transformer.addTransformer(new MarginPageTransformer(8));

        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull  View page, float position) {
                float v = 1- Math.abs(position);

                page.setScaleY(0.85f + v * 0.15f);
            }
        });

        viewpageMovie.setPageTransformer(transformer);
    }

    private void addEvents() {
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMovieCommingSoon();
            }
        });

        viewpageMovie.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Movie movie = movies.get(position);

                if(movie != null){
                    txtNameMovie.setText(movie.getTenphim());
                    txtOpenDate.setText("Khởi chiếu ngày: " + Util.formatDateServerToClient(movie.getNgayKhoiChieu()));
                    txtTime.setText("Thời gian: " + movie.getThoigian() + " phút");
                }
            }
        });

   }

    public void loadMovieCommingSoon() {
        refreshlayout.setRefreshing(true);
        Service service = RetrofitUtil.getService(getActivity());
        Call<List<Movie>> listCall = service.getMovieSapChieu();
        listCall.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                refreshlayout.setRefreshing(false);

                movies.clear();

                if (response.body() != null && response.body().size() != 0) {
                    txtMess.setVisibility(View.INVISIBLE);

                    movies.addAll(response.body());

                }else{
                    txtMess.setText("Không có phim sắp chiếu");
                    txtMess.setVisibility(View.VISIBLE);
                }

                moviePageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                refreshlayout.setRefreshing(false);

                txtMess.setText("Bạn bị ngắt kết nối");

                txtMess.setVisibility(View.VISIBLE);

                call.clone().enqueue(this);
            }
        });
    }

    private void addControls(View view) {
        movies = new ArrayList<>();
        moviePageAdapter = new MoviePageAdapter(getActivity(), movies);
        txtTime = view.findViewById(R.id.txttime);
        txtNameMovie= view.findViewById(R.id.txtnamemovie);
        txtOpenDate = view.findViewById(R.id.txtopendate);
        viewpageMovie = view.findViewById(R.id.viewpagermovie);
        txtMess = view.findViewById(R.id.txtmess);
        refreshlayout = view.findViewById(R.id.refeshmoviecomingsoon);
    }

    public void searchMovie(String keyWord, String typeName , String countryName, String cinemaType,int isFilter){
        Service service = RetrofitUtil.getService(getActivity());
        Call<ArrayList<Movie>> call = service.searchMovie(keyWord, typeName,countryName, 0, cinemaType,isFilter);
        call.enqueue(new Callback<ArrayList<Movie>>() {
            @Override
            public void onResponse(Call<ArrayList<Movie>> call, Response<ArrayList<Movie>> response) {
                movies.clear();

                if(response.body() != null && response.body().size() != 0){
                    txtMess.setVisibility(View.INVISIBLE);
                    movies.addAll(response.body());

                }else{
                    txtMess.setText("Không tìm thấy phim");
                    txtMess.setVisibility(View.VISIBLE);
                }

                moviePageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Movie>> call, Throwable t) {
                Log.d("ABC", t.getMessage());

                txtMess.setText("Bạn bị ngắt kết nối ");

                txtMess.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        loadMovieCommingSoon();
    }
}
