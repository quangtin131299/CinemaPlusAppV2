package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MovieNearYouAdapter extends BaseAdapter {

    Context context;
    ArrayList<Movie> arrMovie;

    public MovieNearYouAdapter(Context context, ArrayList<Movie> arrMovie) {
        this.context = context;
        this.arrMovie = arrMovie;
    }

    @Override
    public int getCount() {
        return arrMovie.size();
    }

    @Override
    public Object getItem(int position) {
        return arrMovie.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_datamovienearyou, null);
            viewHolder = new ViewHolder();
            viewHolder.txttenphim = convertView.findViewById(R.id.txttenphim);
            viewHolder.txtthoigian = convertView.findViewById(R.id.txtthoigian);
            viewHolder.imgphim = convertView.findViewById(R.id.imgphim);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Movie movie = (Movie) getItem(position);
        viewHolder.txttenphim.setText(movie.getTenphim());
        viewHolder.txtthoigian.setText(movie.getThoigian() + " phút");

        if(movie.getHinh() != null && !movie.getHinh().isEmpty()){
           Glide.with(context).asBitmap().load(movie.getHinh()).into(new CustomTarget<Bitmap>() {
               @Override
               public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                   viewHolder.imgphim.setBackground(new BitmapDrawable(context.getResources(), resource));
               }

               @Override
               public void onLoadCleared(@Nullable Drawable placeholder) {
               }

               @Override
               public void onLoadFailed(@Nullable Drawable errorDrawable) {
                   String valueColor = Integer.toHexString(context.getResources().getColor(R.color.colorBackround, null));
                   viewHolder.imgphim.setBackgroundColor(Color.parseColor("#"+valueColor));
                   viewHolder.imgphim.setImageResource(R.drawable.ic_clapperboard);
                   viewHolder.imgphim.setScaleType(ImageView.ScaleType.CENTER);
               }
           });
        }else{
            String valueColor = Integer.toHexString(context.getResources().getColor(R.color.colorBackround, null));
            viewHolder.imgphim.setBackgroundColor(Color.parseColor("#"+valueColor));
            viewHolder.imgphim.setImageResource(R.drawable.ic_clapperboard);
            viewHolder.imgphim.setScaleType(ImageView.ScaleType.CENTER);
        }

        return convertView;
    }

    public class ViewHolder{

        TextView txttenphim, txtthoigian;
        ImageView imgphim;

    }
}
