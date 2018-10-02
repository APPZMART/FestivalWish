package com.autodidact.developers.festivalwish;

public class ImageUpload {
    public String name;
    public String url;

    public ImageUpload(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public ImageUpload() {
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
