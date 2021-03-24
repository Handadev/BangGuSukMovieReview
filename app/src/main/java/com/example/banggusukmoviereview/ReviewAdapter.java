package com.example.banggusukmoviereview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.io.File;

public class ReviewAdapter extends ArrayAdapter {
    LayoutInflater inflater;


    class ListItemHolder {
        ImageView movieImgHolder;
        TextView titleTvHolder;
        RelativeLayout layoutHolder;
    }

    public ReviewAdapter(Activity context) {
        super(context, R.layout.review_list, Storage.reviewArr);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Storage.reviewArr.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return Storage.reviewArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ListItemHolder viewHolder;
        //null = 이 화면이 처음 사용되는 것인가? 만약 처음 사용하는 것이라면 뷰들을 생성해준다
        if (convertView == null) {
            //inflate는 xml파일을 자바로 변경해 처리하는 것
            convertView = inflater.inflate(R.layout.review_list, parent, false);
            viewHolder = new ListItemHolder();
            viewHolder.movieImgHolder = convertView.findViewById(R.id.img);
            viewHolder.titleTvHolder = convertView.findViewById(R.id.title_tv);
            viewHolder.layoutHolder = convertView.findViewById(R.id.review_layout);

            convertView.setTag(viewHolder);
        } else {
            //처음 사용되는 것이 아니라면 이전의 것을 그대로 사용한다
            viewHolder = (ListItemHolder) convertView.getTag();
        }

        String path = Storage.reviewArr.get(position).getImagePath();
        Glide.with(getContext())
                .load(path)
                .into(viewHolder.movieImgHolder);

        viewHolder.titleTvHolder.setText(Storage.reviewArr.get(position).getTitle());


        return convertView;

    }

}
