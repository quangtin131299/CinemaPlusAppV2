package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ngolamquangtin.appdatvexemphim.Adapter.MovieCommingSoonAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCommingSoon extends Fragment {

    TextView txtngatketnoi;
    SwipeRefreshLayout refreshlayout;
    MovieCommingSoonAdapter movieCommingSoonAdapter;
    GridView rvMovieCommingSoon;
    ArrayList<Movie> movies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commingsoon, container, false);
        addControls(view);
        addEvents();
//        loadMovieCommingSoon();
        return view;
    }

    private void addEvents() {
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                movies.clear();
                movieCommingSoonAdapter.notifyDataSetChanged();
                loadMovieCommingSoon();
            }
        });
    }

    private void loadMovieCommingSoon() {
        refreshlayout.setRefreshing(true);
        Service service = RetrofitUtil.getService(getActivity());
        Call<List<Movie>> listCall = service.getMovieSapChieu();
        listCall.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                refreshlayout.setRefreshing(false);
                if (response.body() != null) {
                    if (txtngatketnoi.getVisibility() == View.VISIBLE) {
                        txtngatketnoi.setVisibility(View.INVISIBLE);
                        rvMovieCommingSoon.setVisibility(View.VISIBLE);
                    }

                    movies.addAll(response.body());
                    movieCommingSoonAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                refreshlayout.setRefreshing(false);
                if(txtngatketnoi.getVisibility() != View.VISIBLE){
                    txtngatketnoi.setVisibility(View.VISIBLE);
                    rvMovieCommingSoon.setVisibility(View.INVISIBLE);
                }
                call.clone().enqueue(this);
            }
        });
    }

    private void addControls(View view) {
        txtngatketnoi = view.findViewById(R.id.txtthongbaongatketnoi);
        refreshlayout = view.findViewById(R.id.refreshlayout);
        rvMovieCommingSoon = view.findViewById(R.id.rvMovieCommingSoon);
        movies = new ArrayList<>();
        movieCommingSoonAdapter = new MovieCommingSoonAdapter(getActivity(), movies);
        rvMovieCommingSoon.setAdapter(movieCommingSoonAdapter);
    }
}
