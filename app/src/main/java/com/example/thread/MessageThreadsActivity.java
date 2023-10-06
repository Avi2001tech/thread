package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thread.adapters.MessageListAdapter;
import com.example.thread.api.ApiService;
import com.example.thread.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageThreadsActivity extends AppCompatActivity {
     private Button buttonOpenConversation;
    private ListView threadListView;
    private SharedPreferences sharedPreferences;
    private MessageListAdapter messageListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_threads);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Message Threads");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        buttonOpenConversation = findViewById(R.id.buttonOpenConversation);

        buttonOpenConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageThreadsActivity.this, ConversationDetailActivity.class);
                startActivity(intent);
            }
        });


            sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            threadListView = findViewById(R.id.threadListView);

        threadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message selectedMessage = (Message) parent.getItemAtPosition(position);

                Intent intent = new Intent(MessageThreadsActivity.this, ConversationDetailActivity.class);
                intent.putExtra("threadId", selectedMessage.getThreadId());
                startActivity(intent);
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://android-messaging.branch.co/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);

            String authToken = sharedPreferences.getString("auth_token", "");

            Call<List<Message>> call = apiService.getAllMessages(authToken);

            call.enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    if (response.isSuccessful()) {
                        List<Message> messages = response.body();
                        messageListAdapter = new MessageListAdapter(MessageThreadsActivity.this, messages
                        );
                        threadListView.setAdapter(messageListAdapter);
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
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
}