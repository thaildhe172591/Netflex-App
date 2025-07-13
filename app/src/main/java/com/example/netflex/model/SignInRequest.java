package com.example.netflex.model;

public class SignInRequest {
    private String email;
    private String password;
    private String deviceId;

    public SignInRequest(String email, String password, String deviceId) {
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getDeviceId() { return deviceId; }
}
