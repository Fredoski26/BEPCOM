package com.example.bepcom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoCaptureActivity extends AppCompatActivity {
    ImageView userImage;
    AppCompatButton done;
    AppCompatButton back;
    TextView retake;

    TextView capture;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_capture);
        getWindow().setStatusBarColor(ContextCompat.getColor(PhotoCaptureActivity.this, R.color.colorPrimary));
        inits();
    }

    private void inits() {

        done =  (AppCompatButton) findViewById(R.id.done);
        back = (AppCompatButton) findViewById(R.id.back);
        retake = (TextView) findViewById(R.id.retakeCapture);
        capture = (TextView) findViewById(R.id.takeCapture);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(PhotoCaptureActivity.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(PhotoCaptureActivity.this, new String[]{android.Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                }
                else {
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera_intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PhotoCaptureActivity.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(PhotoCaptureActivity.this, new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                }
                else {
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera_intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            userImage=(ImageView) findViewById(R.id.userImage);
            done =  (AppCompatButton) findViewById(R.id.done);
            retake = (TextView) findViewById(R.id.retakeCapture);
            capture = (TextView) findViewById(R.id.takeCapture);
            capture.setVisibility(View.INVISIBLE);
            retake.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);
            userImage.setImageBitmap(photo);
        }
    }
}