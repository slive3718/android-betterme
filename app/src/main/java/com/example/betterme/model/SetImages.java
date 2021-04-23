package com.example.betterme.model;

import android.util.Log;

import com.example.betterme.APIContants;

import org.json.JSONObject;

import java.io.Serializable;

public class SetImages implements Serializable {
    String id;
    String post_id;
    String image_name;
    String user_id = "NAME";
    String date_created;
    String image_post_type;
    String image_full_path;

    public SetImages() {
    }

    public SetImages(JSONObject jsonObject) {
        this.id = jsonObject.optString("image_id");
        this.post_id = jsonObject.optString("post_id");;
        this.image_name = jsonObject.optString("image_name");;
        this.user_id = jsonObject.optString("user_id");;
        this.date_created = jsonObject.optString("date_created");;
        this.image_post_type = jsonObject.optString("image_post_type");;
        this.image_full_path = jsonObject.optString("image_full_path");;
    }

    public SetImages(String id, String post_id, String image_name, String user_id, String date_created, String image_post_type, String image_full_path) {
        this.id = id;
        this.post_id = post_id;
        this.image_name = image_name;
        this.user_id = user_id;
        this.date_created = date_created;
        this.image_post_type = image_post_type;
        this.image_full_path = image_full_path;
    }

    public String getImageURL(){
        Log.e("Image", APIContants.IMAGE_BASE_URL + image_name);
        return APIContants.IMAGE_BASE_URL + image_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getImage_post_type() {
        return image_post_type;
    }

    public void setImage_post_type(String image_post_type) {
        this.image_post_type = image_post_type;
    }

    public String getImage_full_path() {
        return image_full_path;
    }

    public void setImage_full_path(String image_full_path) {
        this.image_full_path = image_full_path;
    }
}
