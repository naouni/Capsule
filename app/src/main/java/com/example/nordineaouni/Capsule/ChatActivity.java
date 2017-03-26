package com.example.nordineaouni.Capsule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by nordineaouni on 01/03/17.
 */

public class ChatActivity extends AppCompatActivity {


    private String TAG = getClass().toString();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChatAdapter chatAdapter;
    private EditText writeCapsuleField;
    private Capsule capsule;
    //Tools to get current date and
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //Capsule metadata
    String text;
    String senderId;
    String sentDate;
    String openingDate;
    //Firebase
    private FirebaseAuth auth;
    private DatabaseReference numberCapsulesRef;
    private DatabaseReference convContentsRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        //Get the intent that started this activity(see ChatsListFragment.onCreateView())
        Intent intent = getIntent();
        String conversationID = intent.getStringExtra("conversationID");//The id of the displayed conversation

        auth = FirebaseAuth.getInstance();
        numberCapsulesRef = FirebaseDatabase.getInstance().getReference().child("conversations").child(auth.getCurrentUser().getUid()).child(conversationID).child("numberCapsules");
        convContentsRef = FirebaseDatabase.getInstance().getReference().child("conversationsContents").child(auth.getCurrentUser().getUid()).child(conversationID);

        recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        layoutManager = new LinearLayoutManager(this);

        chatAdapter = new ChatAdapter(conversationID);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);

        writeCapsuleField = (EditText) findViewById(R.id.writeCapsuleField);

        Button button = (Button) findViewById(R.id.sendCapsuleButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCapsule();
            }
        });
    }


    public void sendCapsule(){

        text = writeCapsuleField.getText().toString();
        senderId = auth.getCurrentUser().getUid();
        sentDate = dateFormat.format(calendar.getTime());
        openingDate = new String(sentDate);//Todo: let the user choose

        capsule = new Capsule(text, senderId, sentDate, openingDate);

        //TODO: Update the number of capsules counts
        incrementNumberCapsules(numberCapsulesRef);

        convContentsRef.push().setValue(capsule);

        //TODO: Reset the textfield to an empty String
        writeCapsuleField.setText("");
    }

    private void incrementNumberCapsules(DatabaseReference numberCqpsulesRef) {
        numberCqpsulesRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                long numberCapsules =  mutableData.getValue(long.class);
                if (mutableData.getValue(long.class) == null) {
                    return Transaction.success(mutableData);
                }

                //Increment the variable keeping track of the number of capsules in this conversations
                numberCapsules++;

                // Set value and report transaction success
                mutableData.setValue(numberCapsules);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "incrementNumberCapsulesTransaction:onComplete:" + databaseError);
            }
        });
    }
}
