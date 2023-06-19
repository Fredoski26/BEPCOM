package com.example.bepcom.network;
import com.example.bepcom.model.BiometricLoginModel;
import com.example.bepcom.model.FingerprintModel;
import com.example.bepcom.model.LoginModel;
import com.example.bepcom.model.PassportModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("api/v1/enroll")
    Call<FingerprintModel> getFingerprint(@Body JsonObject data);

    @POST("api/v1/uploadPassport")
    Call<PassportModel> getPassport(@Body JsonObject data);

    @POST("api/v1/loginUser")
    Call<LoginModel> getLogin(@Body JsonObject data);

    @GET("loginUserWithFingerPrint")
    Call<BiometricLoginModel> getUserWithBiometric(@Body JsonObject data);
}
