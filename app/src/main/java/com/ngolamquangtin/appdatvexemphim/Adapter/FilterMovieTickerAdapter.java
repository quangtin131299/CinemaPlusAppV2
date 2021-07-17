package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;

import java.util.ArrayList;

public class FilterMovieTickerAdapter extends BaseAdapter {

    Context context;
    ArrayList<Movie> movies;

    public FilterMovieTickerAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_filter, null);
            viewHolder = new ViewHolder();

            viewHolder.txtTitleMovie = view.findViewById(R.id.txttype);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        Movie movie = movies.get(i);

        viewHolder.txtTitleMovie.setText(movie.getTenphim());

        return view;
    }

    public class ViewHolder{
        TextView txtTitleMovie;
    }
}
