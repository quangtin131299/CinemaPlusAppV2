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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ngolamquangtin.appdatvexemphim.Activity.DetalsMovieActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MoviePageAdapter extends RecyclerView.Adapter<MoviePageAdapter.ViewHolder> {

    Context context;
    ArrayList<Movie> movies;

    public MoviePageAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false));
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        if (movie.getHinh() != null && !movie.getHinh().isEmpty()) {
            Glide.with(context).asBitmap().load(movie.getHinh()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    holder.imgMovie.setBackground(new BitmapDrawable(context.getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }

                @Override
                public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                    String valueColor = Integer.toHexString(context.getResources().getColor(R.color.colorBackround, null));
                    holder.imgMovie.setBackgroundColor(Color.parseColor("#" + valueColor));
                    holder.imgMovie.setImageResource(R.drawable.ic_clapperboard);
                    holder.imgMovie.setScaleType(ImageView.ScaleType.CENTER);
                }
            });
        }


        holder.imgMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToScreenDetailMovie = new Intent(context, DetalsMovieActivity.class);

                intentToScreenDetailMovie.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intentToScreenDetailMovie.putExtra("MOVIE", movie);

                context.startActivity(intentToScreenDetailMovie);
            }
        });

//        new Boom(holder.imgMovie);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imgMovie;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imgMovie = itemView.findViewById(R.id.imgmovienowshow);
        }
    }

}
