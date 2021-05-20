package com.chatter.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.chatter.DAO.ChatterDatabase;
import com.chatter.DAO.MediaDAO;
import com.chatter.R;
import com.chatter.activities.MapsActivity;
import com.chatter.adapters.MessagesAdapter;
import com.chatter.classes.Conversation;
import com.chatter.classes.Location;
import com.chatter.classes.Media;
import com.chatter.classes.Message;
import com.chatter.classes.User;
import com.chatter.viewModels.MessagesViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


//TODO: incarcarea doar a unei parti din elemente
//TODO: incarcarea a mai multe elemente la scroll in sus

public class MessagesFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_LOCATION = 10;

    Conversation conversation;
    MessagesAdapter messagesAdapter;
    MessagesViewModel messagesViewModel;
    RecyclerView recyclerView;

    Observer<ArrayList<Message>> messagesListUpdateObserver = new Observer<ArrayList<Message>>() {
        @Override
        public void onChanged(ArrayList<Message> messagesArrayList) {
            messagesAdapter.notifyItemInserted(conversation.getMessages().getValue().size() -1);
            recyclerView.smoothScrollToPosition(conversation.getMessages().getValue().size() -1);
        }
    };

    public MessagesFragment() {
        super(R.layout.fragment_conversation_messages);
    }

    public MessagesFragment(String conversationKey) {
        super(R.layout.fragment_conversation_messages);
        this.conversation = User.getConversation(conversationKey);

    }

    public static MessagesFragment newInstance(String conversationKey) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putString("conversationKey", conversationKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.conversation = User.getConversation(getArguments().getString("conversationKey"));
        }

    }

    private void sendPhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    private void sendLocation(){
        Intent sendLocationIntent = new Intent(this.getContext(), MapsActivity.class);
        try {
            startActivityForResult(sendLocationIntent, REQUEST_LOCATION);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    private void sendMessage(){
        EditText inputEditTextMessage = getView().findViewById(R.id.editTextMessage);
        String messageContent = inputEditTextMessage.getText().toString();

        Message newMessage = new Message(messageContent);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference convRef = database.getReference().child("messages").child(conversation.getKey()).push();
        convRef.setValue(newMessage);
        inputEditTextMessage.setText("");
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        FloatingActionButton buttonSendMessage = view.findViewById(R.id.buttonSendMessage);
        buttonSendMessage.setOnClickListener(v -> sendMessage());

        FloatingActionButton buttonSendOthers = view.findViewById(R.id.buttonSendOthers);
        buttonSendOthers.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this.getContext(), v);
            MenuInflater inflater = popup.getMenuInflater();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.itemSendPhoto:
                           sendPhoto();
                           break;
                        case R.id.itemSendLocation:
                            sendLocation();
                            break;
                    }
                    return true;
                }
            });
            inflater.inflate(R.menu.conversation_send_additional_items, popup.getMenu());
            popup.show();
        });

        recyclerView = view.findViewById(R.id.recycle_message_list);
        messagesAdapter = new MessagesAdapter(conversation.getMessages().getValue());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(messagesAdapter);
        recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());
        ((LinearLayoutManager) recyclerView.getLayoutManager()).setStackFromEnd(true);
        //adauga observer pentru lista de conversatii
        messagesViewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);
        messagesViewModel.setMessagesLiveData(conversation.getMessages());
        messagesViewModel.getMessagesLiveData().observe(getViewLifecycleOwner(), messagesListUpdateObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_conversation_messages, container, false);
    }


    private void uploadPhoto(Bitmap imageBitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //referinta la unde se va salva mesajul
        DatabaseReference newMessageRef = database.getReference().child("messages").child(conversation.getKey()).push();
        //referinta la unde se va salva poza care va avea cheia mesajului ca denumire
        StorageReference imageRef = storageRef.child("images").child(newMessageRef.getKey());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] mediaBytes = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(mediaBytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    //genereaza uri
                    return imageRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Message newMessage = new Message(imageRef.getPath(), true);
                        newMessageRef.setValue(newMessage);

                        Media media = new Media(imageRef.getPath(), 0, mediaBytes);
                        saveMedia(media);
                    } else {
                        // Handle failures
                        // ...
                    }
                });

            }
        });
    }

    //salvarea fisierelor media in baza de date room
    private void saveMedia(Media media) {
        Context context = getContext();
        assert context != null;
        ChatterDatabase db = Room.databaseBuilder(context,
                ChatterDatabase.class, "media-database").build();

        MediaDAO mediaDAO = db.mediaDAO();
        mediaDAO.insertMedia(media);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uploadPhoto(imageBitmap);
        }
        if (requestCode == REQUEST_LOCATION && resultCode == 1) {
            Bundle extras = data.getExtras();
            LatLng location = (LatLng) extras.get("selectedLocation");
            sendLocationMessage(location);
        }
    }

    private void sendLocationMessage(LatLng coordonates){
        Message newMessage = new Message(new Location(coordonates));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference convRef = database.getReference().child("messages").child(conversation.getKey()).push();
        convRef.setValue(newMessage);
    }
}