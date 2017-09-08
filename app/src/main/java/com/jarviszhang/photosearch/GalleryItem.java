package com.jarviszhang.photosearch;

/**
 * Created by jarvis on 9/8/17.
 */

public class GalleryItem {
    String id;
    String secret;
    String server;
    String farm;

    public String getUrl(){
        return "http://farm"+farm+".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }
}
