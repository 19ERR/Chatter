package com.chatter.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.chatter.R;

public class InsertConversationNameDialog extends Dialog implements
        android.view.View.OnClickListener {

    String newConversationName;
    private Dialog dialog;

    public InsertConversationNameDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_insert_conversation_name);

        Button buttonSetTitle = findViewById(R.id.button_set_title);
        Button buttonCancel = findViewById(R.id.button_cancel_title);
        buttonSetTitle.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_set_title:
                setNewConversationName(v);
                break;
            case R.id.button_cancel_title:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void setNewConversationName(View v) {
        EditText editTextEmail = findViewById(R.id.edit_text_new_conversation_title);
        newConversationName = editTextEmail.getText().toString();

    }

}