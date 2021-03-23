package com.example.banggusukmoviereview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class BoxOffcieAdapter extends ArrayAdapter {
    LayoutInflater inflater;

    class ListItemHolder {
        TextView rankTvHolder;
        TextView movieNameTvHolder;
        TextView dateTvHolder;
        RelativeLayout layoutHolder;
    }

    public BoxOffcieAdapter(Activity context) {
        super(context, R.layout.box_office_list, Storage.boxOfficeArr);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Storage.boxOfficeArr.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return Storage.boxOfficeArr.get(position);
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
            convertView = inflater.inflate(R.layout.box_office_list, parent, false);
            viewHolder = new ListItemHolder();
            viewHolder.rankTvHolder = convertView.findViewById(R.id.rank_tv);
            viewHolder.movieNameTvHolder = convertView.findViewById(R.id.movie_name_tv);
            viewHolder.dateTvHolder = convertView.findViewById(R.id.date_tv);
            viewHolder.layoutHolder = convertView.findViewById(R.id.box_office_layout);

            convertView.setTag(viewHolder);
        } else {
            //처음 사용되는 것이 아니라면 이전의 것을 그대로 사용한다
            viewHolder = (ListItemHolder) convertView.getTag();
        }

        viewHolder.rankTvHolder.setText(Storage.boxOfficeArr.get(position).getRank());
        viewHolder.movieNameTvHolder.setText(Storage.boxOfficeArr.get(position).getMovieName());
        viewHolder.dateTvHolder.setText(Storage.boxOfficeArr.get(position).getOpenDate());


        return convertView;
    }

}
