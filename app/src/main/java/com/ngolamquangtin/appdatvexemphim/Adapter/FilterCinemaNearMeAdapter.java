package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Image;
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
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FilterCinemaNearMeAdapter extends BaseAdapter {

    Location currentLocation;
    Context context;
    ArrayList<Cinema> cinemas;

    public FilterCinemaNearMeAdapter(Context context, ArrayList<Cinema> cinemas, Location currentLocation) {
        this.context = context;
        this.cinemas = cinemas;
        this.currentLocation = currentLocation;
    }

    @Override
    public int getCount() {
        return cinemas.size();
    }

    @Override
    public Object getItem(int i) {
        return cinemas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_cinema, null);
            viewHolder = new ViewHolder();

            viewHolder.imgCinema = view.findViewById(R.id.imgcinema);
            viewHolder.txtNameCinema = view.findViewById(R.id.tvtenrap);
            viewHolder.txtDistance = view.findViewById(R.id.tvdiachi);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        Cinema cinema = cinemas.get(i);

        viewHolder.txtNameCinema.setText(cinema.getTenrap());

        double latCurrent = currentLocation.getLatitude();
        double lngCurrent = currentLocation.getLongitude();
        double distance = Util.distance(Double.parseDouble(cinema.getVido()), Double.parseDouble(cinema.getKinhdo()), latCurrent, lngCurrent);
        DecimalFormat decimalformat = new DecimalFormat("#.##");

        viewHolder.txtDistance.setVisibility(View.VISIBLE);
        viewHolder.txtDistance.setText(decimalformat.format(distance) + " km");

        if(cinema.getHinh() != null && !cinema.getHinh().isEmpty()){
            ViewHolder finalViewHolder = viewHolder;

            Glide.with(context).asBitmap().load(cinema.getHinh()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull  Bitmap resource, @Nullable  Transition<? super Bitmap> transition) {
                    finalViewHolder.imgCinema.setBackground(new BitmapDrawable(context.getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable  Drawable placeholder) {

                }
            });
        }

        return view;
    }

    public class ViewHolder{
        TextView txtNameCinema, txtDistance;
        ImageView imgCinema;
    }
}
