package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ngolamquangtin.appdatvexemphim.Activity.DetalsMovieActivity;
import com.ngolamquangtin.appdatvexemphim.Adapter.MovieNearYouAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNearYou extends Fragment {

    TextView txtngatketnoi;
    MovieNearYouAdapter movieNearYouAdapter;
    SwipeRefreshLayout refeshmovienearyou;
    GridView rvMovie;
    ArrayList<Movie> movies = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearyou, container, false);
        addControls(view);
        addEvents();
        loadDataPhimDangChieu();
        return view;
    }

    private void addEvents() {
        rvMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movies.get(position);
                if (movie != null) {
                    Intent i = new Intent(getActivity(), DetalsMovieActivity.class);
                    i.putExtra("MOVIE", movie);
                    startActivity(i);
                }
            }
        });
        refeshmovienearyou.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                movies.clear();
                movieNearYouAdapter.notifyDataSetChanged();
                loadDataPhimDangChieu();
            }
        });
    }

    private void addControls(View view) {
        txtngatketnoi = view.findViewById(R.id.txtngatketnoi);
        txtngatketnoi.setVisibility(View.INVISIBLE);
        movieNearYouAdapter = new MovieNearYouAdapter(getActivity(), movies);
        rvMovie = view.findViewById(R.id.rvMovieNearYou);
        rvMovie.setAdapter(movieNearYouAdapter);
        refeshmovienearyou = view.findViewById(R.id.refeshmovienearyou);
    }

    public void loadDataPhimDangChieu() {
        refeshmovienearyou.setRefreshing(true);
        Service service = RetrofitUtil.getService(getActivity());
        Call<List<Movie>> listCall = service.getMovieDangChieu();
        listCall.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.body() != null) {
                    if (txtngatketnoi.getVisibility() == View.VISIBLE) {
                        txtngatketnoi.setVisibility(View.INVISIBLE);
                        rvMovie.setVisibility(View.VISIBLE);
                    }
                    refeshmovienearyou.setRefreshing(false);
                    movies.addAll(response.body());
                    movieNearYouAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                if (txtngatketnoi.getVisibility() != View.VISIBLE) {
                    refeshmovienearyou.setRefreshing(false);
                    txtngatketnoi.setVisibility(View.VISIBLE);
                    rvMovie.setVisibility(View.INVISIBLE);
                }
                call.clone().enqueue(this);

            }
        });

    }
}

