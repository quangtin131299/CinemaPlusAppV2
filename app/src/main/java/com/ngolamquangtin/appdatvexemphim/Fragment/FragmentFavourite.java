package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ngolamquangtin.appdatvexemphim.Activity.ChooseSessionActivity;
import com.ngolamquangtin.appdatvexemphim.Adapter.MovieFavouriteAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.MovieFavourite;
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

public class FragmentFavourite extends Fragment {

    TextView txtthongbaokcoketnoi;
    SwipeRefreshLayout refeshfavourite;
    ListView lvMovieFavourite;
    MovieFavouriteAdapter movieFavouriteAdapter;
    ArrayList<MovieFavourite> movieFavourites;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        addControls(view);
        //addEvents();
        return view;
    }

    private void addEvents() {
        refeshfavourite.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                movieFavourites.clear();
                movieFavouriteAdapter.notifyDataSetChanged();
//                loadMovieFavourite();
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
        Call<List<MovieFavourite>> listCall = service.getListMovieFavourite();
        listCall.enqueue(new Callback<List<MovieFavourite>>() {
            @Override
            public void onResponse(Call<List<MovieFavourite>> call, Response<List<MovieFavourite>> response) {
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
            public void onFailure(Call<List<MovieFavourite>> call, Throwable t) {
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
//        loadMovieFavourite();
    }
}
