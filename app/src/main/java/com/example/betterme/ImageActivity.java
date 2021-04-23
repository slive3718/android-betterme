package com.example.betterme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

public class ImageActivity extends AppCompatActivity {
    ZoomageView zoomageView;
    ImageView ivClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        String url = getIntent().getStringExtra("image");
        if (url == null) finish();
        zoomageView = findViewById(R.id.myZoomageView);
        ivClose = findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .into(zoomageView);
        } catch (Exception e) {

        }
    }
}