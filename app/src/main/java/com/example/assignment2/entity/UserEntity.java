package com.example.assignment2.entity;

import java.util.ArrayList;
import java.util.List;

public class UserEntity {
    public String id;
    public String username;
    public String email;
    public String phone = "";
    public String age = "";
    public String sex = "";
    public String location = "";
    public List<PostEntity> postList;
//    private String password;

    public UserEntity(){

    }

    public UserEntity(String id, String username, String email){
        this.id = id;
        this.username = username;
        this.email = email;
        this.postList = new ArrayList<>();
//        this.password = password;
    }

    public UserEntity(String id, String username, String email, String phone, String age, List<PostEntity> postList){
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.postList = new ArrayList<>();
        this.postList = postList;
        //this.postList.addAll(postList);

    }

    public UserEntity(UserEntity user){
        this.id = user.id;
        this.username = user.username;
        this.email = user.email;
        this.phone = user.phone;
        this.age = user.age;
    }

    public void addPost(PostEntity post){
        postList.add(post);
    }

    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public String toString(){
        return username+" "+email;
    }

//    public void setPassword(String password){
//        this.password = password;
//    }
}
