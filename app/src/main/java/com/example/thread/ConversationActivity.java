package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.thread.adapters.MessageAdapter;
import com.example.thread.api.ApiClient;
import com.example.thread.api.ApiService;
import com.example.thread.model.Message;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationActivity extends AppCompatActivity {
    RecyclerView recyclerViewMessages;
    MessageAdapter messageAdapter;
    List<Message> messageList;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Conversation");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        apiService = ApiClient.getClient().create(ApiService.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);
        fetchMessages();
    }

    private void fetchMessages() {
        Call<List<Message>> call = apiService.getAllMessages();
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    List<Message> messageList = response.body();
                    messageAdapter = new MessageAdapter(messageList);
                    recyclerViewMessages.setAdapter(messageAdapter);
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                handleNetworkError(t);
            }
        });
    }

    private void handleApiError(Response<List<Message>> response) {
        int statusCode = response.code();
        switch (statusCode) {
            case 401:
                break;
            case 403:
                break;
            default:
                String errorMessage = "API Error: " + response.message();
                Toast.makeText(ConversationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void handleNetworkError(Throwable t) {
        Toast.makeText(ConversationActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}