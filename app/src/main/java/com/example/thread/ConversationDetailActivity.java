package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.thread.adapters.MessageListAdapter;
import com.example.thread.api.ApiService;
import com.example.thread.model.Message;
import com.example.thread.model.MessageRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConversationDetailActivity extends AppCompatActivity {

    ListView messageListView;
     EditText responseEditText;
     Button sendButton, resetButton;
    MessageListAdapter messageListAdapter;
    int threadId;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_detail);

        threadId = getIntent().getIntExtra("threadId", -1);

        messageListView = findViewById(R.id.messageListView);
        responseEditText = findViewById(R.id.responseEditText);
        sendButton = findViewById(R.id.sendButton);
        resetButton = findViewById(R.id.resetButton);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android-messaging.branch.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        messageListAdapter = new MessageListAdapter(this, new ArrayList<>());
        messageListView.setAdapter(messageListAdapter);

        // Send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String response = responseEditText.getText().toString().trim();
                if (!response.isEmpty()) {
                    sendMessage(threadId, response);
                }
            }
        });

        //reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetMessages();
            }
        });

        loadMessages(); //load all messages
    }

    //send message function
    private void sendMessage(int threadId, String message) {
        MessageRequest messageRequest = new MessageRequest(threadId, message);
        Call<Message> call = apiService.createNewMessage("your-auth-token", messageRequest);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    Message sentMessage = response.body();
                    messageListAdapter.addMessage(sentMessage);
                    responseEditText.setText("");
                } else {
                    handleApiError(response.code());
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                handleNetworkError(t);
            }
        });
    }



  //reset function
    private void resetMessages() {
        Call<Void> call = apiService.resetMessages();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    messageListAdapter.clearMessages();
                    showMessage("Messages reset successfully");
                } else {
                    handleApiError(response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network errors
                handleNetworkError(t);
            }
        });
    }



    //load message
    private void loadMessages() {
        Call<List<Message>> call = apiService.getMessagesByThreadId("your-auth-token", threadId);

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    // Handle the successful response and update the message list
                    List<Message> messages = response.body();
                    messageListAdapter.setMessages(messages);
                } else {
                    handleApiError(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                handleNetworkError(t);
            }
        });
    }

    private void handleApiError(int statusCode) {
        String errorMessage;

        switch (statusCode) {
            case 401:
                errorMessage = "Unauthorized: Please log in again.";
                break;
            case 404:
                errorMessage = "Resource not found.";
                break;
            default:
                errorMessage = "An error occurred. Please try again later.";
                break;
        }

        showMessage(errorMessage);
    }

    private void handleNetworkError(Throwable t) {
        String errorMessage;

        if (t instanceof java.net.SocketTimeoutException) {
            errorMessage = "Network timeout. Please check your internet connection.";
        } else if (t instanceof java.net.UnknownHostException) {
            errorMessage = "No internet connection. Please connect to a network.";
        } else {
            errorMessage = "Network error. Please try again later.";
        }

        showMessage(errorMessage);
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

}