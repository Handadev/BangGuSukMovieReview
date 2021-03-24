package com.example.banggusukmoviereview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class BoxOfficeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    ImageView homeImg;
    ImageView searchImg;
    ImageView writeReviewImg;
    ImageView wishListImg;
    TextView dateTv;

    ListView boxOfficeList;
    BoxOffcieAdapter adapter;

    String curDate;
    String movieName;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_office);
        homeImg = findViewById(R.id.home_btn);
        searchImg = findViewById(R.id.search_movie_btn);
        writeReviewImg = findViewById(R.id.write_review_btn);
        wishListImg = findViewById(R.id.wish_list_btn);
        dateTv = findViewById(R.id.date_tv);
        boxOfficeList = findViewById(R.id.box_office_list);

        adapter = new BoxOffcieAdapter(this);
        boxOfficeList.setAdapter(adapter);

        setDateTV();
        boxOfficeRequest();

        homeImg.setOnClickListener(this);
        writeReviewImg.setOnClickListener(this);
        searchImg.setOnClickListener(this);
        wishListImg.setOnClickListener(this);

        boxOfficeList.setOnItemClickListener(this);
        boxOfficeList.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.write_review_btn) {
            Intent intent = new Intent(this, ReviewWriteActivity.class);
            Storage.isAccessPointNotMain =true;
            startActivity(intent);

        } else if (v.getId() == R.id.home_btn) {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, Storage.boxOfficeArr.get(position).getMovieName());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        showPopUp();

        return true;
    }

    private String setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        curDate = sdf.format(cal.getTime());
        return curDate;
    }

    private void boxOfficeRequest() {
        RequestQueue req = Volley.newRequestQueue(this);
        curDate = setDate();
        String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=59a64d3d6e06fc6dde6869eb0b2ebb2b&targetDt="+curDate;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,successListener,failListener);
        //몇 번 재시도 하는가 3초동안 3번더
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, 1f));
        req.add(stringRequest);
    }

    private void setDateTV() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        dateTv.setText(sdf.format(cal.getTime())+" 기준");
    }

    Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject boxOffice = jsonObject.getJSONObject("boxOfficeResult");
                JSONArray list = boxOffice.getJSONArray("dailyBoxOfficeList");

                if(boxOffice != null) {
                    for (int i = 0; i <list.length() ; i++) {
                        JSONObject room = list.getJSONObject(i);
                        String rank = room.getString("rank");
                        movieName =  room.getString("movieNm");
                        String openDate =  room.getString("openDt");
                        Storage.boxOfficeArr.add(new BoxOffice(rank, movieName, openDate));
                    }
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("한다","박스오피스 리스트 수신 실패!");
        }
    };

    public void showPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("어떤 작업을 하시겠습니까?");
        builder.setSingleChoiceItems(Storage.selWrite, Storage.selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Storage.selected = which;
            }
        });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Storage.selected == 0){
                    Intent intent = new Intent(BoxOfficeActivity.this, ReviewWriteActivity.class);
                    intent.putExtra("boxMovieName", Storage.boxOfficeArr.get(position).getMovieName());
                    Storage.isBoxOfficeData = true;
                    Storage.isDataFormNetwork = true;
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(BoxOfficeActivity.this, WishListWriteActivity.class);
                    intent.putExtra("boxMovieName", Storage.boxOfficeArr.get(position).getMovieName());
                    Storage.isBoxOfficeData = true;
                    Storage.isDataFormNetwork = true;
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Storage.boxOfficeArr.clear();
        adapter.notifyDataSetChanged();
    }
}