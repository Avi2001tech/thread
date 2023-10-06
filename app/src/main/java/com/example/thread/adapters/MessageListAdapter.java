package com.example.thread.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thread.R;
import com.example.thread.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messages;

    public MessageListAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = new ArrayList<>();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        }

        TextView senderTextView = convertView.findViewById(R.id.textViewSender);
        TextView timestampTextView = convertView.findViewById(R.id.textViewTimestamp);
        TextView messageTextView = convertView.findViewById(R.id.textViewMessage);

        Message message = messages.get(position);

        // Populate the list item views with data
        senderTextView.setText("Sender: " + (message.isSentByAgent() ? "Agent " + message.getAgentId() : "User " + message.getUserId()));
        timestampTextView.setText("Timestamp: " + message.getTimestamp());
        messageTextView.setText(message.getBody());

        return convertView;
    }

    public void clearMessages() {
        messages.clear();
        notifyDataSetChanged();
    }
}

