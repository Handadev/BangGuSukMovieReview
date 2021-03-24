package com.example.banggusukmoviereview;

import java.util.ArrayList;

public class Storage {
    //startActivityForResult
    public static final int GET_IMAGE = 10;
    public static final int GET_REVIEW = 20;
    public static final int GET_WISH_LIST = 30;

    //현재 액티비티
    public static int curScreen;
    //액티비티 전환
    public static final int MAIN_ACTIVITY = 100;
    public static final int BOX_OFFICE_ACTIVITY = 200;
    public static final int WRITE_ACTIVITY = 300;
    public static final int SEARCH_ACTIVITY = 400;
    public static final int WISH_LIST_ACTIVITY = 500;

    //SearchMovie naver api id password
    public static final String CLIENT_ID = "JRop7AmWIR0W0jdIp4EF";
    public static final String CLIENT_SECRET = "o08qeP7qqb";

    public static ArrayList<Review> reviewArr = new ArrayList<>();
    public static ArrayList<BoxOffice> boxOfficeArr = new ArrayList<>();
    public static ArrayList<SearchMovie> searchMovieArr = new ArrayList<>();
    public static ArrayList<WishList> wishArr = new ArrayList<>();

    //BoxOffice SearchMovie alertDialoge용
    public static String[] selWrite = new String[] {"리뷰 작성", "위시리스트 작성"};
    public static int selected = 0;

    //BoxOffice or SearchMovie 확인용
    public static boolean isBoxOfficeData;

    //WriteActivity에서 사용자가 직접 입력한 정보인지 인터넷에서 가져온 정보인지 확인
    public static boolean isDataFormNetwork;
    //main이 아닌 다른 엑티비티에서 리뷰 작성버튼을 눌렀을 때
    public static boolean isAccessPointNotMain;

}
