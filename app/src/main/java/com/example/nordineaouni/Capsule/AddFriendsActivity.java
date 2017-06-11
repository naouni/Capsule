package com.example.nordineaouni.Capsule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFriendsActivity extends AppCompatActivity {


    String TAG = getClass().toString();
    AddFriendsAdapter adapter;
    FirebaseAuth auth;
    Map <String, String> contacts;
    DatabaseReference contactsRef;
    EditText addFriendsField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        auth = FirebaseAuth.getInstance();
        contactsRef = FirebaseDatabase.getInstance().getReference().child("contacts").child(auth.getCurrentUser().getUid());
        contactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contacts = (HashMap<String,String>) dataSnapshot.getValue();;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new AddFriendsAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.addFriendsRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        //TODO: Add the selected user to contacts
                        User friend = adapter.getUser(position);
                        String friendId = friend.getUserId();
                        String friendUserName = friend.getUserName();

                        if(contacts.containsKey(friendId)){
                            Toast.makeText(getBaseContext(), getString(R.string.youAreAlreadyFriendWith)+" "+friendUserName, Toast.LENGTH_SHORT).show();
                        }else{
                            contactsRef.child(friendId).setValue(friendUserName);
                            Toast.makeText(getBaseContext(), getString(R.string.friendAdded), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        addFriendsField = (EditText) findViewById(R.id.addFriendsSearch);
        addFriendsField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                adapter.filter(addFriendsField.getText().toString());
            }
        });

    }
}
