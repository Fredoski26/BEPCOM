package com.example.bepcom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity {
    AppCompatButton login;
    AppCompatButton exit;
    TextView fileName;
    TextView pass;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getWindow().setStatusBarColor(ContextCompat.getColor(AuthActivity.this, R.color.colorPrimary));
        inits();
    }


    private void inits() {
        login = (AppCompatButton) findViewById(R.id.login);
        exit = (AppCompatButton) findViewById(R.id.exit);
        fileName = (TextView) findViewById(R.id.fileNumber);
        pass = (TextView) findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if (fileName.getText().toString().trim().isEmpty()) {
                    fileName.setError("enter file number");
                    Toast.makeText(getBaseContext(), "Enter file number", Toast.LENGTH_LONG).show();

                } else if (pass.getText().toString().trim().isEmpty()) {
                    pass.setError("enter password");
                    Toast.makeText(getBaseContext(), "Enter password", Toast.LENGTH_LONG).show();

                } else {
                    startActivity(new Intent(AuthActivity.this, HomeActivity.class));
                    finish();
                }

            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}