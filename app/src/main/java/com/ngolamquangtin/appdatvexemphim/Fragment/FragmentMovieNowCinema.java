package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ngolamquangtin.appdatvexemphim.Activity.MovieNowShowActivity;
import com.ngolamquangtin.appdatvexemphim.Adapter.MovieNowShowCinemaAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMovieNowCinema extends Fragment {

    int idCinema;
    SwipeRefreshLayout refeshMovie;
    ArrayList<Movie> movies;
    MovieNowShowCinemaAdapter movieAdapter;
    ListView lvMovie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_now_show_cinema, container,false);

        addControls(view);

        setIdCinema();

        loadMovieByCinema(idCinema);

        return view;
    }

    public void addControls(View view){
        refeshMovie = view.findViewById(R.id.refeshmovie);
        movies = new ArrayList<>();
        movieAdapter = new MovieNowShowCinemaAdapter(getActivity(), movies);
        lvMovie = view.findViewById(R.id.lvMovie);
        lvMovie.setAdapter(movieAdapter);
    }

    public void loadMovieByCinema(int idCinema){
        Service service = RetrofitUtil.getService(getActivity());
        Call<ArrayList<Movie>> call = service.getAllMovieByCinemaId(idCinema);
        call.enqueue(new Callback<ArrayList<Movie>>() {
            @Override
            public void onResponse(Call<ArrayList<Movie>> call, Response<ArrayList<Movie>> response) {
                movies.clear();

                if(response.body() != null && response.body().size() != 0){
                    movies.addAll(response.body());
                }

                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Movie>> call, Throwable t) {

            }
        });
    }

    public void setIdCinema(){
        idCinema = getArguments().getInt("ID_CINEMA");
    }

    public void addEvents() {
        refeshMovie.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(idCinema != 0){
                    loadMovieByCinema(idCinema);
                }
            }
        });
    }
}
