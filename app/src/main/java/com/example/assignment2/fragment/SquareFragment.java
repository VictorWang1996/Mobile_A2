package com.example.assignment2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment2.R;
import com.example.assignment2.activity.LogInActivity;
import com.example.assignment2.activity.SendPostActivity;
import com.example.assignment2.adapter.PostAdapter;
import com.example.assignment2.entity.PostEntity;
import com.example.assignment2.utils.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SquareFragment extends Fragment implements View.OnClickListener {

    private ImageButton btn_sendPost;
    private RecyclerView mRecyclerview;
    private SmartRefreshLayout mRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_square,container,false);
        btn_sendPost = view.findViewById(R.id.btn_sendpost);
        mRecyclerview = view.findViewById(R.id.recyclerview);
        mRefreshLayout = view.findViewById(R.id.refreshlayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh(2000/*,false*/);
                loadPosts();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishLoadMore(2000/*,false*/);
                Toast.makeText(getActivity(),"No more data available!",Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);

        loadPosts();
        setClickListener();
        return view;
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
                    Collections.sort(posts, new Comparator<PostEntity>() {
                        @Override
                        public int compare(PostEntity postEntity, PostEntity t1) {
                            return -postEntity.getPostTime().compareTo(t1.getPostTime());
                        }
                    });
                    PostAdapter postAdapter = new PostAdapter(getActivity(),posts);
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

    private void setClickListener(){
        btn_sendPost.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_sendpost:
                if(Database.mAuth.getCurrentUser() == null){
                    Toast.makeText(getContext(),"Sign In Please!", Toast.LENGTH_SHORT).show();
                    intent  = new Intent(getActivity(), LogInActivity.class);
                    startActivity(intent);
                }else{
//                    Database.loadCurrentUser(getContext());
                    intent = new Intent(getActivity(), SendPostActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
