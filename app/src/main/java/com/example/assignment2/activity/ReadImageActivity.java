package com.example.assignment2.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.assignment2.R;
import com.example.assignment2.utils.Database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;

public class ReadImageActivity extends AppCompatActivity {
    private ImageView image;
    private ImageView upload_image;
    private StorageReference mStorageRef;
    private String image_name = "Screen Shot 2020-10-18 at 3.34.01 pm.png";
    private String path = "storage/emulated/0/DCIM/Camera/IMG_20201102_203411.jpg";
    private View root;
    private Button camera;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public static final int TAKE_PHOTO_CODE = 1;
    public static final int SELECT_IMAGE_CODE = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_image);
        image = findViewById(R.id.image1);
        upload_image = findViewById(R.id.image2);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        root = findViewById(R.id.root_view);
        camera = findViewById(R.id.btn_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromAlbum();
            }
        });
        Database.download_image(image_name,ReadImageActivity.this,upload_image);
        image.setImageURI(Uri.fromFile(new File(path)));
    }

    public void pickImageFromAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECT_IMAGE_CODE:
                if (resultCode == RESULT_OK && data!=null){
                    Uri imageUri = data.getData();
                    image.setImageURI(imageUri);
                    try {
//                        String type = getExtension(imageUri);
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        formatter.setTimeZone(TimeZone.getTimeZone("GMT+11"));
//                        Date curDate = new Date(System.currentTimeMillis());
//                        String createDate = formatter.format(curDate);
                        Database.upload_image(imageUri,ReadImageActivity.this);
//                        FirebaseUser user = Database.mAuth.getCurrentUser();
//                        Post post = new Post(user.getUid()+)
//                        Database.createPosts();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            default:
                break;
        }
    }

//    private String getExtension(Uri uri){
//        ContentResolver cr=getContentResolver();
//        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
//    }

}