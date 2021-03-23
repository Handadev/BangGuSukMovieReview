package com.example.banggusukmoviereview;

public class SearchMovie {
    String title;
    String image;
    String userRating;
    String director;
    String actor;
    String link;

    public SearchMovie(String title, String image, String userRating, String director, String actor, String link) {
        this.title = title;
        this.image = image;
        this.userRating = userRating;
        this.director = director;
        this.actor = actor;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
