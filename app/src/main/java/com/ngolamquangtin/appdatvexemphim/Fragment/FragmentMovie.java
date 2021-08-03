package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.astritveliu.boom.Boom;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.ngolamquangtin.appdatvexemphim.Activity.DetailCinemaActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.SearchMovieActivity;
import com.ngolamquangtin.appdatvexemphim.Adapter.CinemaNearMeAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.CountryAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.FilterAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.FilterMovieOfCinemAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.MovieViewPagerAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.DTO.Country;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.DTO.MovieType;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMovie extends Fragment {

    final int REQUEST_CODE_LOCALTION_PERMISSION = 1;

    LottieAnimationView progressing;
    CinemaNearMeAdapter cinemaNearMeAdapter;
    Location currentLocation;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    RecyclerView rycinema;
    Handler handler;
    ArrayList<Cinema> cinemas,cinemaNearMe;
    TabLayout tabLayout;
    ViewPager viewPager;
    MovieViewPagerAdapter movieViewPagerAdapter;
    TextView txtKeyWork;
    ImageButton btnLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        addControls(view);

        initRecyerViewCinema();

        initRequestLocation();

        requestPermisson();

        registerHandler();

        prepearViewPager();

        addEvents();

        return view;
    }

    public void updateBtnLocation() {
        if(Util.checkEnablePermisson(getActivity())){
            if(Util.checkLocationTurnOn(getActivity())){
                setCurrentLocation();
                btnLocation.setImageResource(R.drawable.ic_turnonlocation);
            }
        }
    }

    public void initRequestLocation() {
        locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    public void initRecyerViewCinema() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        rycinema.setLayoutManager(layoutManager);
        rycinema.setAdapter(cinemaNearMeAdapter);
    }

    public void registerHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull @NotNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 2:
                        loadCinema();
                    case 3:
                        loadCinemaNearMe();
                    default:
                        break;
                }
            }
        };
    }

    private void addEvents() {
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Util.checkEnablePermisson(getActivity())){
                    if(Util.checkLocationTurnOn(getActivity())){
                        setCurrentLocation();

                        btnLocation.setImageResource(R.drawable.ic_turnonlocation);
                    }else{
                        Util.turnOnLocation(getActivity());

                        setCurrentLocation();

                        btnLocation.setImageResource(R.drawable.ic_turnonlocation);
                    }
                }else{
                    requestPermisson();
                }
            }
        });


        txtKeyWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToScreenSearch = new Intent(getActivity(), SearchMovieActivity.class);

                startActivity(intentToScreenSearch);
            }
        });
    }

    private void addControls(View view) {
        cinemaNearMe = new ArrayList<>();
        cinemas = new ArrayList<>();
        cinemaNearMeAdapter = new CinemaNearMeAdapter(getActivity(), cinemaNearMe);
        rycinema = view.findViewById(R.id.rycinema);
        txtKeyWork = view.findViewById(R.id.txtkeywork);
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpagermovie);
        btnLocation = view.findViewById(R.id.btnlocation);
        progressing = view.findViewById(R.id.progressing);
        movieViewPagerAdapter = new MovieViewPagerAdapter(getFragmentManager());

        new Boom(btnLocation);
    }

    public void prepearViewPager() {
        FragmentNearYou fragmentNearYou = new FragmentNearYou();
        FragmentCommingSoon fragmentCommingSoon = new FragmentCommingSoon();

        movieViewPagerAdapter.addFragment(fragmentNearYou, getResources().getString(R.string.movieNowShow));
        movieViewPagerAdapter.addFragment(fragmentCommingSoon, getResources().getString(R.string.movieComingSoon));

        viewPager.setAdapter(movieViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setCurrentLocation() {
        progressing.playAnimation();
        progressing.setVisibility(View.VISIBLE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            progressing.playAnimation();
            progressing.setVisibility(View.INVISIBLE);

            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                currentLocation = locationResult.getLastLocation();

                cinemaNearMeAdapter.setCurrentLocation(currentLocation);

                fusedLocationClient.removeLocationUpdates(this);

                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        }, Looper.getMainLooper());

    }

    public void loadCinemaNearMe(){
        Service service = RetrofitUtil.getService(getActivity());
        Integer [] idcinema = new Integer[50];

        getCinemaNearMe().toArray(idcinema);

        Call<ArrayList<Cinema>> call = service.searchCinemaNearMe("", idcinema);

        call.enqueue(new Callback<ArrayList<Cinema>>() {
            @Override
            public void onResponse(Call<ArrayList<Cinema>> call, Response<ArrayList<Cinema>> response) {
                cinemaNearMe.clear();

                progressing.pauseAnimation();
                progressing.setVisibility(View.INVISIBLE);

                if(response.body() != null && response.body().size() != 0){
                    cinemaNearMe.addAll(response.body());
                }

                cinemaNearMeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Cinema>> call, Throwable t) {
                progressing.pauseAnimation();
                progressing.setVisibility(View.INVISIBLE);
            }
        });
    }

    public ArrayList<Integer> getCinemaNearMe(){
        ArrayList<Integer> idCinemas = new ArrayList<>();

        if(cinemas != null && cinemas.size() != 0){
            int countCinema = cinemas.size();
            int distance = 0;
            double latcurrent = currentLocation.getLatitude();
            double lngcurrent = currentLocation.getLongitude();

            for (int i = 1; i < countCinema; i++) {
                Cinema cinema = cinemas.get(i);
                double latCinema = Double.parseDouble(cinema.getVido());
                double lngCinema = Double.parseDouble(cinema.getKinhdo());
                distance = (int) Util.distance(latcurrent, lngcurrent, latCinema, lngCinema );

                if(distance < 4){
                    idCinemas.add(cinema.getId());
                }
            }
        }

        return idCinemas;
    }

    public void requestPermisson(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity()
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_CODE_LOCALTION_PERMISSION);
        }else{
            updateBtnLocation();
        }
    }

    public void loadCinema() {
        Service service = RetrofitUtil.getService(getActivity());
        Call<List<Cinema>> call = service.getListCinema();

        call.enqueue(new Callback<List<Cinema>>() {
            @Override
            public void onResponse(Call<List<Cinema>> call, Response<List<Cinema>> response) {
                cinemas.clear();

                if (response.body() != null && response.body().size() != 0) {
                    cinemas.addAll(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<Cinema>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCinema();

        updateBtnLocation();
    }
}
