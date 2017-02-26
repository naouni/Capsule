package com.example.nordineaouni.TimeCapsule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull; //Should not be deleted
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



/**
 * Created by nordineaouni on 19/01/17.
 */

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = getClass().toString();

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent mIntent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(mIntent);

                } else {

                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        mEmailField = (EditText) findViewById(R.id.emailEditText);
        mPasswordField = (EditText) findViewById(R.id.passwordEditText);

        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    public void onClick(final View v) {

        final String email = mEmailField.getText().toString();
        final  String password = mPasswordField.getText().toString();

        switch (v.getId()) {

            case R.id.signUpButton:
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {

                                    Toast.makeText(LogInActivity.this, R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            case R.id.signInButton:
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(LogInActivity.this, R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
        }
    }
}