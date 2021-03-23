package com.example.banggusukmoviereview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WishListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ListView wishList;
    WishLishAdapter adapter;
    FloatingActionButton writeWishBtn;

    public static int wishPosition;

    String title;
    String memo;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        wishList = findViewById(R.id.wish_list);
        writeWishBtn = findViewById(R.id.write_wish_list_btn);

        adapter = new WishLishAdapter(this);
        wishList.setAdapter(adapter);

        writeWishBtn.setOnClickListener(this);
        wishList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WishListWriteActivity.class);
        startActivityForResult(intent, Storage.GET_WISH_LIST);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.wishPosition = position;
        alertDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Storage.GET_WISH_LIST && resultCode == RESULT_OK) {
            title = data.getStringExtra("title");
            memo = data.getStringExtra("memo");
            date = data.getStringExtra("date");
            addDb();
            Storage.wishArr.add(new WishList(title, memo, date));
            Log.d("한다", "WISHACTIVITYonActivityResult: "+title+"/"+memo+"/"+date);
            adapter.notifyDataSetChanged();

        }
    }

    public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내").setMessage("어떤 행동을 하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delDb();
                Storage.wishArr.remove(wishPosition);
                adapter.notifyDataSetChanged();
                Log.d("한다", "WishAC AD reviewPosition: "+wishPosition);
            }
        });
        builder.setNegativeButton("수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(WishListActivity.this, WishListUpdateActivity.class);
                intent.putExtra("title", Storage.wishArr.get(wishPosition).getTitle());
                intent.putExtra("memo", Storage.wishArr.get(wishPosition).getMemo());
                intent.putExtra("date", Storage.wishArr.get(wishPosition).getDate());
                startActivity(intent);
            }
        });

        builder.create();
        builder.show();
    }

    public void addDb() {
        SQLiteDatabase db = openOrCreateDatabase("movieReview.db", MODE_PRIVATE, null);
        //member는 테이블의 이름이다. 정해주고 싶은 이름으로 만들어주면 된다
        db.execSQL("CREATE TABLE IF NOT EXISTS wish(idx INTEGER PRIMARY KEY AUTOINCREMENT, " +
                //컬럼을 추가 삭제한 후 테이블을 적용하고 싶을 땐 앱을 삭제해야한다
                "title TEXT," +
                "memo TEXT," +
                "date TEXT" +
                ")");
        String temp = "INSERT INTO wish (title, memo, date) VALUES " +
                "('" + title + "','" + memo + "','" + date + "')";
        db.execSQL(temp);
        db.close();
        //넣어줄 때 위에서 선언한 테이블의 이름과 같게 해주어야한다
        //db.execSQL("INSERT INTO member (eng, kor) VALUES ('"+engEt.getText().toString()+"','"+korEt.getText().toString()+"')");
    }

    public void delDb() {
        int idx = Storage.wishArr.get(wishPosition).getIdx();
        SQLiteDatabase db = openOrCreateDatabase("movieReview.db",MODE_PRIVATE,null);
        db.execSQL("DELETE FROM wish WHERE idx ="+idx);
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}