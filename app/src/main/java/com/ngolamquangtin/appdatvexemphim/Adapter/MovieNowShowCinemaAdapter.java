package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class MovieNowShowCinemaAdapter extends BaseAdapter {

    Context context;
    ArrayList<Movie> movies;

    public MovieNowShowCinemaAdapter(Context context, ArrayList<Movie> movies) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_movie_now_show_cinema, null);
            viewHolder = new ViewHolder();

            viewHolder.txtNameMovie = view.findViewById(R.id.txtnamemovie);
            viewHolder.txtDecription = view.findViewById(R.id.txtdecription);
            viewHolder.txtTime = view.findViewById(R.id.txttime);
            viewHolder.txtOpenDay = view.findViewById(R.id.txtopenday);
            viewHolder.imgMovie = view.findViewById(R.id.imgmovie);
            view.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        Movie movie = movies.get(i);

        viewHolder.txtNameMovie.setText(movie.getTenphim());
        viewHolder.txtOpenDay.setText(Util.formatDateServerToClient(movie.getNgayKhoiChieu()));
        viewHolder.txtDecription.setText(movie.getMota());
        viewHolder.txtTime.setText(movie.getThoigian() + " phút");

        if(movie.getHinh() != null && !movie.getHinh().isEmpty()){

            Picasso.with(context).load(movie.getHinh()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    viewHolder.imgMovie.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

        return view;
    }

    public class ViewHolder{
        ImageView imgMovie;
        TextView txtNameMovie, txtDecription,txtTime, txtOpenDay;
    }
}
