package com.chatter.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.classes.Message;
import com.chatter.classes.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessagesAdapter extends FirebaseRecyclerAdapter<Message, MessagesAdapter.ViewHolder> {
    Context context;

    public MessagesAdapter(@NonNull FirebaseRecyclerOptions<Message> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Message message) {
        viewHolder.getTextViewMessageSender().setText(message.getSenderEmail());
        viewHolder.getTextViewMessageContent().setText(message.getContent());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        viewHolder.getTextViewMessageTimestamp().setText(dateFormat.format(message.getTimestamp()));
        viewHolder.itemView.setOnClickListener(v -> {
        });
        if (User.getEmail().equals(message.getSenderEmail())) {
            //la dreapta
           makeOwn(viewHolder);
        } else {
            //la stanga
            makeOpponent(viewHolder);
        }
    }

    private void makeOpponent(ViewHolder holder) {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) holder.itemBOdy.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        holder.itemBOdy.setLayoutParams(params);
    }

    private void makeOwn(ViewHolder holder) {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) holder.itemBOdy.getLayoutParams();
        //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        holder.itemBOdy.setLayoutParams(params);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewMessageContent;
        private final TextView textViewMessageSender;
        private final TextView textViewMessageTimestamp;
        private final CardView cardView;
        private RelativeLayout itemBOdy;

        public ViewHolder(View view) {
            super(view);
            textViewMessageContent = view.findViewById(R.id.textViewMessageContent);
            textViewMessageSender = view.findViewById(R.id.textViewMessageSender);
            textViewMessageTimestamp = view.findViewById(R.id.textViewMessageTimestamp);
            cardView = view.findViewById(R.id.card_view_message);
            itemBOdy = itemView.findViewById(R.id.layout_message_view);
        }

        public CardView getCardView() {
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