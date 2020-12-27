package com.example.socialnetwork.Model;

import java.util.HashMap;

public class Post {
    String pId, pTitle, pTime, pDescr, pImage, uid, likeCount, commentCount;

    public Post() {
    }

    public Post(String pId, String pTitle, String pTime, String pDescr, String pImage,
                String uid,
                String likeCount, String commentCount) {
        this.pId = pId;
        this.pTitle = pTitle;
        this.pTime = pTime;
        this.pDescr = pDescr;
        this.pImage = pImage;
        this.uid = uid;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getpDescr() {
        return pDescr;
    }

    public void setpDescr(String pDescr) {
        this.pDescr = pDescr;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", getUid());
        map.put("pId", getpId());
        map.put("pTitle", getpTitle());
        map.put("pTime", getpTime());
        map.put("pDescr", getpDescr());
        map.put("pImage", getpImage());
        map.put("likeCount", getLikeCount());
        map.put("commentCount", getCommentCount());
        return map;
    }
}