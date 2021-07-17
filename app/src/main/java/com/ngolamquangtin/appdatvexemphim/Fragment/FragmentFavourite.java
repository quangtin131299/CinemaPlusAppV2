package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ngolamquangtin.appdatvexemphim.Activity.ChooseSessionActivity;
import com.ngolamquangtin.appdatvexemphim.Adapter.MovieFavouriteAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.DTO.MovieFavourite;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFavourite extends Fragment {

    TextView txtthongbaokcoketnoi;
    SwipeRefreshLayout refeshfavourite;
    ListView lvMovieFavourite;
    MovieFavouriteAdapter movieFavouriteAdapter;
    ArrayList<Movie> movieFavourites;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        addControls(view);
        addEvents();
        return view;
    }

    private void addEvents() {
        refeshfavourite.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                movieFavourites.clear();
                movieFavouriteAdapter.notifyDataSetChanged();
                loadMovieFavourite();
            }
        });
        lvMovieFavourite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ChooseSessionActivity.class);
                i.putExtra("ID_MOVIE", movieFavourites.get(position).getId());
            }
        });
    }

    private void loadMovieFavourite() {
        refeshfavourite.setRefreshing(true);
        Service service = RetrofitUtil.getService(getActivity());
        Call<List<Movie>> listCall = service.getListMovieFavourite();
        listCall.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.body() != null) {
                    refeshfavourite.setRefreshing(false);
                    if(txtthongbaokcoketnoi.getVisibility() == View.VISIBLE){
                        txtthongbaokcoketnoi.setVisibility(View.INVISIBLE);
                        lvMovieFavourite.setVisibility(View.VISIBLE);
                    }
                    movieFavourites.addAll(response.body());
                    movieFavouriteAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                if(txtthongbaokcoketnoi.getVisibility() != View.VISIBLE){
                    refeshfavourite.setRefreshing(false);
                    txtthongbaokcoketnoi.setVisibility(View.VISIBLE);
                    lvMovieFavourite.setVisibility(View.INVISIBLE);
                }
                call.clone().enqueue(this);
            }
        });
    }

    private void addControls(View view) {
        txtthongbaokcoketnoi = view.findViewById(R.id.txtthongbaokcoketnoi);
        refeshfavourite = view.findViewById(R.id.refeshfavourite);
        lvMovieFavourite = view.findViewById(R.id.lvmoviefavourite);
        movieFavourites = new ArrayList<>();
        movieFavouriteAdapter = new MovieFavouriteAdapter(getActivity().getApplicationContext(), movieFavourites);
        lvMovieFavourite.setAdapter(movieFavouriteAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        movieFavourites.clear();
        movieFavouriteAdapter.notifyDataSetChanged();
        loadMovieFavourite();
    }
}
