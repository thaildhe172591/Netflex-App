package com.example.netflex.model;

import java.io.Serializable;

public class SignInResponse implements Serializable {
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
}
