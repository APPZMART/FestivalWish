package com.autodidact.developers.festivalwish;

public class MonthImage {
    public Long name;
    public String url;

    public MonthImage(Long name, String url) {
        this.name = name;
        this.url = url;
    }

    public MonthImage() {
    }

    public Long getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}

