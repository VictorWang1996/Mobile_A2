package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.assignment2.imageTest.GridImageActivity;
import com.example.assignment2.me.ContainerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//platform
    private Button btnLogIn;
    private Button btnImg;
    private Button btnMe;
    private Button btnGridImage;
    private Button test;
    private ImageView test_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogIn = findViewById(R.id.btn_login_main);
        btnLogIn.setOnClickListener(this);
        btnImg = findViewById(R.id.btn_loadimg);
        btnImg.setOnClickListener(this);
        btnMe = findViewById(R.id.btn_fragment);
        btnMe.setOnClickListener(this);
        test = findViewById(R.id.btn_test);
        test.setOnClickListener(this);
        test_img = findViewById(R.id.test_image);
        btnGridImage = findViewById(R.id.btn_gridImage);
        btnGridImage.setOnClickListener(this);
//        test_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login_main:
                Intent toLogin = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(toLogin);
                break;
            case R.id.btn_loadimg:
                Intent toLoadImage = new Intent(MainActivity.this, ReadImageActivity.class);
                startActivity(toLoadImage);
                break;
            case R.id.btn_fragment:
                Intent toContainer = new Intent(MainActivity.this, ContainerActivity.class);
                startActivity(toContainer);
                break;
            case R.id.btn_test:
                test_img.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) test.getLayoutParams();
                params.setMargins(0,0,0,250);
                test.setLayoutParams(params);
                break;
            case R.id.btn_gridImage:
                Intent toGridImage = new Intent(MainActivity.this, GridImageActivity.class);
                startActivity(toGridImage);
                break;
        }
    }
}