package com.tradingsimulator.model;

import java.util.Objects;

public class News {
    private int id;
    private String headline;
    private String image;
    private String source;
    private String summary;
    private String url;
    private int datetime;

    public News() {
    }

    public News(int id, String headline, String image, String source, String summary, String url, int datetime) {
        this.id = id;
        this.headline = headline;
        this.image = image;
        this.source = source;
        this.summary = summary;
        this.url = url;
        this.datetime = datetime;
    }

    public int getDatetime() {
        return datetime;
    }

    public void setDatetime(int datetime) {
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return id == news.id && datetime == news.datetime && Objects.equals(headline, news.headline) && Objects.equals(image, news.image) && Objects.equals(source, news.source) && Objects.equals(summary, news.summary) && Objects.equals(url, news.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, headline, image, source, summary, url, datetime);
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", headline='" + headline + '\'' +
                ", image='" + image + '\'' +
                ", source='" + source + '\'' +
                ", summary='" + summary + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
