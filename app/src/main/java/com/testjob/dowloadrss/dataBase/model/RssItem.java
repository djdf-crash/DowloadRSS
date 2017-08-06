package com.testjob.dowloadrss.dataBase.model;


public class RssItem {
    private String link;
    private String description;
    private String pubDate;
    private String title;

    public RssItem(String link, String description, String pubDate, String title) {
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.title = title;
    }

    public RssItem() {
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getTitle() {
        return title;
    }
}
