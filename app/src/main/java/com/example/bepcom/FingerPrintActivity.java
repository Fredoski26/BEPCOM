package com.example.bepcom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;


public class FingerPrintActivity extends AppCompatActivity {
 AppCompatButton exit;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        getWindow().setStatusBarColor(ContextCompat.getColor(FingerPrintActivity.this, R.color.colorPrimary));
        inits();
    }

    private void inits() {
        exit = findViewById(R.id.exit);

        exit.setOnClickListener(v -> onBackPressed());


    }


}