package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ngolamquangtin.appdatvexemphim.Adapter.MovieViewPagerAdapter;
import com.ngolamquangtin.appdatvexemphim.R;
import com.google.android.material.tabs.TabLayout;

public class FragmentMovie extends Fragment {


    TabLayout tabLayout;
    ViewPager viewPager;
    MovieViewPagerAdapter movieViewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        addControls(view);
        prepearViewPager();

        return view;
    }

    private void addControls(View view) {
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpagermovie);
        movieViewPagerAdapter = new MovieViewPagerAdapter(getFragmentManager());
    }


    public void prepearViewPager() {
        FragmentNearYou fragmentNearYou = new FragmentNearYou();
        FragmentCommingSoon fragmentCommingSoon = new FragmentCommingSoon();

        movieViewPagerAdapter.addFragment(fragmentNearYou, "NearYou");
        movieViewPagerAdapter.addFragment(fragmentCommingSoon, "Cooming Soon");

        viewPager.setAdapter(movieViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
