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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ngolamquangtin.appdatvexemphim.Activity.ChooseSessionActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.DetalsMovieActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.DTO.MovieFavourite;
import com.ngolamquangtin.appdatvexemphim.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class MovieFavouriteAdapter extends BaseAdapter {

    Context context;
    ArrayList<Movie> movieFavourites;

    public MovieFavouriteAdapter(Context context, ArrayList<Movie> movieFavourites) {
        this.context = context;
        this.movieFavourites = movieFavourites;
    }

    @Override
    public int getCount() {
        return movieFavourites.size();
    }

    @Override
    public Object getItem(int position) {
        return movieFavourites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_favourite, null);
            viewHolder = new ViewHolder();
            viewHolder.setImgmoviefavourite((ImageView) convertView.findViewById(R.id.imgmoviefavourite));
            viewHolder.setTxtphimfavourite((TextView) convertView.findViewById(R.id.txtphimfavourite));
            viewHolder.setTxtthoigianmoviefavourite((TextView) convertView.findViewById(R.id.txtthoigianmoviefavourite));
            viewHolder.setTxtmotamovivefavourite((TextView) convertView.findViewById(R.id.txtmotamovivefavourite));
            viewHolder.setBtndatve((Button) convertView.findViewById(R.id.btndatve));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Movie movieFavourite = movieFavourites.get(position);
        viewHolder.getTxtphimfavourite().setText(movieFavourite.getTenphim());
        viewHolder.getTxtthoigianmoviefavourite().setText(movieFavourite.getThoigian() + " phút");
        viewHolder.getTxtmotamovivefavourite().setText(movieFavourite.getMota());

        if( movieFavourite.getHinh() != null && !movieFavourite.getHinh().isEmpty()){
            Glide.with(context).asBitmap().load(movieFavourite.getHinh()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                    viewHolder.imgmoviefavourite.setBackground(new BitmapDrawable(context.getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    String valueColor = Integer.toHexString(context.getResources().getColor(R.color.colorBackround, null));
                    viewHolder.imgmoviefavourite.setBackgroundColor(Color.parseColor("#"+valueColor));
                    viewHolder.imgmoviefavourite.setImageResource(R.drawable.ic_clapperboard);
                    viewHolder.imgmoviefavourite.setScaleType(ImageView.ScaleType.CENTER);
                }
            });

        }else{
            String valueColor = Integer.toHexString(context.getResources().getColor(R.color.colorBackround, null));
            viewHolder.getImgmoviefavourite().setBackgroundColor(Color.parseColor("#"+valueColor));
            viewHolder.getImgmoviefavourite().setImageResource(R.drawable.ic_clapperboard);
            viewHolder.getImgmoviefavourite().setScaleType(ImageView.ScaleType.CENTER);
        }

        viewHolder.btndatve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToScreenChoose = new Intent(context, ChooseSessionActivity.class);

                intentToScreenChoose.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intentToScreenChoose.putExtra("ID_MOVIE", movieFavourite.getId());
                intentToScreenChoose.putExtra("TEN_PHIM", movieFavourite.getTenphim());
                intentToScreenChoose.putExtra("IMAGE_MOVIE", movieFavourite.getHinh());
                intentToScreenChoose.putExtra("TIME_MOVIE", movieFavourite.getThoigian());

                context.startActivity(intentToScreenChoose);

            }
        });

        new Boom(viewHolder.btndatve);

        return convertView;
    }


    public class ViewHolder {
        private ImageView imgmoviefavourite;
        private TextView txtphimfavourite, txtthoigianmoviefavourite, txtmotamovivefavourite;
        private Button btndatve;

        public Button getBtndatve() {
            return btndatve;
        }

        public void setBtndatve(Button btndatve) {
            this.btndatve = btndatve;
        }

        public ImageView getImgmoviefavourite() {
            return imgmoviefavourite;
        }

        public void setImgmoviefavourite(ImageView imgmoviefavourite) {
            this.imgmoviefavourite = imgmoviefavourite;
        }

        public TextView getTxtphimfavourite() {
            return txtphimfavourite;
        }

        public void setTxtphimfavourite(TextView txtphimfavourite) {
            this.txtphimfavourite = txtphimfavourite;
        }

        public TextView getTxtthoigianmoviefavourite() {
            return txtthoigianmoviefavourite;
        }

        public void setTxtthoigianmoviefavourite(TextView txtthoigianmoviefavourite) {
            this.txtthoigianmoviefavourite = txtthoigianmoviefavourite;
        }

        public TextView getTxtmotamovivefavourite() {
            return txtmotamovivefavourite;
        }

        public void setTxtmotamovivefavourite(TextView txtmotamovivefavourite) {
            this.txtmotamovivefavourite = txtmotamovivefavourite;
        }
    }
}
