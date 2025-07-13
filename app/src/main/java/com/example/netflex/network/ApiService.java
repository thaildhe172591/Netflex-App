package com.example.netflex.network;

import com.example.netflex.model.SignInRequest;
import com.example.netflex.model.SignInResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/signin")
    Call<SignInResponse> signIn(@Body SignInRequest request);
}
