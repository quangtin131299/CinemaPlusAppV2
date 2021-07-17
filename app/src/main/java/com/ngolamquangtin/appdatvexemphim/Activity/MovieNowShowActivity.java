package com.ngolamquangtin.appdatvexemphim.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ngolamquangtin.appdatvexemphim.Adapter.MovieViewPagerAdapter;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentComingSoonCinema;
import com.ngolamquangtin.appdatvexemphim.Fragment.FragmentMovieNowCinema;
import com.ngolamquangtin.appdatvexemphim.R;

public class MovieNowShowActivity extends AppCompatActivity {

    int idCinema;
    MovieViewPagerAdapter viewPagerCinemaAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_now_show);

        addControls();

        setCinema();

        initTabAndViewPage();

    }

    public void initTabAndViewPage() {
        FragmentMovieNowCinema fragMovieNowCinema = new FragmentMovieNowCinema();
        Bundle bundleMovieNowShow = new Bundle();

        bundleMovieNowShow.putInt("ID_CINEMA", idCinema);

        fragMovieNowCinema.setArguments(bundleMovieNowShow);

        FragmentComingSoonCinema fragMovieComingSoonCinema = new FragmentComingSoonCinema();

        viewPagerCinemaAdapter.addFragment(fragMovieNowCinema, "Phim đang chiếu");
        viewPagerCinemaAdapter.addFragment(fragMovieComingSoonCinema, "Phim sắp chiếu ");

        viewPager.setAdapter(viewPagerCinemaAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void addControls() {
        viewPagerCinemaAdapter = new MovieViewPagerAdapter(getSupportFragmentManager());
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpagermovie);
    }

    public void setCinema(){
        Intent intentScreenDetailCinema = getIntent();

        if(intentScreenDetailCinema.hasExtra("ID_CINEMA")){
            idCinema = intentScreenDetailCinema.getIntExtra("ID_CINEMA", 0);
        }
    }

}