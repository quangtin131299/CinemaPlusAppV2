package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngolamquangtin.appdatvexemphim.Activity.DetalsMovieActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.MovieFavourite;
import com.ngolamquangtin.appdatvexemphim.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class MovieFavouriteAdapter extends BaseAdapter {

    Context context;
    ArrayList<MovieFavourite> movieFavourites;

    public MovieFavouriteAdapter(Context context, ArrayList<MovieFavourite> movieFavourites) {
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

        final MovieFavourite movieFavourite = movieFavourites.get(position);
        viewHolder.getTxtphimfavourite().setText(movieFavourite.getTenphim());
        viewHolder.getTxtthoigianmoviefavourite().setText(movieFavourite.getThoigian() + " min");
        viewHolder.getTxtmotamovivefavourite().setText(movieFavourite.getMota());
        Picasso.with(context).load(movieFavourite.getHinh()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                viewHolder.getImgmoviefavourite().setBackground(new BitmapDrawable(context.getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        viewHolder.btndatve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetalsMovieActivity.class);
                i.putExtra("ID_MOVIE", movieFavourite.getId());
                context.startActivity(i);

            }
        });

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
