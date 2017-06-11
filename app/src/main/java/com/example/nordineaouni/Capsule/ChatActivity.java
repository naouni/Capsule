package com.example.nordineaouni.Capsule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nordineaouni on 01/03/17.
 */

public class ChatActivity extends AppCompatActivity {


    private String TAG = getClass().toString();
    private EditText writeCapsuleField;
    //Tools to get current date and
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //Capsule metadata
    String text;
    String senderId;
    String sentDate;
    int year;
    int month;
    int day;
    //Firebase
    private FirebaseAuth auth;
    private DatabaseReference numberCapsulesRef;
    private DatabaseReference convContentsRef;
    private DatabaseReference conversationsListRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        //Get the intent that started this activity(see for instance ChatsListFragment.onCreateView())
        Intent intent = getIntent();
        final String conversationID = intent.getStringExtra("conversationID");//The id of the displayed conversation

        auth = FirebaseAuth.getInstance();
        numberCapsulesRef = FirebaseDatabase.getInstance().getReference().child("conversations").child(auth.getCurrentUser().getUid()).child(conversationID).child("numberCapsules");
        convContentsRef = FirebaseDatabase.getInstance().getReference().child("conversationsContents").child(auth.getCurrentUser().getUid()).child(conversationID);
        conversationsListRef = FirebaseDatabase.getInstance().getReference().child("conversations").child(auth.getCurrentUser().getUid());

        //Add a listener on the list of conversations
        conversationsListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (! snapshot.hasChild(conversationID)) {
                    // The conversation does not exist yet (in the database). So we instantiate it.

                    String dateLastSent = "";
                    String closestOpening = "";
                    String interlocutorId = conversationID;
                    Long conversationNumber = 0L;
                    Conversation conversation = new Conversation(dateLastSent, closestOpening, interlocutorId, conversationNumber);
                    conversationsListRef.child(conversationID).setValue(conversation);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        ChatAdapter chatAdapter = new ChatAdapter(conversationID);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);

        writeCapsuleField = (EditText) findViewById(R.id.writeCapsuleField);

        Button sendButton = (Button) findViewById(R.id.sendCapsuleButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendButtonClicked();
            }
        });
    }


    public void onSendButtonClicked(){
        //Open date picker
        DialogFragment datePickerFragment = new DatePickerFragment(this);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onDateSet(int year, int month, int day){
        //Gets the year,month and day from the date picker that used its callback's function
        this.year = year;
        this.month = month;
        this.day = day;
        DialogFragment timePickerFragment =  new TimePickerFragment(this);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void sendCapsule(int hourOfDay, int minute){

        //Set Capsule metadata
        //Gets the hourOfDay and minute from the timePicker that used its callback's function
        text = writeCapsuleField.getText().toString();
        senderId = auth.getCurrentUser().getUid();
        sentDate = dateFormat.format(calendar.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hourOfDay, minute);
        Date openingDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String openingDateString = dateFormat.format(openingDate);

        //Create the new capsule
        Capsule capsule = new Capsule(text, senderId, sentDate, openingDateString);

        //Increment the total number of capsules in this conversation
        convContentsRef.push().setValue(capsule);
        incrementNumberCapsules(numberCapsulesRef);

        writeCapsuleField.setText("");//reset text field's value to an empty string
    }

    //Increment by 1 the number of capsule in this conversation
    private void incrementNumberCapsules(DatabaseReference numberCapsulesRef) {
        numberCapsulesRef.runTransaction(new Transaction.Handler() {
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
