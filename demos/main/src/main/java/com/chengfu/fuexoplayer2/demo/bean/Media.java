package com.chengfu.fuexoplayer2.demo.bean;

import java.io.Serializable;

public class Media implements Serializable {
    private String name;
    private String path;
    private String image;
    private String type;
    private String tag;

    public Media() {

    }

    public Media(String name, String path, String type, String tag) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.tag = tag;
    }

    public Media(String name, String path, String image, String type, String tag) {
        this.name = name;
        this.path = path;
        this.image = image;
        this.type = type;
        this.tag = tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
