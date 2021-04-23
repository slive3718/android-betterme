package com.example.betterme.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SetCommunityThread implements Serializable {
    private String communityId;
    private String threadTitle;
    private String threadImage;
    private String threadContent;
    private String threadDate;
    private String threadUserId;
    private String archiveStatus;
    private UserInfo userInfo;
    private List<SetComment> comments = new ArrayList<>();


    public SetCommunityThread() {
    }

    public SetCommunityThread(JSONObject jsonObject) {
        if (jsonObject != null) {
            communityId = jsonObject.optString("community_id");
            threadTitle = jsonObject.optString("thread_title");
            threadUserId = jsonObject.optString("thread_user_id");
            threadContent = jsonObject.optString("thread_content");
            threadImage = jsonObject.optString("thread_image_name");
            threadDate = jsonObject.optString("thread_date");
            archiveStatus = jsonObject.optString("archive_status");
            userInfo = new UserInfo(jsonObject);
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
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public String getThreadImage() {
        return threadImage;
    }

    public void setThreadImage(String threadImage) {
        this.threadImage = threadImage;
    }

    public String getThreadContent() {
        return threadContent;
    }

    public void setThreadContent(String threadContent) {
        this.threadContent = threadContent;
    }

    public String getThreadDate() {
        return threadDate;
    }

    public void setThreadDate(String threadDate) {
        this.threadDate = threadDate;
    }

    public String getThreadUserId() {
        return threadUserId;
    }

    public void setThreadUserId(String threadUserId) {
        this.threadUserId = threadUserId;
    }

    public String getArchiveStatus() {
        return archiveStatus;
    }

    public void setArchiveStatus(String archiveStatus) {
        this.archiveStatus = archiveStatus;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<SetComment> getComments() {
        return comments;
    }

    public void setComments(List<SetComment> comments) {
        this.comments = comments;
    }
}
