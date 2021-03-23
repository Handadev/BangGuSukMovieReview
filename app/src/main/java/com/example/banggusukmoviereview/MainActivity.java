package com.example.banggusukmoviereview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    ImageView homeImg;
    ImageView boxOfficeImg;
    ImageView searchImg;
    ImageView writeReviewImg;
    ImageView wishListImg;

    GridView movieList;
    ReviewAdapter adapter;
    public static int reviewPosition;

    String imagePath;
    String title;
    String date;
    String genre;
    float rating;
    String review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = findViewById(R.id.movie_list);
        homeImg = findViewById(R.id.home);
        boxOfficeImg = findViewById(R.id.box_office_btn);
        searchImg = findViewById(R.id.search_movie_btn);
        writeReviewImg = findViewById(R.id.write_review_btn);
        wishListImg = findViewById(R.id.wish_list_btn);
        permission();


        adapter = new ReviewAdapter(this);
        movieList.setAdapter(adapter);


        movieList.setOnItemClickListener(this);
        movieList.setOnItemLongClickListener(this);
        writeReviewImg.setOnClickListener(this);
        boxOfficeImg.setOnClickListener(this);
        searchImg.setOnClickListener(this);
        wishListImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.write_review_btn) {
            Intent intent = new Intent(this, ReviewWriteActivity.class);
            startActivityForResult(intent, Storage.GET_REVIEW);

        } else if (v.getId() == R.id.box_office_btn) {
            Intent intent = new Intent(this, BoxOfficeActivity.class);
            startActivity(intent);

        } else if (v.getId() == R.id.search_movie_btn) {
            Intent intent = new Intent(this, SearchMovieActivity.class);
            startActivity(intent);

        } else if (v.getId() == R.id.wish_list_btn) {
            Intent intent = new Intent(this, WishListActivity.class);
            startActivity(intent);
        }
    }
    //리뷰 수정
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.reviewPosition = position;
        Intent resultActIntent = new Intent(this, ReviewResultActivity.class);
        resultActIntent.putExtra("imagePath", Storage.reviewArr.get(position).getImagePath());
        resultActIntent.putExtra("title", Storage.reviewArr.get(position).getTitle());
        resultActIntent.putExtra("date", Storage.reviewArr.get(position).getDate());
        resultActIntent.putExtra("genre", Storage.reviewArr.get(position).getGenre());
        resultActIntent.putExtra("rating", Storage.reviewArr.get(position).getRating());
        resultActIntent.putExtra("review", Storage.reviewArr.get(position).getReview());
        startActivity(resultActIntent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        this.reviewPosition = position;
        alertDialog();
        //true로 해야 onItemClick과 중복되지 않는다
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //WriteReview 에서 결과물 받아오기
        if(requestCode == Storage.GET_REVIEW && resultCode == RESULT_OK) {
            imagePath = data.getStringExtra("imagePath");
            title = data.getStringExtra("title");
            date = data.getStringExtra("date");
            genre = data.getStringExtra("genre");
            rating = data.getFloatExtra("rating", 0);
            review = data.getStringExtra("review");
            addDb();
            Storage.reviewArr.add(new Review(imagePath, title, date, genre, review, rating));
            adapter.notifyDataSetChanged();
            Log.d("한다", "MAIN getData: " + imagePath +"/"+ title +"/"+ date +"/"+ genre +"/"+ rating +"/"+ review);

        }
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내").setMessage("삭제하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delDb();
                Storage.reviewArr.remove(reviewPosition);
                adapter.notifyDataSetChanged();
                Log.d("한다", "MainAC AD reviewPosition: "+reviewPosition);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create();
        builder.show();
    }

    public void permission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .check();
    }


    public void addDb() {
        SQLiteDatabase db = openOrCreateDatabase("movieReview.db", MODE_PRIVATE, null);
        //member는 테이블의 이름이다. 정해주고 싶은 이름으로 만들어주면 된다
        db.execSQL("CREATE TABLE IF NOT EXISTS reviews(idx INTEGER PRIMARY KEY AUTOINCREMENT, " +
                //컬럼을 추가 삭제한 후 테이블을 적용하고 싶을 땐 앱을 삭제해야한다
                "imagePath TEXT," +
                "title TEXT," +
                "date TEXT," +
                "genre TEXT," +
                "review TEXT," +
                "rating INTEGER" +
                ")");
        String temp = "INSERT INTO reviews (imagePath, title, date, genre, review, rating) VALUES " +
                "('" + imagePath + "','" + title + "','" + date + "','" + genre + "','" + review + "','" + (int) rating + "')";
        db.execSQL(temp);
        db.close();
        //넣어줄 때 위에서 선언한 테이블의 이름과 같게 해주어야한다
        //db.execSQL("INSERT INTO member (eng, kor) VALUES ('"+engEt.getText().toString()+"','"+korEt.getText().toString()+"')");
    }


    public void delDb() {
        int idx = Storage.reviewArr.get(reviewPosition).getIdx();
        SQLiteDatabase db = openOrCreateDatabase("movieReview.db",MODE_PRIVATE,null);
        db.execSQL("DELETE FROM reviews WHERE idx ="+idx);
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}