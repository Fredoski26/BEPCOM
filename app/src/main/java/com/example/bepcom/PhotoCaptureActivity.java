package com.example.bepcom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoCaptureActivity extends AppCompatActivity {
   ImageView userImage;
   AppCompatButton done;
   AppCompatButton back;
   TextView retake;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_capture);
        getWindow().setStatusBarColor(ContextCompat.getColor(PhotoCaptureActivity.this, R.color.colorPrimary));
        inits();
    }

    private void inits() {
        userImage = findViewById(R.id.userImage);
        done = findViewById(R.id.done);
        back = findViewById(R.id.back);
        retake = findViewById(R.id.retakeCapture);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}