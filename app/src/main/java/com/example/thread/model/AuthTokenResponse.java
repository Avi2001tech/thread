package com.example.thread.model;

import com.google.gson.annotations.SerializedName;

public class AuthTokenResponse {
    @SerializedName("auth_token")
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }
}
