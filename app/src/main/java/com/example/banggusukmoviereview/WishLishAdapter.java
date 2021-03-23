package com.example.banggusukmoviereview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;

public class WishLishAdapter extends ArrayAdapter {
    LayoutInflater inflater;

    class ListItemHolder {
        TextView titleTvHolder;
        TextView memoTvHolder;
        TextView dateTvHolder;
        ConstraintLayout constraintLayout;
    }

    public WishLishAdapter(Context context) {
        super(context, R.layout.wish_list, Storage.wishArr);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Storage.wishArr.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return Storage.wishArr.get(position);
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
            convertView = inflater.inflate(R.layout.wish_list, parent, false);
            viewHolder = new ListItemHolder();
            viewHolder.titleTvHolder = convertView.findViewById(R.id.title_tv);
            viewHolder.memoTvHolder = convertView.findViewById(R.id.memo_tv);
            viewHolder.dateTvHolder = convertView.findViewById(R.id.date_tv);
            viewHolder.constraintLayout = convertView.findViewById(R.id.with_list_layout);

            convertView.setTag(viewHolder);
        } else {
            //처음 사용되는 것이 아니라면 이전의 것을 그대로 사용한다
            viewHolder = (ListItemHolder) convertView.getTag();
        }


        viewHolder.titleTvHolder.setText(Storage.wishArr.get(position).getTitle());
        viewHolder.memoTvHolder.setText(Storage.wishArr.get(position).getMemo());
        viewHolder.dateTvHolder.setText(Storage.wishArr.get(position).getDate());


        return convertView;

    }


}
