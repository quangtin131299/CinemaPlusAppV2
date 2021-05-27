package com.ngolamquangtin.appdatvexemphim.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends FragmentPagerAdapter {

    ArrayList<String> arrtitle = new ArrayList<>();
    List<Fragment>  lstfragment = new ArrayList<>();

    public void addFragment(Fragment fragment, String title){
        arrtitle.add(title);
        lstfragment.add(fragment);
    }

    public MainAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return lstfragment.get(position);
    }

    @Override
    public int getCount() {
        return lstfragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  arrtitle.get(position);
    }
}
