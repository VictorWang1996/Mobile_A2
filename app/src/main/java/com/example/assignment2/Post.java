package com.example.assignment2;

import java.util.List;

public class Post {
    public String postID;
    public String userID;
    public String postTime;
    public String postText;
    public List<String> postImgPath;

    public Post(Post post) {
        this.postID = post.postID;
        this.userID = post.userID;
        this.postTime = post.postTime;
        this.postText = post.postText;
        for(int i=0;i<postImgPath.size();i++){
            this.postImgPath.add(post.postImgPath.get(i));
        }
    }
    public Post(){

    }

    public Post(String postID, String userID, String postTime, String postText,List<String> postImgPath) {
        this.postID = postID;
        this.userID = userID;
        this.postTime = postTime;
        this.postText = postText;
        for(int i=0;i<postImgPath.size();i++){
            this.postImgPath.add(postImgPath.get(i));
        }
    }

    public void addImg(String img){
        postImgPath.add(img);
    }
}
