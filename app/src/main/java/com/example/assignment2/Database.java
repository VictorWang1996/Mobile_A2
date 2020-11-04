package com.example.assignment2;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Database {
    private static FirebaseDatabase mfirebaseDatabase = FirebaseDatabase.getInstance();
    private static FirebaseStorage mfirebaseStorage = FirebaseStorage.getInstance();
    private static StorageReference mStorageRef = mfirebaseStorage.getReference();
    public static DatabaseReference mFdatabase = mfirebaseDatabase.getReference();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public static FirebaseStorage getMfirebaseStorage(){
        return mfirebaseStorage;
    }

    public static FirebaseDatabase getMfirebaseDatabase(){
        return mfirebaseDatabase;
    }
    //check if user in realtime database. If not store to the realtime database.
    public static void createUser(){
        final FirebaseUser user = mAuth.getCurrentUser();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
//                  User user = dataSnapshot.getValue(User.class);
//                  Toast.makeText(LogInActivity.this,user.toString(),Toast.LENGTH_SHORT).show();
                    //default username is id
                    User new_user = new User(user.getUid(),user.getUid(),user.getEmail());
                    update(new_user);
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mFdatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(userListener);

    }

    public static void updateUser(final Post post){
        FirebaseUser user = mAuth.getCurrentUser();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                  User temp_user = dataSnapshot.getValue(User.class);
                  temp_user.addPost(post);
//                  Toast.makeText(LogInActivity.this,user.toString(),Toast.LENGTH_SHORT).show();
                  update(temp_user);
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mFdatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(userListener);

    }

    public static void updateUser(final User update_user){
        FirebaseUser user = mAuth.getCurrentUser();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    update(update_user);
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mFdatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(userListener);

    }

    public static void loadCurrentUser(final TextView tv_name, final TextView tv_sex, final TextView tv_age,
                                       final TextView tv_email, final TextView tv_location, Activity activity){
        FirebaseUser user = Database.mAuth.getCurrentUser();
        if(user==null){
            Toast.makeText(activity,"Log in Please",Toast.LENGTH_SHORT).show();
            return;
        }
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    User loadUser = dataSnapshot.getValue(User.class);
//                    Toast.makeText(getActivity(),loadUser.toString(),Toast.LENGTH_SHORT).show();
                    tv_name.setText(loadUser.username);
                    tv_email.setText(loadUser.email);
                    if(loadUser.age!="" && loadUser.age!=null){
                        tv_age.setVisibility(View.VISIBLE);
                        tv_age.setText(loadUser.age);
                    }
                    if(loadUser.sex!=""&& loadUser.sex!=null){
                        tv_sex.setVisibility(View.VISIBLE);
                        tv_sex.setText(loadUser.sex);
                    }
                    if(loadUser.location!="" && loadUser.location!=null){
                        tv_location.setVisibility(View.VISIBLE);
                        tv_location.setText(loadUser.location);
                    }
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        Database.mFdatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(userListener);

    }


    public static void createPosts(final Post post){
//        FirebaseUser user = mAuth.getCurrentUser();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
//                  Toast.makeText(LogInActivity.this,user.toString(),Toast.LENGTH_SHORT).show();
                  update(post);
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LogIn", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mFdatabase.child("posts").child(post.postID).addListenerForSingleValueEvent(userListener);

    }



    public static void update(User user){
        mFdatabase.child("users").child(user.id).setValue(user);
    }

    public static void update(Post post){
        mFdatabase.child("posts").child(post.postID).setValue(post);
    }

    public static void download_image(String image_name, final Activity activity, final ImageView image){

        mStorageRef.child("posts/"+image_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(activity).load(uri).into(image);
                Toast.makeText(activity,"load success",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(activity,"load fail",Toast.LENGTH_SHORT).show();
                // Handle any errors
            }
        });

    }

    public static void download_image(String image_name, final Activity activity, final ImageView image, User user){

        mStorageRef.child("posts/"+user.id+"/"+image_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(activity).load(uri).into(image);
                Toast.makeText(activity,"load success",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(activity,"load fail",Toast.LENGTH_SHORT).show();
                // Handle any errors
            }
        });

    }

    public static void upload_image(Uri file, final Activity activity) throws FileNotFoundException {
        StorageReference mStorageRef = mfirebaseStorage.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            Toast.makeText(activity,"Please log in.",Toast.LENGTH_SHORT).show();
            return;
        }
//        Uri file = Uri.fromFile(new File(path));
        if(file == null){
            Toast.makeText(activity,"file not found",Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference riversRef = mStorageRef.child(user.getUid()+"/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(activity,"upload fail",Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(activity,"upload success",Toast.LENGTH_SHORT).show();


            }
        });
    }




}
