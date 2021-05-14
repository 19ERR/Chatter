package com.chatter.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.adapters.MessagesAdapter;
import com.chatter.classes.Conversation;
import com.chatter.classes.Message;
import com.chatter.classes.User;
import com.chatter.viewModels.MessagesViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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

//TODO: incarcarea a mai multe elemente la scroll in sus
public class ConversationActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    MessagesAdapter messagesAdapter;
    String conversationKey;
    MessagesViewModel messagesViewModel;
    Observer<ArrayList<Message>> messagesListUpdateObserver = new Observer<ArrayList<Message>>() {
        @Override
        public void onChanged(ArrayList<Message> messagesArrayList) {
            messagesAdapter.notifyDataSetChanged();
        }
    };
    Conversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        conversationKey = getIntent().getStringExtra("conversation_key");
        conversation = User.getConversation(conversationKey);

        Toolbar toolbar = findViewById(R.id.toolbar_conversation_list);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Conversatii");
        }

        FloatingActionButton buttonSendMessage = findViewById(R.id.buttonSendMessage);
        buttonSendMessage.setOnClickListener(v -> {
            EditText inputEditTextMessage = findViewById(R.id.editTextMessage);
            String messageContent = inputEditTextMessage.getText().toString();

            Message newMessage = new Message(messageContent, null, User.getEmail());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference convRef = database.getReference().child("messages").child(conversationKey).push();
            convRef.setValue(newMessage);
            inputEditTextMessage.setText("");
        });

        FloatingActionButton buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        buttonTakePhoto.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycle_message_list);

        messagesAdapter = new MessagesAdapter(conversation.getMessages());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messagesAdapter);
        recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount());
        ((LinearLayoutManager) recyclerView.getLayoutManager()).setStackFromEnd(true);
        //adauga observer pentru lista de conversatii
        messagesViewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);
        messagesViewModel.setMessagesLiveData(conversation.getMessages());
        messagesViewModel.getMessagesLiveData().observe(this, messagesListUpdateObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void uploadPhoto(Bitmap imageBitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //referinta la unde se va salva mesajul
        DatabaseReference newMessageRef = database.getReference().child("messages").child(conversationKey).push();
        //referinta la unde se va salva poza care va avea cheia mesajului ca denumire
        StorageReference imageRef = storageRef.child("images").child(newMessageRef.getKey());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(bytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    //genereaza uri
                    return imageRef.getDownloadUrl();
                }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Message newMessage = new Message("", imageRef.getPath(), User.getEmail());
                        newMessageRef.setValue(newMessage);
                    } else {
                        // Handle failures
                        // ...
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uploadPhoto(imageBitmap);

        }
    }
}