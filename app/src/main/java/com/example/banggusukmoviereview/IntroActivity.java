package com.example.banggusukmoviereview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {
    RelativeLayout layout;
    boolean isReviewExists;
    boolean isWishExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        layout = findViewById(R.id.intro_layout);

        checkLoadDB();

        //review, wishlist 중 table 있는 것만 load
        if(isReviewExists) {
            loadReviewDb();
        }
        if(isWishExists) {
            loadWishDb();
        }

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.intro_anim);
        layout.startAnimation(anim);

        anim.setAnimationListener(this);
        layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        handler.removeMessages(0);
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void loadReviewDb() {
        SQLiteDatabase db = openOrCreateDatabase("movieReview.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * from reviews", null);
        cursor.moveToFirst();
        //정보가 섞이는 것을 방지하기 위해서 arr를 초기화 하고 밑에서 다시 추가해준다
        Storage.reviewArr.clear();
        while (cursor.isAfterLast() == false) {
            int idx = cursor.getInt(0);
            String imagePath = cursor.getString(1);
            String title = cursor.getString(2);
            String date = cursor.getString(3);
            String genre = cursor.getString(4);
            String review = cursor.getString(5);
            float rating = cursor.getInt(6);
            Log.d("한다", "DB LOAD: "+idx+"/"+imagePath+"/"+title+"/"+date+"/"+genre+"/"+review+"/"+rating);
            Storage.reviewArr.add(new Review(idx, imagePath, title, date, genre, review, rating));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    public void loadWishDb() {
        SQLiteDatabase db = openOrCreateDatabase("movieReview.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * from wish", null);
        cursor.moveToFirst();
        Storage.wishArr.clear();
        while (cursor.isAfterLast() == false) {
            int idx = cursor.getInt(0);
            String title = cursor.getString(1);
            String memo = cursor.getString(2);
            String date = cursor.getString(3);
            Storage.wishArr.add(new WishList(idx, title, memo, date));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
    }

    public void checkLoadDB() {
        SQLiteDatabase db = openOrCreateDatabase("movieReview.db", MODE_PRIVATE, null);
        Cursor reviewExists = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name ='reviews'", null);
        Cursor wishExists = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name ='wish'", null);
        if(reviewExists.moveToFirst()) {
            for(;;) {
                //table에 아무것도 없으면 0 하나라도 있으면 1을 출력한다
                //0이 아니면 true로 table not found error 해결
                Log.d("한다", "table name : " + reviewExists.getString(0));
                if(!reviewExists.getString(0).equals("0")) {
                    isReviewExists = true;
                }
                if(!reviewExists.moveToNext())
                    break;
            }
        }
        if(wishExists.moveToFirst()) {
            for(;;) {
                //table에 아무것도 없으면 0 하나라도 있으면 1을 출력한다
                //0이 아니면 true로 table not found error 해결
                Log.d("한다", "table name : " + wishExists.getString(0));
                if(!wishExists.getString(0).equals("0")) {
                    isWishExists = true;
                }
                if(!reviewExists.moveToNext())
                    break;
            }
        }

        wishExists.close();
        reviewExists.close();
        db.close();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}