package com.example.thread.api;

import com.example.thread.model.AuthTokenResponse;
import com.example.thread.model.LoginRequest;
import com.example.thread.model.Message;
import com.example.thread.model.MessageRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/login")
    Call<AuthTokenResponse> login(@Body LoginRequest request);

    // Get messages
    @GET("api/messages")
    Call<List<Message>> getAllMessages(@Header("Authorization") String authToken);

    // new message
    @POST("api/messages")
    Call<Message> createNewMessage(String s, @Body MessageRequest request);

    // Reset messages
    @POST("api/reset")
    Call<Void> resetMessages();

    // Get messages by thread ID
    @GET("api/messages/{threadId}")
    Call<List<Message>> getMessagesByThreadId(@Header("Authorization") String authToken, @Path("threadId") int threadId);

}
