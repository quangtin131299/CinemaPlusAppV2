package com.ngolamquangtin.appdatvexemphim.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.astritveliu.boom.Boom;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ngolamquangtin.appdatvexemphim.Adapter.CountryAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.FilterAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.FilterMovieOfCinemAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.SearchMovieAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.DTO.Country;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.DTO.MovieType;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMovieActivity extends AppCompatActivity {

    LottieAnimationView progressing;
    ListView lvFilter;
    LinearLayout linearMovieType, linearCountry, linearCinema;
    EditText edtKeyWord;
    ListView lvMovieSearch;
    TextView txtType, txtCountry, txtCinema, txtMess;
    Handler handler;
    FilterAdapter filterTypeadapter;
    CountryAdapter countryAdapter;
    FilterMovieOfCinemAdapter filterMovieByCinemaAdapter;
    ArrayList<MovieType> types;
    ArrayList<Country> countries;
    ArrayList<Cinema> cinemas;
    ArrayList<Movie> movies;
    SearchMovieAdapter searchAdapter;
    ImageButton imgBtnSearchMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        addControls();

        initListViewSearMovie();

        registerHandler();

        loadMovieType();

        addEvents();
    }

    private void initListViewSearMovie() {
        lvMovieSearch.setAdapter(searchAdapter);
    }

    public void addEvents() {
        txtCinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog btDialogCinema = new BottomSheetDialog(SearchMovieActivity.this, R.style.BottomSheetDialogTheme);
                View viewDialogFilter = LayoutInflater.from(SearchMovieActivity.this).inflate(R.layout.dialogbottom_filter, null);

                btDialogCinema.setContentView(viewDialogFilter);

                Util.dissableBottomDialogDragging(btDialogCinema);

                TextView txtTitleFilter = btDialogCinema.findViewById(R.id.txttitlefilter);
                lvFilter = btDialogCinema.findViewById(R.id.lvfilter);

                txtTitleFilter.setText("Chọn rạp");
                lvFilter.setAdapter(filterMovieByCinemaAdapter);
                lvFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Cinema cinema = cinemas.get(i);

                        if(cinema != null){
                            txtCinema.setText(cinema.getTenrap());
                        }

                        btDialogCinema.dismiss();
                    }
                });

                btDialogCinema.show();

            }
        });

        txtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog btDialogType = new BottomSheetDialog(SearchMovieActivity.this, R.style.BottomSheetDialogTheme);

                View viewDialogFilter = LayoutInflater.from(SearchMovieActivity.this).inflate(R.layout.dialogbottom_filter, null);

                btDialogType.setContentView(viewDialogFilter);

                Util.dissableBottomDialogDragging(btDialogType);

                TextView txtTitleFilter = btDialogType.findViewById(R.id.txttitlefilter);
                lvFilter = btDialogType.findViewById(R.id.lvfilter);

                txtTitleFilter.setText("Chọn loại phim");
                lvFilter.setAdapter(filterTypeadapter);
                lvFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        MovieType type = types.get(i);

                        if(type != null){
                            txtType.setText(type.getTenLoai());
                        }

                        btDialogType.dismiss();
                    }
                });

                btDialogType.show();
            }
        });

        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog btDialogCountry = new BottomSheetDialog(SearchMovieActivity.this, R.style.BottomSheetDialogTheme);

                View viewDialogFilter = LayoutInflater.from(SearchMovieActivity.this).inflate(R.layout.dialogbottom_filter, null);

                btDialogCountry.setContentView(viewDialogFilter);

                Util.dissableBottomDialogDragging(btDialogCountry);

                TextView txtTitleFilter = btDialogCountry.findViewById(R.id.txttitlefilter);
                lvFilter = btDialogCountry.findViewById(R.id.lvfilter);

                txtTitleFilter.setText("Chọn loại phim");
                lvFilter.setAdapter(countryAdapter);
                lvFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Country country = countries.get(i);

                        if(country != null){
                            txtCountry.setText(country.getNameCountry());
                        }

                        btDialogCountry.dismiss();
                    }
                });

                btDialogCountry.show();
            }
        });

        imgBtnSearchMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyWord = edtKeyWord.getText().toString().trim().replaceAll("\\\\s+", "");
                String typeMovieName = txtType.getText().toString().equals("Tất cả loại") ? "" : txtType.getText().toString() ;
                String countryName = txtCountry.getText().toString().equals("Tất cả quốc gia") ? "": txtCountry.getText().toString();
                String cinemaName = txtCinema.getText().toString().equals("Tất cả rạp phim") ? "" : txtCinema.getText().toString();

                searchAllMovie(keyWord, typeMovieName, countryName, cinemaName);
            }
        });

        new Boom(imgBtnSearchMovie);
    }

    public void searchAllMovie(String keyWord, String typeName, String countryName, String cinemaName){
        movies.clear();
        searchAdapter.notifyDataSetChanged();
        progressing.setVisibility(View.VISIBLE);
        progressing.playAnimation();
        Service service = RetrofitUtil.getService(SearchMovieActivity.this);
        Call<ArrayList<Movie>> call = service.searchAllMovie(keyWord, typeName,countryName, cinemaName);

        call.enqueue(new Callback<ArrayList<Movie>>() {
            @Override
            public void onResponse(Call<ArrayList<Movie>> call, Response<ArrayList<Movie>> response) {
                if(response.body() != null && response.body().size() != 0){
                    progressing.pauseAnimation();
                    progressing.setVisibility(View.INVISIBLE);
                    txtMess.setVisibility(View.INVISIBLE);
                    movies.addAll(response.body());
                }else{
                    progressing.pauseAnimation();
                    progressing.setVisibility(View.INVISIBLE);
                    txtMess.setVisibility(View.VISIBLE);
                }

                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Movie>> call, Throwable t) {
                progressing.pauseAnimation();
                progressing.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void registerHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull @NotNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 1:
                        loadCountryMovie();
                        break;
                    case 2:
                        loadCinema();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void loadMovieType(){
        Service service = RetrofitUtil.getService(SearchMovieActivity.this);
        Call<ArrayList<MovieType>> arrayListCall = service.getAllType();
        arrayListCall.enqueue(new Callback<ArrayList<MovieType>>() {
            @Override
            public void onResponse(Call<ArrayList<MovieType>> call, Response<ArrayList<MovieType>> response) {
                if(response.body() != null && response.body().size() != 0){
                    types.clear();

                    MovieType defaulType = new MovieType();

                    defaulType.setId(0);
                    defaulType.setTenLoai("Tất cả loại");

                    types.add(defaulType);
                    types.addAll(response.body());
                    filterTypeadapter.notifyDataSetChanged();
                }

                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<ArrayList<MovieType>> call, Throwable t) {

            }
        });
    }

    public void addControls() {
        types = new ArrayList<>();
        countries = new ArrayList<>();
        cinemas = new ArrayList<>();
        movies = new ArrayList<>();
        searchAdapter = new SearchMovieAdapter(SearchMovieActivity.this,movies);
        filterTypeadapter = new FilterAdapter(SearchMovieActivity.this, types);
        countryAdapter = new CountryAdapter(SearchMovieActivity.this, countries);
        filterMovieByCinemaAdapter = new FilterMovieOfCinemAdapter(SearchMovieActivity.this, cinemas);
        txtType = findViewById(R.id.txtmovietype);
        txtCountry = findViewById(R.id.txtcountry);
        txtCinema = findViewById(R.id.txtcinema);
        lvMovieSearch = findViewById(R.id.lvmovie);
        edtKeyWord = findViewById(R.id.txtkeywork);
        linearMovieType = findViewById(R.id.linearmovietype);
        linearCountry = findViewById(R.id.linearcountry);
        linearCinema = findViewById(R.id.linearcinema);
        imgBtnSearchMovie = findViewById(R.id.imgbtnsearch);
        progressing = findViewById(R.id.progressing);
        txtMess = findViewById(R.id.txtmess);
    }

    private void loadCountryMovie() {
        Service service = RetrofitUtil.getService(SearchMovieActivity.this);
        Call<ArrayList<Country>> call = service.getCountryOfMovie();

        call.enqueue(new Callback<ArrayList<Country>>() {
            @Override
            public void onResponse(Call<ArrayList<Country>> call, Response<ArrayList<Country>> response) {
                countries.clear();

                if(response.body() != null && response.body().size() != 0){
                    Country country = new Country();
                    country.setId(0);
                    country.setNameCountry("Tất cả quốc gia");

                    countries.add(country);
                    countries.addAll(response.body());

                }

                countryAdapter.notifyDataSetChanged();

                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<ArrayList<Country>> call, Throwable t) {

            }
        });
    }

    public void loadCinema(){
        Service service = RetrofitUtil.getService(SearchMovieActivity.this);
        Call<List<Cinema>> call = service.getListCinema();

        call.enqueue(new Callback<List<Cinema>>() {
            @Override
            public void onResponse(Call<List<Cinema>> call, Response<List<Cinema>> response) {
                cinemas.clear();

                if(response.body() != null && response.body().size() != 0){
                    Cinema cinema = new Cinema();

                    cinema.setId(0);
                    cinema.setTenrap("Tất cả rạp phim");

                    cinemas.add(cinema);
                    cinemas.addAll(response.body());
                }

                filterMovieByCinemaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Cinema>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }


}