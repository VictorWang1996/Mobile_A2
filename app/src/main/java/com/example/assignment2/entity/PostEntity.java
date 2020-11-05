package com.example.assignment2.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostEntity implements Serializable {
    private String postID;
    private String userID;
    private String postTime;
    private String postText;
    private int likeCount;
    private int collectCount;
    private int commentCount;
    private List<String> postImgPath;

    public List<String> getPostImgPath() {
        return postImgPath;
    }

    public void setPostImgPath(List<String> postImgPath) {
        this.postImgPath = postImgPath;
    }

    public PostEntity(){
    }
    public PostEntity(String postID,String userID,String postTime, String postText,List<String> postImgPath){
        this.postImgPath = new ArrayList<>();
        this.postID = postID;
        this.userID = userID;
        this.postTime = postTime;
        this.postText = postText;
        this.postImgPath = postImgPath;
        likeCount = 0;
        collectCount= 0 ;
        commentCount = 0;
    }


    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
