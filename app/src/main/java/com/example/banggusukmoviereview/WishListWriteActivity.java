package com.example.banggusukmoviereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WishListWriteActivity extends AppCompatActivity implements View.OnClickListener{
    EditText titleEt;
    EditText memoEt;
    TextView dateTv;
    Button saveBtn;

    String title;
    String memo;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_write);
        titleEt = findViewById(R.id.title_et_write);
        memoEt = findViewById(R.id.memo_et_write);
        dateTv = findViewById(R.id.date_tv_write);
        saveBtn = findViewById(R.id.save_btn_write);

        if (Storage.isDataFormNetwork) {
            if (Storage.isBoxOfficeData) {
                getSetBoxOfficeData();
            } else {
                getSetSearchMovieData();
            }
        }

        setCurDate();

        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //BoxOffice or SearchMovie 의 데이터를 보냄
        if (Storage.isDataFormNetwork) {
            Intent intent = new Intent(this, WishListActivity.class);
            intent.putExtra("title", titleEt.getText().toString());
            intent.putExtra("memo", memoEt.getText().toString());
            intent.putExtra("date", dateTv.getText().toString());
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, WishListActivity.class);
            intent.putExtra("title", titleEt.getText().toString());
            intent.putExtra("memo", memoEt.getText().toString());
            intent.putExtra("date", dateTv.getText().toString());
            Log.d("한다", "WISHWRITE: "+title+"/"+memo+"/"+date);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    public void setCurDate () {
        SimpleDateFormat simpleData = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        dateTv.setText(simpleData.format(cal.getTime()));
    }

    public void getSetBoxOfficeData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("boxMovieName");
        titleEt.setText(title);
    }

    public void getSetSearchMovieData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("searchTitle");
        titleEt.setText(title);

    }


}