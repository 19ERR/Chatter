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
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.chatter.DAO.ChatterDatabase;
import com.chatter.DAO.MediaDAO;
import com.chatter.R;
import com.chatter.classes.Media;
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
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Completable;

//TODO: BUFFER GLOBAL IN ACTIVITATE PENTRU A TRIMITE TEXT SI POZA IN ACELAS TIMP
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>{

    private final ArrayList<Message> messages;
    public MessagesAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.getTextViewMessageSender().setText(messages.get(position).getSenderEmail());
        viewHolder.getTextViewMessageContent().setText(messages.get(position).getTextContent());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        viewHolder.getTextViewMessageTimestamp().setText(dateFormat.format(messages.get(position).getTimestamp()));
        viewHolder.itemView.setOnClickListener(v -> {
        });
        if (User.getEmail().equals(messages.get(position).getSenderEmail())) {
            //la dreapta
           makeOwn(viewHolder);
        } else {
            //la stanga
            makeOpponent(viewHolder);
        }

        if(messages.get(position).getMediaKey()!=null) {
            //verificare daca exista in room
            //daca exista afiseaza
            //daca nu exista, descarca, salveaza si afiseaza
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Context context = viewHolder.itemView.getContext();
                    ChatterDatabase db = Room.databaseBuilder(context, ChatterDatabase.class, "media-database").build();
                    MediaDAO mediaDAO = db.mediaDAO();
                    Media media = mediaDAO.getByLink(messages.get(position).getMediaKey());
                    if(media !=null){
                        //daca exista local
                        Bitmap img = BitmapFactory.decodeByteArray(media.data, 0, media.data.length);
                        viewHolder.getImageView().setImageBitmap(img);
                    } else {
                        getMediaFromFirebase(messages.get(position).getMediaKey(), viewHolder);
                    }

                }
            });
        }

    }

    public void getMediaFromFirebase(String mediaLink, MessagesAdapter.ViewHolder viewHolder){
        //descarcare din firebase si salvare in local
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child(mediaLink);

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
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] mediaBytes = baos.toByteArray();

                Media media = new Media(imageRef.getPath(), 0, mediaBytes);
                saveMedia(media, viewHolder.itemView.getContext());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }

    //salvarea fisierelor media in baza de date room
    private void saveMedia(Media media, Context context) {
        ChatterDatabase db = Room.databaseBuilder(context,
                ChatterDatabase.class, "media-database").build();

        MediaDAO mediaDAO = db.mediaDAO();
        mediaDAO.insertMedia(media);
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
        private final RelativeLayout relativeLayout;

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