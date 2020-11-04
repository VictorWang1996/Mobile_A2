package com.example.assignment2.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;

import com.example.assignment2.R;

public class ContainerActivity extends AppCompatActivity {
    RadioButton btn_me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        btn_me = findViewById(R.id.radiobtn_me);


    }
}