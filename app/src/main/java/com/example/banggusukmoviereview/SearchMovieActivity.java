package com.example.banggusukmoviereview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SearchMovieActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    EditText searchEt;
    Button searchBtn;
    ListView searchList;

    SearchMovieAdapter adapter;

    public StringBuilder sb;
    String movieName;

    String title;
    String image;
    int position;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        searchEt = findViewById(R.id.search_movie_et);
        searchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_movie_list);

        adapter = new SearchMovieAdapter(this);
        searchList.setAdapter(adapter);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        searchBtn.setOnClickListener(this);
        searchList.setOnItemClickListener(this);
        searchList.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_btn) {
            Storage.searchMovieArr.clear();
            movieName = searchEt.getText().toString();
            imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String str = searchURLConnection(movieName);
                    Log.d("한다", "MOVIE SEARCH : "+str);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultToJSON(str);

                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Storage.searchMovieArr.get(position).getLink()));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        showPopUp();
        return true;
    }

    private String searchURLConnection(String movieName) {
        try {
            String search = URLEncoder.encode(movieName,"utf-8");
            String url = "https://openapi.naver.com/v1/search/movie.json?query="+search;

            URL movieUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)movieUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Naver-Client-Id", Storage.CLIENT_ID);
            connection.setRequestProperty("X-Naver-Client-Secret", Storage.CLIENT_SECRET);
            int responseCode = connection.getResponseCode();
            BufferedReader br;
            //네이버는 정상 responseCode를 200으로 설정함
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            sb = new StringBuilder();
            String lines;

            while ((lines = br.readLine()) != null) {
                sb.append(lines);
            }
            br.close();
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void resultToJSON(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray resultArr = jsonObject.getJSONArray("items");

            if (jsonObject != null) {
                for (int i = 0; i < resultArr.length(); i++) {
                    JSONObject room = resultArr.getJSONObject(i);
                    title = room.getString("title");
                    title = title.replace("<b>","")
                            .replace("</b>","")
                            .replace("&amp;","");
                    String link = room.getString("link");
                    image = room.getString("image");
                    String director = room.getString("director");
                    String actor = room.getString("actor");
                    String userRating = room.getString("userRating");
                    Storage.searchMovieArr.add(new SearchMovie(title, image, userRating, director, actor, link));
                }
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
                    Intent intent = new Intent(SearchMovieActivity.this, ReviewWriteActivity.class);
                    intent.putExtra("searchTitle", Storage.searchMovieArr.get(position).getTitle());
                    intent.putExtra("searchImage", Storage.searchMovieArr.get(position).getImage());
                    Storage.isBoxOfficeData = false;
                    Storage.isDataFormNetwork = true;
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SearchMovieActivity.this, WishListWriteActivity.class);
                    intent.putExtra("searchTitle", Storage.searchMovieArr.get(position).getTitle());
                    Storage.isBoxOfficeData = false;
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
        Log.d("한다", "onResume: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Storage.searchMovieArr.clear();
        adapter.notifyDataSetChanged();
        Log.d("한다", "onDestroy: ");
    }
}