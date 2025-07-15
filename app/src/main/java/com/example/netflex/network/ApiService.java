package com.example.netflex.network;

import com.example.netflex.model.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/notifications/user/{userId}")
    Call<List<Notification>> getNotificationsForUser(@Path("userId") String userId);
}
