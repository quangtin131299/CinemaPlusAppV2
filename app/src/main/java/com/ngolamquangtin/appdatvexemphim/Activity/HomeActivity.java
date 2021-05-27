package com.ngolamquangtin.appdatvexemphim.Activity;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ngolamquangtin.appdatvexemphim.Adapter.MainAdapter;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentCinema;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentFavourite;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentMovie;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentProfile;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentTicker;
import com.ngolamquangtin.appdatvexemphim.LocationChange;
import com.ngolamquangtin.appdatvexemphim.NetWorkChange;
import com.ngolamquangtin.appdatvexemphim.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    NetWorkChange netWorkChange;


    ViewPager viewPager;
    ArrayList<String> arrayTilte = new ArrayList<>();
    BottomNavigationView bottomNavigationViewHome;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registerBroadCast();

        ReadStatusLogin();
        addControl();
        addEvent();
        prepearViewPager(viewPager, arrayTilte);
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
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        FragmentMovie fragmentMovie = new FragmentMovie();
        FragmentTicker fragmentTicker = new FragmentTicker();
        FragmentCinema fragmentCinema = new FragmentCinema();
        FragmentFavourite fragmentFavourite = new FragmentFavourite();
        FragmentProfile fragmentProfile = new FragmentProfile();


        mainAdapter.addFragment(fragmentMovie, "Movie");
        mainAdapter.addFragment(fragmentTicker, "Ticker");
        mainAdapter.addFragment(fragmentCinema, "Cinema");
        mainAdapter.addFragment(fragmentFavourite, "Favourite");
        mainAdapter.addFragment(fragmentProfile, "Profile");
        viewPager.setAdapter(mainAdapter);

    }

}
