package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ngolamquangtin.appdatvexemphim.Activity.DetailTickerActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

public class FragmentDialogNewMovie extends DialogFragment {

    LottieAnimationView processing;
    Movie newMovie;
    RoundedImageView imgMovie;
    ImageButton btnClose;

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_movie, container, false);
        
        addContronls(view);

        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);

        loadData();
        
        return view;
    }


    public void setNewMovie(Movie newMovie) {
        this.newMovie = newMovie;
    }

    public void loadData() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        imgMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToScreenDetailMovie = new Intent(getActivity(), DetailTickerActivity.class);

                intentToScreenDetailMovie.putExtra("MOVIE", newMovie);

                startActivity(intentToScreenDetailMovie);
            }
        });

        if(newMovie != null && newMovie.getHinh() != null && !newMovie.getHinh().isEmpty()){
            Glide.with(getActivity()).asBitmap().load(newMovie.getHinh()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    processing.setVisibility(View.INVISIBLE);
                    processing.pauseAnimation();

                    imgMovie.setImageBitmap(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }

                @Override
                public void onLoadStarted(@Nullable  Drawable placeholder) {
                    super.onLoadStarted(placeholder);

                    processing.setVisibility(View.VISIBLE);
                    processing.playAnimation();
                }
            });
        }

        new Boom(btnClose);
    }

    private void addContronls(View view) {
        processing = view.findViewById(R.id.processing);
        imgMovie  = view.findViewById(R.id.imgMovie);
        btnClose = view.findViewById(R.id.btnClose);
    }
}
