package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ngolamquangtin.appdatvexemphim.Adapter.MainAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentCinema;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentDialogNewMovie;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentFavourite;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentMovie;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentProfile;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentTicker;
import com.ngolamquangtin.appdatvexemphim.NetWorkChange;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    final int REQUEST_CODE_LOCALTION_PERMISSION = 1;

    Handler handler;
    NetWorkChange netWorkChange;
    MainAdapter mainAdapter;
    ViewPager viewPager;
    ArrayList<String> arrayTilte = new ArrayList<>();
    BottomNavigationView bottomNavigationViewHome;
    SharedPreferences sharedPreferences;
    Dialog dialogNewMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registerBroadCast();

        ReadStatusLogin();

        addControl();

        registerHandler();

        showDialogNewMovie();

        addEvent();

        prepearViewPager(viewPager, arrayTilte);
    }

    public void registerHandler() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull  Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        FragmentDialogNewMovie dialogNewMovie = new FragmentDialogNewMovie();

                        dialogNewMovie.setNewMovie((Movie) msg.obj);

                        dialogNewMovie.show(getSupportFragmentManager(), "DialogNewMovie");

                        deleteNewMovie();

                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void deleteNewMovie(){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("isNotifyNewMovie", "");

        editor.apply();
    }

    public void showDialogNewMovie() {
        String idMovie =sharedPreferences.getString("isNotifyNewMovie", "");

        if(!idMovie.isEmpty()){
            loadNewMovie(Integer.parseInt(idMovie));
        }
    }

    public void loadNewMovie(int idMovie){
        Service service = RetrofitUtil.getService(HomeActivity.this);
        Call<Movie> call = service.getNewMovie(idMovie);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(response.body() != null){
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = response.body();

                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });

    }

    private void registerBroadCast() {
        netWorkChange = new NetWorkChange();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChange,intentFilter);
    }

    private void ReadStatusLogin() {
        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);
    }

    private void addEvent() {
        bottomNavigationViewHome.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.itemmovie:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.itemticker:
                        viewPager.setCurrentItem(1);
//                        hiddenNotifyTicker();
                        break;
                    case R.id.itemcinema:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.itemfavourite:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.itemprofile:
                        viewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });

    }


    private void addControl() {

        dialogNewMovie = new Dialog(HomeActivity.this);
        viewPager = findViewById(R.id.viewpagerhome);
        //set để lưu lại các trang
        viewPager.setOffscreenPageLimit(4);
        bottomNavigationViewHome = findViewById(R.id.bottomnavigationhome);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationViewHome.setSelectedItemId(R.id.itemmovie);
                        break;
                    case 1:
                        bottomNavigationViewHome.setSelectedItemId(R.id.itemticker);
//                        hiddenNotifyTicker();
                        break;
                    case 2:
                        bottomNavigationViewHome.setSelectedItemId(R.id.itemcinema);
                        break;
                    case 3:
                        bottomNavigationViewHome.setSelectedItemId(R.id.itemfavourite);
                        break;
                    case 4:
                        bottomNavigationViewHome.setSelectedItemId(R.id.itemprofile);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void prepearViewPager(ViewPager viewPager, ArrayList<String> arrayTilte) {
        mainAdapter = new MainAdapter(getSupportFragmentManager());
        FragmentMovie fragmentMovie = new FragmentMovie();
        FragmentTicker fragmentTicker = new FragmentTicker();
        FragmentCinema fragmentCinema = new FragmentCinema();
        FragmentFavourite fragmentFavourite = new FragmentFavourite();
        FragmentProfile fragmentProfile = new FragmentProfile();


        mainAdapter.addFragment(fragmentMovie, "Phim");
        mainAdapter.addFragment(fragmentTicker, "Vé");
        mainAdapter.addFragment(fragmentCinema, "Rạp chiếu");
        mainAdapter.addFragment(fragmentFavourite, "Phim yêu thích");
        mainAdapter.addFragment(fragmentProfile, "Cá nhân");
        viewPager.setAdapter(mainAdapter);
    }

    public void setNotifyTicker(){
        Intent intentScreenPayment = getIntent();

        if(intentScreenPayment.hasExtra("NUMBER_NEW_TICKER")){

            int  numberNewTicker = intentScreenPayment.getIntExtra("NUMBER_NEW_TICKER", 0);

            BadgeDrawable badge = bottomNavigationViewHome.getOrCreateBadge(R.id.itemticker);
            badge.setVisible(true);
            badge.setNumber(numberNewTicker);
        }
    }

    private void hiddenNotifyTicker() {
        BadgeDrawable badge = bottomNavigationViewHome.getOrCreateBadge(R.id.itemticker);
        if(badge.isVisible()){
            badge.setVisible(false);
            badge.clearNumber();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            FragmentProfile fragProfile = (FragmentProfile) mainAdapter.getItem(4);
            Uri imgProfile = data.getData();

            fragProfile.updateUIImageProfile(imgProfile);
            fragProfile.uploadImageProfile(imgProfile);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCALTION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FragmentMovie fragmentMovie = (FragmentMovie) mainAdapter.getItem(0);
                fragmentMovie.updateBtnLocation();
            } else {
                Util.ShowToastErrorMessage(HomeActivity.this, "Quyền bị từ chối");
            }
        }
    }
}
