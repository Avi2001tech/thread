package com.example.thread.api;

import com.example.thread.model.AuthTokenResponse;
import com.example.thread.model.LoginRequest;
import com.example.thread.model.Message;
import com.example.thread.model.MessageRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/login")
    Call<AuthTokenResponse> login(@Body LoginRequest request);

    // Get messages
    @GET("api/messages")
    Call<List<Message>> getAllMessages();

    // new message
    @POST("api/messages")
    Call<Message> createNewMessage(@Body MessageRequest request);

    // Reset messages
    @POST("api/reset")
    Call<Void> resetMessages();
}
