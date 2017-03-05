package com.example.nordineaouni.TimeCapsule;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nordineaouni on 01/03/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    String TAG = getClass().toString();

    private String conversationID;
    private ArrayList<Capsule> capsulesList;
    private HashMap<String, String> contactsList;

    private FirebaseAuth auth;
    private DatabaseReference conversationsContentsRef;
    private DatabaseReference contactsRef;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView senderTextView;
        private TextView textTextView;

        public ViewHolder(View view, TextView senderName, TextView text) {
            super(view);
            senderTextView = senderName;
            textTextView = text;
        }
    }

    public ChatAdapter(String conversationID) {

        capsulesList = new ArrayList<Capsule>();
        this.conversationID = conversationID;
        auth = FirebaseAuth.getInstance();
        conversationsContentsRef = FirebaseDatabase.getInstance().getReference().child("conversationsContents").child(auth.getCurrentUser().getUid()).child(this.conversationID);
        contactsRef = FirebaseDatabase.getInstance().getReference().child("contacts").child(auth.getCurrentUser().getUid());

        conversationsContentsRef.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                Capsule capsule = dataSnapshot.getValue(Capsule.class);
                capsulesList.add(capsule);

                //Get the instance of the outer class (ChatarrayAdapter) having created this inner class (ChildEventListener)
                ChatAdapter adapter = (ChatAdapter) ChatAdapter.this;
                //Necessary to tell the observers of the array(i.e the recycler view) to refresh
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

        contactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                contactsList = (HashMap<String,String>) dataSnapshot.getValue();
                for(String contact: contactsList.values())
                    Log.d(TAG,"Contact: " +contact );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: contactsRef");
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View rowView  = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message, parent, false);
        //Get the refrence of the textViews displaying the sender name and the capsule's content
        TextView text = (TextView) rowView.findViewById(R.id.capsuleTextTextView);
        TextView senderName = (TextView) rowView.findViewById(R.id.messageSenderName);

        // Here I can set the view's size, margins, paddings and layout parameters

        ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(rowView, senderName, text);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Get the capsule's content and the sender's name
        String text = capsulesList.get(position).getText();
        String senderID = capsulesList.get(position).getSenderID();
        String senderName = contactsList.get(senderID);
        //Set textViewss' content
        holder.textTextView.setText(text);
        holder.senderTextView.setText(senderName);
    }

    @Override
    public int getItemCount() {
        return capsulesList.size();
    }
}
