package com.example.assignment2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.assignment2.R;
import com.example.assignment2.adapter.MyCollectionAdapter;
import com.example.assignment2.adapter.PostAdapter;
import com.example.assignment2.entity.PostEntity;
import com.example.assignment2.utils.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MyCollectionActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerview;
    private ImageView mMyCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
    }

    private void loadPosts(){
        ValueEventListener postsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //dataSnapshot.getValue() get a hashMap type
                    //Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue(Map.class);
                    GenericTypeIndicator<Map<String, PostEntity>> genericTypeIndicator = new GenericTypeIndicator<Map<String, PostEntity>>() {};
                    Map<String, PostEntity> map = dataSnapshot.getValue(genericTypeIndicator);
                    List<PostEntity> posts = new ArrayList<>();
                    for(PostEntity key:map.values()){
                        posts.add(key);
//                        Log.e("Add", String.valueOf(posts.size()));
                    }
                    MyCollectionAdapter postAdapter = new MyCollectionAdapter(MyCollectionActivity.this,posts);
                    mRecyclerview.setAdapter(postAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.mFdatabase.child("posts").addListenerForSingleValueEvent(postsListener);

    }

    @Override
    public void onClick(View v) {

    }
}