package com.example.betterme.model;

import org.json.JSONObject;

import java.io.Serializable;

public class SetComment implements Serializable {
    String id;
    String comment;
    String date;
    String user_id = "NAME";
    String post_id;
    String archive_status;
    UserInfo userInfo;

    public SetComment() {
    }

    public SetComment(JSONObject jsonObject) {
        if (jsonObject.has("comment_id")) {
            this.id = jsonObject.optString("comment_id");
            this.comment = jsonObject.optString("comment_content");
            this.date = jsonObject.optString("comment_date");
            this.user_id = jsonObject.optString("comment_user_id");
            this.post_id = jsonObject.optString("community_id");
        } else {
            this.id = jsonObject.optString("id");
            this.comment = jsonObject.optString("comment");
            this.date = jsonObject.optString("date");
            this.user_id = jsonObject.optString("user_id");
            this.post_id = jsonObject.optString("post_id");
        }
        this.archive_status = jsonObject.optString("archive_status");
        this.userInfo = new UserInfo(jsonObject);
    }

    public SetComment(String id, String comment, String date, String archive_id, String user_id, String post_id, String archive_status) {
        this.id = id;
        this.comment = comment;
        this.date = date;
        this.user_id = user_id;
        this.post_id = post_id;
        this.archive_status = archive_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getArchive_status() {
        return archive_status;
    }

    public void setArchive_status(String archive_status) {
        this.archive_status = archive_status;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
