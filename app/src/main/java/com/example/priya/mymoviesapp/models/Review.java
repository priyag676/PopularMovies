package com.example.priya.mymoviesapp.models;

/**
 * Created by Priya on 1/1/2016.
 */
public class Review {

    private String id;
    private String author;
    private String content;
    public Review() { }



    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() { return id; }

    public String getAuthor() { return author; }

    public String getContent() { return content; }
}