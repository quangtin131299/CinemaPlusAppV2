package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.ngolamquangtin.appdatvexemphim.DTO.PopCorn;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PopCornAdapter extends BaseAdapter {

    Context context;

    ArrayList<PopCorn> popCorns;

    public PopCornAdapter(Context context, ArrayList<PopCorn> popCorns) {
        this.context = context;
        this.popCorns = popCorns;
    }

    @Override
    public int getCount() {
        return popCorns.size();
    }

    @Override
    public Object getItem(int i) {
        return popCorns.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_popcorn, null);
            viewHolder = new ViewHolder();
            viewHolder.imgpopcorn = view.findViewById(R.id.imgpopcorn);
            viewHolder.txtpopcorn = view.findViewById(R.id.txtpopcorn);
            viewHolder.txtDescriptionPopCorn = view.findViewById(R.id.txtdescriptionpopcorn);
            viewHolder.txtPrice = view.findViewById(R.id.txtprice);
            viewHolder.txtIncrease = view.findViewById(R.id.btnincrease);
            viewHolder.txtReduce = view.findViewById(R.id.btnreduce);
            viewHolder.txtcount = view.findViewById(R.id.txtcount);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        PopCorn popCorn = popCorns.get(i);

        viewHolder.txtpopcorn.setText(popCorn.getComboName());
        viewHolder.txtDescriptionPopCorn.setText(popCorn.getDescription());
        viewHolder.txtPrice.setText(Util.formatAmount(popCorn.getUnitPrice()) + "đ");

        if(popCorn.getImage() != null && !popCorn.getImage().isEmpty()){
            Glide.with(context).asBitmap().load(popCorn.getImage()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    viewHolder.imgpopcorn.setImageBitmap(null);
                    viewHolder.imgpopcorn.setBackground(new BitmapDrawable(context.getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

                }
            });
        }

        viewHolder.txtIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(viewHolder.txtcount.getText().toString());
                count++;

                viewHolder.txtcount.setText(count+"");
            }
        });

        viewHolder.txtReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(viewHolder.txtcount.getText().toString());

                if(count != 0) {
                    count--;
                }

                viewHolder.txtcount.setText(count+"");
            }
        });

        new Boom(viewHolder.txtIncrease);
        new Boom(viewHolder.txtReduce);

        return view;
    }

    public class ViewHolder{
        TextView txtpopcorn,txtDescriptionPopCorn,txtPrice, txtIncrease, txtReduce, txtcount;
        ImageView imgpopcorn;
    }
}
