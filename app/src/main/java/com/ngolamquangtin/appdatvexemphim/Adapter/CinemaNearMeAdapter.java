package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ngolamquangtin.appdatvexemphim.Activity.DetailCinemaActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CinemaNearMeAdapter extends RecyclerView.Adapter<CinemaNearMeAdapter.ViewHolder> {

    Location currentLocation;
    Context context;
    ArrayList<Cinema> cinemas;

    public CinemaNearMeAdapter(Context context, ArrayList<Cinema> cinemas) {
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
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Cinema cinema = cinemas.get(position);

        if(cinema.getHinh() != null && !cinema.getHinh().isEmpty()){
            Glide.with(context).asBitmap().load(cinema.getHinh()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull  Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    holder.imgCnema.setBackground(new BitmapDrawable(context.getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable  Drawable placeholder) {

                }

                @Override
                public void onLoadFailed(@Nullable  Drawable errorDrawable) {
                    Log.d("Glide", cinemas.get(position).getHinh());
                }
            });
        }
        String valueColor = Integer.toHexString(context.getResources().getColor(R.color.colorUnSelect, null));
        holder.cardcinema.setCardBackgroundColor(Color.parseColor("#" + valueColor));
        holder.txtNameCinema.setText(cinema.getTenrap());
        holder.txtDistance.setVisibility(View.VISIBLE);
        double distance = Util.distance(Double.parseDouble(cinema.getVido()), Double.parseDouble(cinema.getKinhdo()), currentLocation.getLatitude(), currentLocation.getLongitude());

        DecimalFormat decimalformat = new DecimalFormat("#.##");

        holder.txtDistance.setText(decimalformat.format(distance) + " km");
    }

    @Override
    public int getItemCount() {
        return cinemas.size();
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardcinema;
        RoundedImageView imgCnema;
        TextView txtNameCinema, txtDistance;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            cardcinema = itemView.findViewById(R.id.cardcinema);
            imgCnema = itemView.findViewById(R.id.imgcinema);
            txtNameCinema = itemView.findViewById(R.id.tvtenrap);
            txtDistance = itemView.findViewById(R.id.tvdiachi);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cinema cinema = cinemas.get( getPosition());

                    if(cinema != null){
                        Intent intentToScreenDetailCinema = new Intent(context, DetailCinemaActivity.class);

                        intentToScreenDetailCinema.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        intentToScreenDetailCinema.putExtra("CINEMA", cinema);

                        context.startActivity(intentToScreenDetailCinema);
                    }
                }
            });

            new Boom(itemView);
        }
    }
}
