package com.example.banggusukmoviereview;

public class Review {
    int idx;
    String imagePath;
    String title;
    String date;
    String genre;
    String review;
    float rating;

    public Review(String imagePath, String title, String date, String genre, String review, float rating) {
        this.imagePath = imagePath;
        this.title = title;
        this.date = date;
        this.genre = genre;
        this.review = review;
        this.rating = rating;
    }

    public Review(int idx, String imagePath, String title, String date, String genre, String review, float rating) {
        this.idx = idx;
        this.imagePath = imagePath;
        this.title = title;
        this.date = date;
        this.genre = genre;
        this.review = review;
        this.rating = rating;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
