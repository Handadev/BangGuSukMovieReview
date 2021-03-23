package com.example.banggusukmoviereview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WishListUpdateActivity extends AppCompatActivity implements View.OnClickListener{
    Button saveBtn;
    EditText titleEt;
    EditText memoEt;
    TextView dateTv;

    String title;
    String memo;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_update);
        saveBtn = findViewById(R.id.save_btn);
        titleEt = findViewById(R.id.title_et);
        dateTv = findViewById(R.id.date_tv);
        memoEt = findViewById(R.id.memo_et);

        getData();
        setData();

        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        sendData();
    }

    public void getData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        memo = intent.getStringExtra("memo");
        date = intent.getStringExtra("date");
        Log.d("한다", "WISHUPDATEgetData: "+title+"/"+memo+"/"+date);

    }

    public void setData() {
        titleEt.setText(title);
        memoEt.setText(memo);
        dateTv.setText(date);
    }

    public void sendData() {
        Intent intent = new Intent(this, WishListActivity.class);
        title = titleEt.getText().toString();
        memo = memoEt.getText().toString();
        date = dateTv.getText().toString();
        updateDb();
        Storage.wishArr.set(WishListActivity.wishPosition, new WishList(title, memo, date));
        startActivity(intent);
        finish();
    }

    public void updateDb() {
        SQLiteDatabase db = openOrCreateDatabase("movieReview.db",MODE_PRIVATE,null);
        db.execSQL("UPDATE wish SET title ='"+title+"' , memo = '"+memo+"' , date = '"+date+"' WHERE idx ="+Storage.wishArr.get(WishListActivity.wishPosition).getIdx());
        Log.d("한다", "updateDb: "+title+"/"+date+"/idx : "+Storage.wishArr.get(WishListActivity.wishPosition).getIdx());
        db.close();
    }
}