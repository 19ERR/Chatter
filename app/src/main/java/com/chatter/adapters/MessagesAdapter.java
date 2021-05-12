package com.chatter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chatter.R;
import com.chatter.classes.Message;
import com.chatter.classes.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

//TODO: BUFFER GLOBAL IN ACTIVITATE PENTRU A TRIMITE TEXT SI POZA IN ACELAS TIMP
public class MessagesAdapter extends FirebaseRecyclerAdapter<Message, MessagesAdapter.ViewHolder> {
    Context context;
    RecyclerView recyclerView;
    public MessagesAdapter(@NonNull FirebaseRecyclerOptions<Message> options, Context context, RecyclerView recyclerView) {
        super(options);
        this.context = context;
        this.recyclerView = recyclerView;
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
        viewHolder.getTextViewMessageContent().setText(message.getTextContent());

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

        recyclerView.smoothScrollToPosition(getItemCount());

        if(!message.getMediaKey().isEmpty()) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference imageRef = storage.getReference().child(message.getMediaKey());

            File localFile = null;
            try {
                localFile = File.createTempFile("images", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            File finalLocalFile = localFile;
            imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap img = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                    viewHolder.getImageView().setImageBitmap(img);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

    }

    private void makeOpponent(ViewHolder holder) {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) holder.relativeLayout.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        holder.relativeLayout.setLayoutParams(params);
    }

    private void makeOwn(ViewHolder holder) {
        holder.textViewMessageSender.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) holder.relativeLayout.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        holder.relativeLayout.setLayoutParams(params);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textViewMessageContent;
        private final TextView textViewMessageSender;
        private final TextView textViewMessageTimestamp;
        private final CardView cardView;
        private RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            textViewMessageContent = view.findViewById(R.id.textViewMessageContent);
            textViewMessageSender = view.findViewById(R.id.textViewMessageSender);
            textViewMessageTimestamp = view.findViewById(R.id.textViewMessageTimestamp);
            cardView = view.findViewById(R.id.card_view_message);
            relativeLayout = itemView.findViewById(R.id.layout_message_view);
        }

        public ImageView getImageView() {return imageView;}
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