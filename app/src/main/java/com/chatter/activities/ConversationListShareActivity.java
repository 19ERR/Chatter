package com.chatter.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chatter.R;
import com.chatter.adapters.ConversationsShareAdapter;

//todo: rezolvat cu redimensionarea imaginii din share
public class ConversationListShareActivity extends Activity{

    ConversationsShareAdapter conversationsAdapter;
    RecyclerView recyclerView;
    Bitmap imageToShare;
    String linkToShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list_share);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        }

        conversationsAdapter = new ConversationsShareAdapter(imageToShare, linkToShare);
        recyclerView = findViewById(R.id.recycle_conversation_list_share);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(conversationsAdapter);
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
           this.linkToShare = sharedText;
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            try
            {
                ImageView imageView = findViewById(R.id.imageView2);
                this.imageToShare = MediaStore.Images.Media.getBitmap(this.getContentResolver() , Uri.parse(imageUri.toString()));
                imageView.setImageBitmap(this.imageToShare);
            }
            catch (Exception e)
            {
                //handle exception
            }
        }
    }

}