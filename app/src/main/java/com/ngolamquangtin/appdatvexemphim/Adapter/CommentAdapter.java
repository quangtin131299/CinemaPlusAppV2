package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ngolamquangtin.appdatvexemphim.DTO.Comment;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    Context context;
    ArrayList<Comment> comments;
    ImageView avartar;

    public CommentAdapter(Context context,  ArrayList<Comment> comments, ImageView avartar) {
        this.context = context;
        this.comments = comments;
        this.avartar = avartar;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
            viewHolder = new ViewHolder();
            viewHolder.imgaPersonal = view.findViewById(R.id.imgapersonal);
            viewHolder.txtContentComment = view.findViewById(R.id.txtcontentcomment);
            viewHolder.txtDatePost = view.findViewById(R.id.txtdatepost);
            viewHolder.txtNameCustomer = view.findViewById(R.id.txtnamecustomer);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        Comment comment = comments.get(i);
        viewHolder.txtContentComment.setText(comment.getContent());
        viewHolder.txtNameCustomer.setText(comment.getNameCustomer());
        viewHolder.txtDatePost.setText(Util.formatDateServerToClient(comment.getDatePost()));
//        viewHolder.imgaPersonal.setImageDrawable(avartar.getDrawable());

        if(comment.getAvatar() != null && !comment.getAvatar().isEmpty()){
            Glide.with(context).asBitmap().load(comment.getAvatar()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull  Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    viewHolder.imgaPersonal.setImageBitmap(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }

                @Override
                public void onLoadFailed(@Nullable  Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);

                    viewHolder.imgaPersonal.setImageResource(R.drawable.ic_user);
                }
            });
        }

        return view;
    }

    public class ViewHolder{
        TextView txtContentComment, txtNameCustomer, txtDatePost;
        ImageView imgaPersonal;
    }
}
