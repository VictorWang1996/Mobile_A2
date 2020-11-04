package com.example.assignment2;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {
    public String id;
    public String username;
    public String email;
    public String phone = "";
    public String age = "";
    public String sex = "";
    public String location = "";
    public List<Post> postList = new ArrayList();
//    private String password;

    public User(){

    }

    public User(String id, String username, String email){
        this.id = id;
        this.username = username;
        this.email = email;
//        this.password = password;
    }

    public User(String id, String username, String email, String phone, String age, List<Post> postList){
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.age = age;
        for(int i=0;i<postList.size();i++){
            this.postList.add(new Post(postList.get(i)));
        }
    }

    public User(User user){
        this.id = user.id;
        this.username = user.username;
        this.email = user.email;
        this.phone = user.phone;
        this.age = user.age;
        for(int i=0; i<user.postList.size();i++){
            this.postList.add(user.postList.get(i));
        }
    }

    public void addPost(Post post){
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
