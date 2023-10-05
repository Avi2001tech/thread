package com.example.thread.model;

import com.google.gson.annotations.SerializedName;

public class MessageRequest {
    @SerializedName("thread_id")
    private int threadId;

    @SerializedName("body")
    private String body;

    public MessageRequest(int threadId, String body) {
        this.threadId = threadId;
        this.body = body;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
