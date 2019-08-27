package com.example.mydata;

public class Upload {
    public String name;
    public String imgurl;

    public Upload() {

    }

    public Upload(String name, String imgurl) {
        this.name = name;
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
