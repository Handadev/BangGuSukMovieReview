package com.example.banggusukmoviereview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReviewWriteActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    EditText titleEt;
    EditText dateEt;
    EditText genreEt;
    EditText reviewEt;
    RatingBar ratingBar;
    Button saveBtn;


    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);
        imageView = findViewById(R.id.imageView);
        titleEt = findViewById(R.id.title_et);
        dateEt = findViewById(R.id.date_et);
        genreEt = findViewById(R.id.genre_et);
        reviewEt = findViewById(R.id.review_et);
        ratingBar = findViewById(R.id.rating_bar);
        saveBtn = findViewById(R.id.save_btn);

        imageView.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        dateEt.setOnClickListener(this);
        genreEt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageView) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, Storage.GET_IMAGE);

        } else if (v.getId() == R.id.save_btn) {
            sendData();
            finish();

        } else if (v.getId() == R.id.date_et) {
            new DatePickerDialog(this, datePick, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();

        } else if (v.getId() == R.id.genre_et) {
            setGenre();
        }
    }

    SimpleDateFormat simpleData = new SimpleDateFormat("yyyy/MM/dd");
    Calendar cal = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener datePick = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePick, int year, int month, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateEt.setText(simpleData.format(cal.getTime()));
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Storage.GET_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            imagePath = getRealpath(selectedImageUri);

            imageView.setBackgroundColor(Color.parseColor("#00ff0000"));
            Log.d("한다", "writeReviewAc onActivityResult: " + imagePath +"/resultCode: "+resultCode);
        }

    }

    //이미지의 실제 경로를 가져오는 메소드 가져온 이미지의 경로를 텍스트 뷰에 저장하고, 저장한 경로를 바탕으로
    //이미지 뷰에서 이미지를 출력하게 하는 곳에 사용한다
    private String getRealpath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(uri, proj, null, null, null);
        int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        c.moveToFirst();
        String path = c.getString(index);
        return path;
    }

    private void sendData() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("imagePath", imagePath);
        intent.putExtra("title", titleEt.getText().toString());
        intent.putExtra("date", dateEt.getText().toString());
        intent.putExtra("genre", genreEt.getText().toString());
        intent.putExtra("rating", ratingBar.getRating());
        intent.putExtra("review", reviewEt.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }


    private void setGenre() {
        ArrayList<String> setGenreArr = new ArrayList<>();
        String[] getGenre = getResources().getStringArray(R.array.genre);
        boolean[] genreSelect = {false, false, false, false, false, false, false, false, false, false, false};
        AlertDialog.Builder genreAD = new AlertDialog.Builder(this);
        genreAD.setTitle("장르를 선택해주세요");
        genreAD.setMultiChoiceItems(getGenre, genreSelect, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    setGenreArr.add(getGenre[which]);
                }
            }
        });
        genreAD.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String items = "";
                for (int i = 0; i < setGenreArr.size(); i++) {
                    items += setGenreArr.get(i) + "  ";
                }
                genreEt.setText(items);
                Log.d("한다", "genreitem: " + items);
            }
        });
        genreAD.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        genreAD.create();
        genreAD.show();
    }

}