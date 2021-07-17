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

import com.ngolamquangtin.appdatvexemphim.DTO.Comment;
import com.ngolamquangtin.appdatvexemphim.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    Context context;
    ArrayList<Comment> comments;

    public CommentAdapter(Context context,  ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
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
        viewHolder.txtDatePost.setText(comment.getDatePost());

        Picasso.with(context).load(comment.getAvatar()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                viewHolder.imgaPersonal.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                viewHolder.imgaPersonal.setImageResource(R.drawable.ic_user);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        return view;
    }

    public class ViewHolder{
        TextView txtContentComment, txtNameCustomer, txtDatePost;
        ImageView imgaPersonal;
    }
}
