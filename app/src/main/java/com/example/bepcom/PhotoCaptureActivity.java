package com.example.bepcom;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bepcom.constant.Constant;
import com.example.bepcom.model.PassportModel;
import com.example.bepcom.model.PassportRequest;
import com.example.bepcom.network.Api;
import com.example.bepcom.network.ApiInterface;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class PhotoCaptureActivity extends AppCompatActivity {
    ImageView userImage;
    AppCompatButton done;
    AppCompatButton back;
    TextView retake;

    ProgressBar progressBar;
    public String token = "";

    public String base64Image;

    private MultipartBody gone;

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
        progressBar = findViewById(R.id.progressBar);


       // token = getIntent().getStringExtra("token");
        base64Image = getIntent().getStringExtra("photo");

        Bitmap imageBitmap = decodeBase64(base64Image);
        userImage.setImageBitmap(imageBitmap);



        back.setOnClickListener(v -> {startActivity(new Intent(getBaseContext(), HomeActivity.class));
        }
        );
        done.setOnClickListener(v -> apiUploadPassport());
        retake.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), HomeActivity.class));
            finish();
        });





    }


    private Bitmap decodeBase64(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

   /* private  MultipartBody.Part createMultipartFromBase64() {
        // Decode the Base64 image string

        byte[] decodedImage = Base64.decode(base64Image, Base64.DEFAULT);

        // Create a RequestBody from the decoded image bytes
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), decodedImage);

        // Create a MultipartBody.Part using the RequestBody
        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody);
    }*/


    private void apiUploadPassport() {
        String dataImage = "data:image/jpeg;base64,";
        String imageBase64 = dataImage + base64Image;
        done.setVisibility(View.GONE);

        ApiInterface api = Api.CreateNodeApi();
       /* PassportRequest passportRequest = new PassportRequest();
        passportRequest.setPassport(imageBase64);*/
        JsonObject object = new JsonObject();
        object.addProperty("passport",imageBase64);

        progressBar.setVisibility(View.VISIBLE);
        final Call<PassportModel> passportModelCall = api.getPassport(object);
        Log.d(TAG, "onWithPassport:   " +object);
        passportModelCall.enqueue(new Callback<PassportModel>() {
            @Override
            public void onResponse(@NonNull Call<PassportModel> call, @NonNull Response<PassportModel> response) {
                Log.d(TAG, "onWithFred:   " +response);

             if(response.isSuccessful() && response.errorBody() == null){
                 assert response.body() != null;
                 if((response.body().getStatus_code() == 200)){

                     Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                     intent.putExtra("message", response.body().getMessage());
                     startActivity(intent);

                     Toast.makeText(PhotoCaptureActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                 }else {
                     done.setVisibility(View.VISIBLE);
                     progressBar.setVisibility(View.GONE);
                     Toast.makeText(PhotoCaptureActivity.this, "Failed to Upload please try again", Toast.LENGTH_SHORT).show();

                 }
             }

                if (response.code() == 422) {
                    done.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PhotoCaptureActivity.this, "Something went wrong! please try again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<PassportModel> call, @NonNull Throwable t) {
               t.printStackTrace();
                Log.d("Throwable  ------- > ", t.toString());
                done.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        });


    }
}