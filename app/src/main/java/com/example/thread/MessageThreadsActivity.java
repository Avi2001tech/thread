package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MessageThreadsActivity extends AppCompatActivity {
    private Button buttonOpenConversation;
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
                Intent intent = new Intent(MessageThreadsActivity.this, ConversationActivity.class);
                startActivity(intent);
            }
        });
    }
}