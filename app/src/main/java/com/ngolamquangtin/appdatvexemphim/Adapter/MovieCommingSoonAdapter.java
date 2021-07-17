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

import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class MovieCommingSoonAdapter extends BaseAdapter {
    Context context;
    ArrayList<Movie> movies;

    public MovieCommingSoonAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_datamovienearyou, null);
            viewHolder.txttenphim = convertView.findViewById(R.id.txttenphim);
            viewHolder.txtthoigian = convertView.findViewById(R.id.txtthoigian);
            viewHolder.imgphim = convertView.findViewById(R.id.imgphim);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Movie movie = movies.get(position);
        viewHolder.txttenphim.setText(movie.getTenphim());
        viewHolder.txtthoigian.setText(movie.getThoigian() + " min");

        if(!movie.getHinh().isEmpty()){
            Picasso.with(context).load(movie.getHinh()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    viewHolder.imgphim.setImageBitmap(null);
                    viewHolder.imgphim.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    String valueColor = Integer.toHexString(context.getResources().getColor(R.color.colorBackround, null));
                    viewHolder.imgphim.setBackgroundColor(Color.parseColor("#"+valueColor));
                    viewHolder.imgphim.setImageResource(R.drawable.ic_clapperboard);
                    viewHolder.imgphim.setScaleType(ImageView.ScaleType.CENTER);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

        return convertView;
    }

    public class ViewHolder {
        TextView txttenphim, txtthoigian;
        ImageView imgphim;
    }
}
