package com.example.netflex.network;

import androidx.annotation.*;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.*;

public class TokenAuthenticator implements Authenticator {
    private TokenManager tokenManager;
    private final String BASE_URL = "https://api.cukhoaito.id.vn/";
    public TokenAuthenticator(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (responseCount(response) >= 2) return null;
        String refreshToken = tokenManager.getRefreshToken();
        if (refreshToken == null) return null;

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(
                "{\"refreshToken\":\"" + refreshToken + "\"}",
                MediaType.get("application/json")
        );

        Request refreshRequest = new Request.Builder()
                .url(BASE_URL + "api/v1/auth/refresh")
                .post(body)
                .build();

        Response refreshResponse = client.newCall(refreshRequest).execute();
        try {
            if (refreshResponse.isSuccessful()) {
                String responseBody = refreshResponse.body().string();
                JSONObject json = new JSONObject(responseBody);
                String newAccessToken = json.getString("accessToken");
                String newRefreshToken = json.getString("refreshToken");

                tokenManager.saveTokens(newAccessToken, newRefreshToken);
                return response.request().newBuilder()
                        .header("Authorization", "Bearer " + newAccessToken)
                        .build();
            } else {
                tokenManager.clearTokens();
                return null;
            }
        }catch (Exception e){
            tokenManager.clearTokens();
            return null;
        }
    }

    private int responseCount(Response response) {
        int count = 1;
        while ((response = response.priorResponse()) != null) {
            count++;
        }
        return count;
    }
}
