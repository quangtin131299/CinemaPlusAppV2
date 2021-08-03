package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ngolamquangtin.appdatvexemphim.DTO.PopCorn;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.ArrayList;


public class PopCornPayMentAdapter extends RecyclerView.Adapter<PopCornPayMentAdapter.ViewHolder> {

    Context context;
    ArrayList<PopCorn> popcorns;

    public PopCornPayMentAdapter(Context contex, ArrayList<PopCorn> popcorns) {
        this.context = contex;
        this.popcorns = popcorns;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_popcorn_payment, null));

        if(parent.getId() == R.id.rypopcorndetailbill){
            String color = Integer.toHexString(context.getResources().getColor(R.color.colorUnSelect, null));
            viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#"+color)); //cardView
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PopCorn popCorn = popcorns.get(position);

        holder.txtNamePopCorn.setText(popCorn.getComboName());
        holder.txtCount.setText("Số lượng: " + popCorn.getCount());
        holder.txtUnitPrice.setText(Util.formatAmount(popCorn.getUnitPrice()) + "đ");

        if(popCorn.getImage() != null && !popCorn.getImage().isEmpty()){
            Glide.with(context).asBitmap().load(popCorn.getImage()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull  Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    holder.imagePopCorn.setImageBitmap(null);
                    holder.imagePopCorn.setBackground(new BitmapDrawable(context.getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable  Drawable placeholder) {

                }

                @Override
                public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                    String valueColor = Integer.toHexString(context.getResources().getColor(R.color.colorBackround, null));
                    holder.imagePopCorn.setBackgroundColor(Color.parseColor("#"+valueColor));
                    holder.imagePopCorn.setImageResource(R.drawable.ic_popcorn);
                    holder.imagePopCorn.setScaleType(ImageView.ScaleType.CENTER);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return popcorns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView txtNamePopCorn, txtCount, txtUnitPrice;
        ImageView imagePopCorn;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.carditem);
            imagePopCorn = itemView.findViewById(R.id.imgcinema);
            txtNamePopCorn = itemView.findViewById(R.id.txtnamepopcorn);
            txtCount = itemView.findViewById(R.id.txtcount);
            txtUnitPrice = itemView.findViewById(R.id.txtunitprice);
        }
    }
}
