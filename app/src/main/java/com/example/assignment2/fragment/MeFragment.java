package com.example.assignment2.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment2.activity.EditProfileActivity;
import com.example.assignment2.activity.LogInActivity;
import com.example.assignment2.activity.MainActivity;
import com.example.assignment2.activity.MyCollectionActivity;
import com.example.assignment2.adapter.PostAdapter;
import com.example.assignment2.adapter.UserPostAdapter;
import com.example.assignment2.entity.PostEntity;
import com.example.assignment2.entity.UserEntity;
import com.example.assignment2.utils.Database;
import com.example.assignment2.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView img_edit;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_age;
    private TextView tv_email;
    private TextView tv_location;
    private TextView tv_phone;
    private ImageView img_header;
    private SmartRefreshLayout refreshLayout;
    private ListView lv_posts;
    private UserEntity loadUser;
    private RecyclerView mRecyclerview;
    private ImageView im_signin_out;
    private ImageView iv_my_collect;
    public static UserEntity currentuser = new UserEntity();

    public MeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.frag_me, container, false);

        mRecyclerview = view.findViewById(R.id.user_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        refreshLayout = view.findViewById(R.id.user_refresh);

        iv_my_collect = view.findViewById(R.id.iv_myCollection);
        iv_my_collect.setOnClickListener(this);

        im_signin_out = view.findViewById(R.id.iv_signin_out);
        im_signin_out.setOnClickListener(this);

        tv_name = view.findViewById(R.id.tv_name);
        tv_name.setOnClickListener(this);
        tv_sex = view.findViewById(R.id.tv_sex);
        tv_age = view.findViewById(R.id.tv_age);
        tv_email = view.findViewById(R.id.tv_email);
        tv_location = view.findViewById(R.id.tv_location);
        img_edit = view.findViewById((R.id.img_edit));
        img_edit.setOnClickListener(this);
        tv_phone = view.findViewById(R.id.tv_phone);
        img_header = view.findViewById(R.id.img_header);
        Database.loadCurrentUser(tv_name, tv_sex, tv_age, tv_email, tv_location, tv_phone, img_header,getActivity());
        Log.e("Me Cur", currentuser.location+currentuser.getHeader());
        loadUserPosts();
        Log.e("Me:",currentuser.toString());
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000/*,false*/);
                loadUserPosts();
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("Me",currentuser.toString());
    }

    private void loadUserPosts(){
        FirebaseUser user = Database.mAuth.getCurrentUser();
        if(user==null){
            Log.d("Load Post:", "no user");
            return;
        }
        ValueEventListener postsListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserEntity load_user = dataSnapshot.getValue(UserEntity.class);
                    currentuser = new UserEntity(load_user);
                    List<PostEntity> posts = new ArrayList<>();
                    if(load_user.postList!= null && load_user.postList.size()>0){
                        for(PostEntity key : load_user.postList){
                            posts.add(key);
                        }
                        Collections.sort(posts, new Comparator<PostEntity>() {
                            @Override
                            public int compare(PostEntity postEntity, PostEntity t1) {
                                return -postEntity.getPostTime().compareTo(t1.getPostTime());
                            }
                        });
                    }
                    UserPostAdapter postAdapter = new UserPostAdapter(getActivity(),posts);
                    mRecyclerview.setAdapter(postAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Database.mFdatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(postsListener);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_signin_out:
                FirebaseUser user = Database.mAuth.getCurrentUser();
                if(user == null){
                    Intent toSignIn = new Intent(getContext(), LogInActivity.class);
                    startActivity(toSignIn);
                }
                else{
                    Database.mAuth.signOut();
                    Intent toMain = new Intent(getContext(), MainActivity.class);
                    startActivity(toMain);
                }
                return;
            case R.id.tv_name:
                if(tv_name.getText().toString().equals("Sign In")){
                    Intent toSignIn = new Intent(getContext(), LogInActivity.class);
                    startActivity(toSignIn);
                    return;
                }
            case R.id.img_edit:
                if(Database.mAuth.getCurrentUser()!= null){
                    Intent toEdit = new Intent(getContext(), EditProfileActivity.class);
                    startActivity(toEdit);
                }else{
                    Toast.makeText(getContext(),"Sign In Please!",Toast.LENGTH_SHORT).show();
                    Intent toSignIn = new Intent(getContext(), LogInActivity.class);

                    startActivity(toSignIn);
                }
                return;
            case R.id.iv_myCollection:
                if(MeFragment.currentuser.getCollect()!= null && MeFragment.currentuser.getCollect().size()>0){
                    Intent toCollection = new Intent(getActivity(), MyCollectionActivity.class);
                    startActivity(toCollection);
                }else{
                    Toast.makeText(getContext(),"No Collections!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}