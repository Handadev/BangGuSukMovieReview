package com.example.banggusukmoviereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;

public class ReviewResultActivity extends AppCompatActivity implements View.OnClickListener {
    Button updateBtn;
    ImageView imageView;
    TextView titleTv;
    TextView dateTv;
    TextView genreTv;
    TextView reviewTv;
    RatingBar ratingBar;

    String imagePath;
    String title;
    String date;
    String review;
    String genre;
    float rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_result);
        updateBtn = findViewById(R.id.update_btn);
        imageView = findViewById(R.id.imageView);
        titleTv = findViewById(R.id.title_tv);
        dateTv = findViewById(R.id.date_tv);
        genreTv = findViewById(R.id.genre_tv);
        reviewTv = findViewById(R.id.review_tv);
        ratingBar = findViewById(R.id.rating_bar);

        getData();
        setData();
        updateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        sendData();

    }

    private void getData() {
        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");
        title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        genre = intent.getStringExtra("genre");
        review = intent.getStringExtra("review");
        rating = intent.getFloatExtra("rating",0);
    }

    private void setData() {
        File files = new File(imagePath);
        if(files.exists()==true) {
            Uri uri = Uri.parse(imagePath);
            imageView.setBackgroundColor(Color.parseColor("#00ff0000"));
            imageView.setImageURI(uri);
        }
        titleTv.setText(title);
        dateTv.setText(date);
        genreTv.setText(genre);
        reviewTv.setText(review);
        ratingBar.setRating(rating);
    }

    private void sendData(){
        Intent intent = new Intent(this, ReviewUpdateActivity.class);
        intent.putExtra("imagePath", imagePath);
        intent.putExtra("title", titleTv.getText().toString());
        intent.putExtra("date", dateTv.getText().toString());
        intent.putExtra("genre", genreTv.getText().toString());
        intent.putExtra("rating", ratingBar.getRating());
        intent.putExtra("review", reviewTv.getText().toString());
        startActivity(intent);
        finish();
    }
}