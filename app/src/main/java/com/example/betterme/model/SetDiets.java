package com.example.betterme.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SetDiets implements Serializable {

    private String id;
    private String title;
    private String content;
    private String date_posted;
    private String routine_count;
    private String routine_format;
    private String post_user_id;
    private String post_type;
    private String other_diet;
    private String archive_id;
    private String image_id;
    private String type_of_diet;
    private String post_image_name;
    private String post_image_url;
    private String archive;
    private String target_audience;
    private String like_id;
    private String like_status;
    private int getLikeCount;
    private int getCommentCount;
    private UserInfo userInfo;
    private List<SetImages> images = new ArrayList<>();
    private List<SetComment> comments = new ArrayList<>();

    public SetDiets() {
    }

    public SetDiets(JSONObject jsonObject) {
        id = jsonObject.optString("post_id");
        title = jsonObject.optString("post_title");
        content = jsonObject.optString("post_content");
        date_posted = jsonObject.optString("date_posted");
        routine_count = jsonObject.optString("routine_count");
        routine_format = jsonObject.optString("routine_format");
        post_user_id = jsonObject.optString("post_user_id");
        post_type = jsonObject.optString("post_type");
        other_diet = jsonObject.optString("other_diet");
        archive_id = jsonObject.optString("archive_id");
        image_id = jsonObject.optString("image_id");
        type_of_diet = jsonObject.optString("type_of_diet");
        post_image_name = jsonObject.optString("post_image_name");
        post_image_url = jsonObject.optString("post_image_url");
        archive = jsonObject.optString("archive");
        target_audience = jsonObject.optString("target_audience");
        like_id = jsonObject.optString("like_id");
        getLikeCount = jsonObject.optInt("getLikeCount",0);
        getCommentCount = jsonObject.optInt("getCommentCount",0);
        userInfo = new UserInfo(jsonObject);
        like_status = jsonObject.optJSONObject("like_status").optString("like_status");
        if (jsonObject.has("images")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("images");
                for (int i = 0; i < jsonArray.length(); i++) {
                    images.add(new SetImages(jsonArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.has("getComments")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("getComments");
                for (int i = 0; i < jsonArray.length(); i++) {
                    comments.add(new SetComment(jsonArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(String date_posted) {
        this.date_posted = date_posted;
    }

    public String getRoutine_count() {
        return routine_count;
    }

    public void setRoutine_count(String routine_count) {
        this.routine_count = routine_count;
    }

    public String getRoutine_format() {
        return routine_format;
    }

    public void setRoutine_format(String routine_format) {
        this.routine_format = routine_format;
    }

    public String getPost_user_id() {
        return post_user_id;
    }

    public void setPost_user_id(String post_user_id) {
        this.post_user_id = post_user_id;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getOther_diet() {
        return other_diet;
    }

    public void setOther_diet(String other_diet) {
        this.other_diet = other_diet;
    }

    public String getArchive_id() {
        return archive_id;
    }

    public void setArchive_id(String archive_id) {
        this.archive_id = archive_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getType_of_diet() {
        return type_of_diet;
    }

    public void setType_of_diet(String type_of_diet) {
        this.type_of_diet = type_of_diet;
    }

    public String getPost_image_name() {
        return post_image_name;
    }

    public void setPost_image_name(String post_image_name) {
        this.post_image_name = post_image_name;
    }

    public String getPost_image_url() {
        return post_image_url;
    }

    public void setPost_image_url(String post_image_url) {
        this.post_image_url = post_image_url;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getTarget_audience() {
        return target_audience;
    }

    public void setTarget_audience(String target_audience) {
        this.target_audience = target_audience;
    }

    public String getLike_id() {
        return like_id;
    }

    public void setLike_id(String like_id) {
        this.like_id = like_id;
    }

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    public int getGetLikeCount() {
        return getLikeCount;
    }

    public void setGetLikeCount(int getLikeCount) {
        this.getLikeCount = getLikeCount;
    }

    public int getGetCommentCount() {
        return getCommentCount;
    }

    public void setGetCommentCount(int getCommentCount) {
        this.getCommentCount = getCommentCount;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<SetImages> getImages() {
        return images;
    }

    public void setImages(List<SetImages> images) {
        this.images = images;
    }

    public List<SetComment> getComments() {
        return comments;
    }

    public void setComments(List<SetComment> comments) {
        this.comments = comments;
    }
}
