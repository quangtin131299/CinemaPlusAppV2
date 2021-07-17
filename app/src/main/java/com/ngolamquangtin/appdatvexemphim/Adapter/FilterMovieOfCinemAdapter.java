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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FilterMovieOfCinemAdapter extends BaseAdapter {

    Context context;
    ArrayList<Cinema> cinemas;

    public FilterMovieOfCinemAdapter(Context context, ArrayList<Cinema> cinemas) {
        this.context = context;
        this.cinemas = cinemas;
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
        ViewHolder viewHolder;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_cinema, null);

            viewHolder = new ViewHolder();
            viewHolder.imagCinema = view.findViewById(R.id.imgcinema);
            viewHolder.txtNameCinema = view.findViewById(R.id.tvtenrap);

            view.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        Cinema cinema = cinemas.get(i);

        viewHolder.txtNameCinema.setText(cinema.getTenrap());

        if(cinema.getHinh() != null && !cinema.getHinh().isEmpty() && !cinema.getTenrap().equals("Tất cả rạp phim")){
            Glide.with(context).asBitmap().load(cinema.getHinh()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull  Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    viewHolder.imagCinema.setBackground(new BitmapDrawable(context.getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable  Drawable placeholder) {

                }
            });
        }

        return view;
    }

    public class ViewHolder{
        TextView txtNameCinema;
        ImageView imagCinema;
    }
}
