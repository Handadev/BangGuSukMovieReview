package com.example.banggusukmoviereview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

public class SearchMovieAdapter extends ArrayAdapter {
    LayoutInflater inflater;

    class ListItemHolder {
        ImageView movieImgHolder;
        TextView titleTvHolder;
        TextView userRatingTvHolder;
        TextView directorTvHolder;
        TextView actorTvHolder;
        ConstraintLayout layoutHolder;
    }

    public SearchMovieAdapter( Context context) {
        super(context, R.layout.search_movie_list, Storage.searchMovieArr);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Storage.searchMovieArr.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return Storage.searchMovieArr.get(position);
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
            convertView = inflater.inflate(R.layout.search_movie_list, parent, false);
            viewHolder = new ListItemHolder();
            viewHolder.movieImgHolder = convertView.findViewById(R.id.movie_img);
            viewHolder.titleTvHolder = convertView.findViewById(R.id.title_tv);
            viewHolder.userRatingTvHolder = convertView.findViewById(R.id.rating_tv);
            viewHolder.directorTvHolder = convertView.findViewById(R.id.director_tv);
            viewHolder.actorTvHolder = convertView.findViewById(R.id.actor_tv);
            viewHolder.layoutHolder = convertView.findViewById(R.id.search_layout);

            convertView.setTag(viewHolder);
        } else {
            //처음 사용되는 것이 아니라면 이전의 것을 그대로 사용한다
            viewHolder = (ListItemHolder) convertView.getTag();
        }

        String imageLink = Storage.searchMovieArr.get(position).getImage();
        Glide.with(getContext())
                .load(imageLink)
                .into(viewHolder.movieImgHolder);

        Double userRating = Double.parseDouble(Storage.searchMovieArr.get(position).getUserRating());
        if (userRating >= 5.0) {
            viewHolder.userRatingTvHolder.setTextColor(Color.RED);
        } else {
            viewHolder.userRatingTvHolder.setTextColor(Color.BLUE);
        }

        viewHolder.titleTvHolder.setText(Storage.searchMovieArr.get(position).getTitle());
        viewHolder.userRatingTvHolder.setText(Storage.searchMovieArr.get(position).getUserRating());
        viewHolder.directorTvHolder.setText(Storage.searchMovieArr.get(position).getDirector());
        viewHolder.actorTvHolder.setText(Storage.searchMovieArr.get(position).getActor());

        return convertView;
    }

}
