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
import com.makeramen.roundedimageview.RoundedImageView;
import com.ngolamquangtin.appdatvexemphim.Activity.ChooseSessionActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.DetailCinemaActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class CinemaAdapter extends BaseAdapter {

    Context context;
    ArrayList<Cinema> cinemas;
    int isFragmentCinema;
    boolean isActivityCinema;
    Intent intentActivityCinema;


    public CinemaAdapter(Context context, ArrayList<Cinema> cinemas, int isFragmentCinema) {
        this.context = context;
        this.cinemas = cinemas;
        this.isFragmentCinema = isFragmentCinema;
    }

    public void setIsActivityCinema(boolean isActivityCinema) {
        this.isActivityCinema = isActivityCinema;
    }

    public void setIntent(Intent intenActivityCinema){
        this.intentActivityCinema = intenActivityCinema;
    }

    @Override
    public int getCount() {
        return cinemas.size();
    }

    @Override
    public Object getItem(int position) {
        return cinemas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (parent.getId() == R.id.lvCinema) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_cinema, null);
                viewHolder.setTvdiachi((TextView) convertView.findViewById(R.id.tvdiachi));

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isFragmentCinema == 1 && isActivityCinema == false) {
                            Intent intentToScreentDetailCinema = new Intent(context, DetailCinemaActivity.class);

                            intentToScreentDetailCinema.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            intentToScreentDetailCinema.putExtra("CINEMA", (Cinema)getItem(position));

                            context.startActivity(intentToScreentDetailCinema);
                        } else if(isFragmentCinema == 1 && isActivityCinema){
                            Intent intentToScreentChoose = new Intent(context, ChooseSessionActivity.class);

                            intentToScreentChoose.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            intentToScreentChoose.putExtra("ID_CINEMA", ((Cinema)getItem(position)).getId());
                            intentToScreentChoose.putExtra("ID_MOVIE", intentActivityCinema.getSerializableExtra("ID_MOVIE"));
                            intentToScreentChoose.putExtra("IMAGE_MOVIE", intentActivityCinema.getStringExtra("IMAGE_MOVIE"));
                            intentToScreentChoose.putExtra("TEN_PHIM", intentActivityCinema.getStringExtra("TEN_PHIM"));
                            intentToScreentChoose.putExtra("SCREEN_CINEMA", 1);

                            context.startActivity(intentToScreentChoose);
                        }
                    }
                });

                new Boom(convertView);

            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_cinema, null);
            }

            viewHolder.setTvtenrap((TextView) convertView.findViewById(R.id.tvtenrap));
            viewHolder.setImageView((RoundedImageView) convertView.findViewById(R.id.imgcinema));
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Cinema cinema = cinemas.get(position);

        viewHolder.getTvtenrap().setText(cinema.getTenrap());

        if (cinema.getHinh() != null && !cinema.getHinh().isEmpty()) {
            Glide.with(context).asBitmap().load(cinema.getHinh()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                    viewHolder.getImageView().setBackground(new BitmapDrawable(context.getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }

            });
        }

        return convertView;
    }

    public class ViewHolder {
        private TextView tvtenrap;
        private TextView tvdiachi;
        private RoundedImageView imageView;

        public RoundedImageView getImageView() {
            return imageView;
        }

        public void setImageView(RoundedImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getTvtenrap() {
            return tvtenrap;
        }

        public void setTvtenrap(TextView tvtenrap) {
            this.tvtenrap = tvtenrap;
        }

        public TextView getTvdiachi() {
            return tvdiachi;
        }

        public void setTvdiachi(TextView tvdiachi) {
            this.tvdiachi = tvdiachi;
        }
    }

}
