package com.example.bepcom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    ImageView textView;
    ImageView fingerScanner;
    ImageView capTure;
    TextView userName;

    AppCompatButton logOut;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.colorPrimary));
        inits();


    }

    private void inits() {
        userName = (TextView) findViewById(R.id.userName);
        fingerScanner = (ImageView) findViewById(R.id.fingerScanner);
        capTure = (ImageView) findViewById(R.id.capture);
        logOut = (AppCompatButton) findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


}