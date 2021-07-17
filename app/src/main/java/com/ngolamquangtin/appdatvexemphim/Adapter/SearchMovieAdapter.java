package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ngolamquangtin.appdatvexemphim.Activity.DetalsMovieActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;

import java.util.ArrayList;

public class SearchMovieAdapter extends BaseAdapter {

    Context context;
    ArrayList<Movie> movies;

    public SearchMovieAdapter(Context context, ArrayList<Movie> movies) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_search_movie, null);
            viewHolder = new ViewHolder();

            viewHolder.imgMovie = view.findViewById(R.id.imgMovie);
            viewHolder.txtdescription = view.findViewById(R.id.txtdescription);
            viewHolder.txttime = view.findViewById(R.id.txttime);
            viewHolder.txtnamemovie = view.findViewById(R.id.txtnamemovie);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        Movie movie = movies.get(i);

        viewHolder.txtnamemovie.setText(movie.getTenphim());
        viewHolder.txttime.setText(movie.getThoigian() + " phút");
        viewHolder.txtdescription.setText(movie.getMota());

        if(movie.getHinh() != null && !movie.getHinh().isEmpty()){
            Glide.with(context).asBitmap().load(movie.getHinh()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable  Transition<? super Bitmap> transition) {
                    viewHolder.imgMovie.setImageBitmap(null);
                    viewHolder.imgMovie.setBackground(new BitmapDrawable(context.getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable  Drawable placeholder) {

                }

                @Override
                public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                    String valueColor = Integer.toHexString(context.getResources().getColor(R.color.colorBackround, null));
                    viewHolder.imgMovie.setBackgroundColor(Color.parseColor("#"+valueColor));
                    viewHolder.imgMovie.setImageResource(R.drawable.ic_clapperboard);
                    viewHolder.imgMovie.setScaleType(ImageView.ScaleType.CENTER);
                }
            });
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToScreenDetailMovie = new Intent(context, DetalsMovieActivity.class);

                intentToScreenDetailMovie.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentToScreenDetailMovie.putExtra("MOVIE", movie);

                context.startActivity(intentToScreenDetailMovie);
            }
        });

        new Boom(view);

        return view;
    }

    public class ViewHolder{
        TextView  txtnamemovie, txttime,txtdescription;
        ImageView imgMovie;
    }
}
