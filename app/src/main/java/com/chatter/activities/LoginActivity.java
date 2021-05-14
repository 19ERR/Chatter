package com.chatter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.chatter.R;
import com.chatter.classes.Contact;
import com.chatter.classes.Conversation;
import com.chatter.classes.Message;
import com.chatter.classes.User;
import com.chatter.dialogs.RegisterDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements RegisterDialog.finishRegisterDialogListener {
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.buttonRegister).setOnClickListener(v -> register());
        findViewById(R.id.googleSignInButton).setOnClickListener(v -> signInGoogle());
        findViewById(R.id.buttonLogIn).setOnClickListener(v -> signInPassword());
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null) {
            User.setEmail(mUser.getEmail());
            userIsAuthenticated();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResultGoogle(task);
        }
    }

    private void handleSignInResultGoogle(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            assert account != null;
            User.setEmail(account.getEmail());

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                userIsAuthenticated();
                            } else {
                                userNotAuthenticated();
                            }
                        }
                    });
        } catch (ApiException e) {
            userNotAuthenticated();
        }
    }

    private void signInPassword() {
        EditText editTextEmailLogIn = findViewById(R.id.editTextEmailLogIn);
        EditText editTextPasswordLogIn = findViewById(R.id.editTextPasswordLogIn);

        String emailLogIn = editTextEmailLogIn.getText().toString();
        String passwordLogIn = editTextPasswordLogIn.getText().toString();
        if (emailLogIn.isEmpty()) {
            Toast.makeText(this, "Va rog sa introduceti un email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passwordLogIn.isEmpty()) {
            Toast.makeText(this, "Va rog sa introduceti parola!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailLogIn, passwordLogIn)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        assert user != null;
                        User.setEmail(user.getEmail());
                        userIsAuthenticated();
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInGoogle() {
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("186499421544-fh9ihal6cna3nn3e7me077ged45i6kts.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void userIsAuthenticated() {
        Intent intent = new Intent(this, ConversationsListActivity.class);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        DatabaseReference usersRef = database.getReference("users").child(FirebaseAuth.getInstance().getUid());

        //cauta utilizatorul in users
        Query query = usersRef.orderByChild("email").equalTo(User.getEmail()).limitToFirst(1);
        //cand in gaseste adauga listener pentru a lua valorile initiale
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot sn = snapshot.getChildren().iterator().next();

                } else {
                    DatabaseReference dbRef = database.getReference("users").child(FirebaseAuth.getInstance().getUid());
                    dbRef.child("email").setValue(User.getEmail());
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //listener pentru contacte
                DatabaseReference userContactsRef = database.getReference("users").child(FirebaseAuth.getInstance().getUid()).child("contacts");
                userContactsRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Contact newContact = snapshot.getValue(Contact.class);
                        assert newContact != null;
                        newContact.setKey(snapshot.getKey());
                        User.addContact(newContact);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        User.removeContact(snapshot.getKey());
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void userNotAuthenticated() {
    }

    private void register() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RegisterDialog registerDialog = RegisterDialog.newInstance();
        registerDialog.show(fragmentManager, "register_dialog");
    }

    @Override
    public void onFinishRegisterDialog(String newUserEmail) {
        User.setEmail(newUserEmail);
        Toast.makeText(this, "Hi, " + User.getEmail(), Toast.LENGTH_SHORT).show();
        userIsAuthenticated();
    }
}