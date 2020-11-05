package com.example.assignment2.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.assignment2.entity.PostEntity;
import com.example.assignment2.utils.Database;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.R;
import com.example.assignment2.adapter.ImageAdapter;
import com.example.assignment2.utils.EmotionData;
import com.example.assignment2.utils.EmotionInputDetector;
import com.example.assignment2.utils.Note;
import com.example.assignment2.utils.SpannableMaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SendPostActivity extends AppCompatActivity implements View.OnClickListener {

    private EmotionInputDetector mDetector;
    ImageButton send,cancel,btn_emoji,btn_camera,btn_picture;
    EditText content;
    //ImageView showPicture;
    String currentPhotoPath;
    RecyclerView mRvEditImage;
    private final int MAX_IMAGES = 9;
    public static final int TAKE_PHOTO_CODE = 1;
    public static final int SELECT_IMAGE_CODE = 111;
    public Uri photoURI;
    public List<Uri> imageUris;
    ImageAdapter mImageAdapter;
    GridLayoutManager gridLayoutManager;
    private List<Note> mNote = EmotionData.getNotes();
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_post);
        init();
        setOnClickListener();
        //getWindow().setSoftInputMode(getWindowManager().LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    private void init(){
        send = findViewById(R.id.btn_send);
        cancel = findViewById(R.id.btn_cancel);
        content = findViewById(R.id.et_content);
        btn_camera = findViewById(R.id.btn_camera);
        btn_emoji = findViewById(R.id.btn_emoji);
        btn_picture = findViewById(R.id.btn_picture);
        mRvEditImage = findViewById(R.id.rv_editImage);
        gridLayoutManager = new GridLayoutManager(SendPostActivity.this,3);
        mRvEditImage.setLayoutManager(gridLayoutManager);
        imageUris = new ArrayList<>();
        mImageAdapter = new ImageAdapter(SendPostActivity.this, imageUris, new ImageAdapter.OnItemClickListener() {
            @Override
            public void onTakePhotoClick() {
                // 点击图片，放大图片
                Toast.makeText(SendPostActivity.this,"Click on photo",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //长按删除图片
                Toast.makeText(SendPostActivity.this,"ready to delete",Toast.LENGTH_SHORT).show();
                imageUris.remove(position);
                mImageAdapter.notifyDataSetChanged();
            }
            @Override
            public void onAddButtonClick() {
                pickImageFromAlbum();
            }
        });
        mRvEditImage.setAdapter(mImageAdapter);
        // set emoji keyboard
        GridView gridView = findViewById(R.id.grid_emoji_view);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note = (Note)adapterView.getAdapter().getItem(i);

                EditText editText = content;

                int start = editText.getSelectionStart();

                Editable editable = editText.getEditableText();

                Spannable spannable = SpannableMaker.buildEmotionSpannable(SendPostActivity.this, note.getText(), (int)editText.getTextSize());

                editable.insert(start, spannable);
            }
        });
        gridView.setAdapter(new EmojiGridViewAdapter());

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(findViewById(R.id.emoji_keyboard))
                .bindToContent(findViewById(R.id.content_view))
                .bindToEditText(content)
                .bindToEmotionButton(btn_emoji)
                .build();
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }
    private void setOnClickListener(){
        send.setOnClickListener(this);
        cancel.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_cancel:
                intent = new Intent(SendPostActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_send:
                try {
                    sendPost();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                intent = new Intent(SendPostActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_camera:
                dispatchTakePictureIntent();
                break;
            case R.id.btn_picture:
                pickImageFromAlbum();
                break;
        }
    }
    private void sendPost() throws FileNotFoundException {
       List<String> cloudPath = new ArrayList<>();
        if(!(imageUris == null || imageUris.size() == 0)){
            for(Uri uri : imageUris){
                Database.upload_image(uri, SendPostActivity.this);
                cloudPath.add(Database.mAuth.getUid()+"/"+uri.getLastPathSegment());
            }
        }
        Log.d("imagePost list", String.valueOf(imageUris.size()));
        String postText = content.getText().toString();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+11"));
        String postTime = simpleDateFormat.format(new Date());
        PostEntity post = new PostEntity(Database.mAuth.getUid()+"-"+postTime, Database.mAuth.getUid(), postTime,postText,cloudPath);
        Log.d("Post list", String.valueOf(post.getPostImgPath().size()));
        Database.update(post);
        Database.updateUser(post);
        imageUris.clear();
        Toast.makeText(SendPostActivity.this,"already send",Toast.LENGTH_SHORT).show();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("SendPostActivity","Error occurred while creating the File" );
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.assignment2.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_CODE);
            }
        }
        else{
            Toast.makeText(SendPostActivity.this,"There is not app that support this action",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+11"));
        String timeStamp = simpleDateFormat.format(new Date());
        Log.d("Time",timeStamp);
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    public void pickImageFromAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE_CODE);
    }
    public void addImage(){
        if (imageUris.size() < MAX_IMAGES) {
            imageUris.add(photoURI);
            mImageAdapter.notifyDataSetChanged();
            ImageView imageView = (ImageView) gridLayoutManager.findViewByPosition(0);
            imageView.setVisibility(View.VISIBLE);

        }else {
            Toast.makeText(SendPostActivity.this,"Maximum number is 9",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO_CODE:
                if (resultCode == RESULT_OK && data!=null){
                    //showPicture.setImageURI(photoURI);
                    addImage();
                }
                break;
            case SELECT_IMAGE_CODE:
                if (resultCode == RESULT_OK && data!=null){
                    photoURI = data.getData();
                    //showPicture.setImageURI(photoURI);
                    addImage();
                }
            default:
                break;
        }
    }
    private class EmojiGridViewAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (!(view instanceof ImageView)) {
                view = new ImageView(SendPostActivity.this);
            }
            ImageView imageView = (ImageView) view;
            imageView.setImageResource(((Note) getItem(position)).getIconRes());
            return view;
        }

        @Override
        public int getCount() {
            return mNote.size();
        }

        @Override
        public Object getItem(int position) {
            return mNote.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

}