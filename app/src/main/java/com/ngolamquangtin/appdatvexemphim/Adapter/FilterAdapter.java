package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ngolamquangtin.appdatvexemphim.DTO.MovieType;
import com.ngolamquangtin.appdatvexemphim.R;

import java.util.ArrayList;

public class FilterAdapter extends BaseAdapter {

    Context context;
    ArrayList<MovieType> types ;

    public FilterAdapter(Context context, ArrayList<MovieType> types) {
        this.context = context;
        this.types = types;
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public Object getItem(int i) {
        return types.get(i);
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

            viewHolder.txtType = view.findViewById(R.id.txtnamefilter);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        MovieType type = types.get(i);

        viewHolder.txtType.setText(type.getTenLoai());

        return view;
    }

    public class ViewHolder {
        TextView txtType;
    }
}
