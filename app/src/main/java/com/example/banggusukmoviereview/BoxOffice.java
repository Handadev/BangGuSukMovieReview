package com.example.banggusukmoviereview;

public class BoxOffice {
    String rank;
    String movieName;
    String openDate;

    public BoxOffice(String rank, String movieName, String openDate) {
        this.rank = rank;
        this.movieName = movieName;
        this.openDate = openDate;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }
}
