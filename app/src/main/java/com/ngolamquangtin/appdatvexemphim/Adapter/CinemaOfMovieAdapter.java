package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ngolamquangtin.appdatvexemphim.Activity.DetailCinemaActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class CinemaOfMovieAdapter extends RecyclerView.Adapter<CinemaOfMovieAdapter.ViewHolder> {

    Context context;
    ArrayList<Cinema> cinemas;

    public CinemaOfMovieAdapter(Context context, ArrayList<Cinema> cinemas) {
        this.context = context;
        this.cinemas = cinemas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cinema_detail_movie, null));
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return cinemas.size();
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        Cinema cinema = cinemas.get(position);

        holder.txtNameCinema.setText(cinema.getTenrap());

        if(cinema.getHinh() != null && !cinema.getHinh().isEmpty()){
            Glide.with(context).asBitmap().load(cinema.getHinh()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull  Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    holder.imgCinema.setBackground(new BitmapDrawable(context.getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtNameCinema;
        RoundedImageView imgCinema;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameCinema = itemView.findViewById(R.id.tvtenrap);
            imgCinema = itemView.findViewById(R.id.imgcinema);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cinema cinemaSelect = cinemas.get(getAdapterPosition());

                    if(cinemaSelect != null){
                        Intent intentToScreenDetailCinema = new Intent(context, DetailCinemaActivity.class);

                        intentToScreenDetailCinema.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        intentToScreenDetailCinema.putExtra("CINEMA", cinemaSelect);

                        context.startActivity(intentToScreenDetailCinema);
                    }

                }
            });

            new Boom(itemView);
        }
    }
}
