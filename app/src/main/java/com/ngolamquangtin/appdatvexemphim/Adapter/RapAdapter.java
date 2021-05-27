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

import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class RapAdapter extends BaseAdapter {


    Context context;
    ArrayList<Cinema> cinemas;


    public RapAdapter(Context context, ArrayList<Cinema> cinemas) {
        this.context = context;
        this.cinemas = cinemas;
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
            if(parent.getId() == R.id.lvCinema) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_cinema, null);
                viewHolder.setTvdiachi((TextView) convertView.findViewById(R.id.tvdiachi));
            }else{
                convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_cinema, null);
            }
            viewHolder.setTvtenrap((TextView) convertView.findViewById(R.id.tvtenrap));
            viewHolder.setImageView((ImageView) convertView.findViewById(R.id.imgcinema));
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Cinema cinema = cinemas.get(position);
        viewHolder.getTvtenrap().setText(cinema.getTenrap());
        Picasso.with(context).load(cinema.getHinh()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                viewHolder.getImageView().setBackground(new BitmapDrawable(context.getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        return convertView;
    }


    public class ViewHolder {
        private TextView tvtenrap;
        private TextView tvdiachi;
        private ImageView imageView;

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
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
