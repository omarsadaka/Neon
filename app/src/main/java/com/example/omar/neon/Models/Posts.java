package com.example.omar.neon.Models;

import java.io.Serializable;

/**
 * Created by Omar on 12/23/2018.
 */

public class Posts implements Serializable {

    private String image;
    private String title;
    private String content;

    public Posts() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
