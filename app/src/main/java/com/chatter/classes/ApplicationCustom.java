package com.chatter.classes;

import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class ApplicationCustom extends Application {

    private GoogleSignInAccount account;

    public GoogleSignInAccount getSomeVariable() {
        return account;
    }

    public void setSomeVariable(GoogleSignInAccount account) {
        this.account = account;
    }
}