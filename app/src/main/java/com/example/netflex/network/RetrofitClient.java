package com.example.netflex.network;

import android.content.Context;

import com.example.netflex.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = Constants.BASE_URL;
    private static Retrofit retrofit;

    public static ApiService getApiService(Context context) {
        TokenManager tokenManager = new TokenManager(context);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(tokenManager))
                .authenticator(new TokenAuthenticator(tokenManager))
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}

