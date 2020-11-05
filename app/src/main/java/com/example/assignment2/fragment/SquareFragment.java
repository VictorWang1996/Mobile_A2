package com.example.assignment2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.R;
import com.example.assignment2.activity.SendPostActivity;
import com.example.assignment2.adapter.PostAdapter;
import com.example.assignment2.entity.PostEntity;

import java.util.ArrayList;
import java.util.List;

public class SquareFragment extends Fragment implements View.OnClickListener {

    private ImageButton btn_sendPost;
    private RecyclerView mRecyclerview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_square,container,false);
        btn_sendPost = view.findViewById(R.id.btn_sendpost);
        mRecyclerview = view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        List<PostEntity> posts = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            PostEntity pe = new PostEntity();
            pe.setUserID("Whatever");
            pe.setPostTime("2020-11-03");
            pe.setCollectCount(i*2);
            pe.setCommentCount(i*3);
            pe.setLikeCount(i*4);
            posts.add(pe);
        }
        PostAdapter postAdapter = new PostAdapter(getActivity(),posts);
        mRecyclerview.setAdapter(postAdapter);
        setClickListener();
        return view;
    }

    private void setClickListener(){
        btn_sendPost.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_sendpost:
                intent = new Intent(getActivity(), SendPostActivity.class);
                startActivity(intent);
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
