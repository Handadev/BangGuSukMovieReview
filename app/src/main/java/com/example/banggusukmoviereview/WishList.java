package com.example.banggusukmoviereview;

public class WishList {
    int idx;
    String title;
    String memo;
    String date;

    public WishList(int idx, String title, String memo, String date) {
        this.idx = idx;
        this.title = title;
        this.memo = memo;
        this.date = date;
    }

    public WishList(String title, String memo, String date) {
        this.title = title;
        this.memo = memo;
        this.date = date;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
