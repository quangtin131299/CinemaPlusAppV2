package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ngolamquangtin.appdatvexemphim.DTO.Country;
import com.ngolamquangtin.appdatvexemphim.R;

import java.util.ArrayList;

public class CountryAdapter extends BaseAdapter {

    Context context;
    ArrayList<Country> countries;

    public CountryAdapter(Context context, ArrayList<Country> countries) {
        this.context = context;
        this.countries = countries;
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int i) {
        return countries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_filter_2, null);
            viewHolder = new ViewHolder();
            viewHolder.txtTitle = view.findViewById(R.id.txtnamefilter);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        Country country = countries.get(i);

        viewHolder.txtTitle.setText(country.getNameCountry());

        return view;
    }

    public class ViewHolder{
        TextView txtTitle;
    }
}
