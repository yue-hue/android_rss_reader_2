package com.example.rss_reader_2;

import java.util.ArrayList;

public class News {
    private String newsTitle;
    private String newsLink;
    private ArrayList<News> newsList = new ArrayList<News>();

    public News() {
    }

    public News(String newsTitle, String newsLink) {
        this.newsTitle = newsTitle;
        this.newsLink = newsLink;
    }
    public String getNewsName() {
        return newsTitle;
    }
    public void setNewsName(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsLink() { return newsLink; }
    public void setNewsLink(String newsLink) { this.newsLink = newsLink; }

    public ArrayList<News> getNList() { return newsList; }
}