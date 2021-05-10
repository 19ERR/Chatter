package com.chatter.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.classes.Message;
import com.chatter.classes.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private final ArrayList<Message> messages;

    public MessagesAdapter(ArrayList<Message> dataSet) {
        messages = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextViewMessageSender().setText(messages.get(position).getSenderEmail());
        viewHolder.getTextViewMessageContent().setText(messages.get(position).getContent());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        viewHolder.getTextViewMessageTimestamp().setText(dateFormat.format(messages.get(position).getTimestamp()));
        viewHolder.itemView.setOnClickListener(v -> {
        });
        if (User.getEmail().equals(messages.get(position).getSenderEmail())) {

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewMessageContent;
        private final TextView textViewMessageSender;
        private final TextView textViewMessageTimestamp;
        private final CardView cardView;

        public ViewHolder(View view) {
            super(view);
            textViewMessageContent = view.findViewById(R.id.textViewMessageContent);
            textViewMessageSender = view.findViewById(R.id.textViewMessageSender);
            textViewMessageTimestamp = view.findViewById(R.id.textViewMessageTimestamp);
            cardView = view.findViewById(R.id.card_view_message);
        }

        public CardView getLinearLayout() {
            return cardView;
        }

        public TextView getTextViewMessageContent() {
            return textViewMessageContent;
        }

        public TextView getTextViewMessageSender() {
            return textViewMessageSender;
        }

        public TextView getTextViewMessageTimestamp() {
            return textViewMessageTimestamp;
        }
    }
}