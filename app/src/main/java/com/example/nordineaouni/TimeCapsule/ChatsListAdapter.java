package com.example.nordineaouni.TimeCapsule;

import android.content.Context;
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
 * Created by nordineaouni on 25/02/17.
 */

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ViewHolder> {

    String TAG = getClass().toString();

    private FirebaseAuth mAuth;
    private DatabaseReference mMetadataRef;
    private DatabaseReference mContactsRef;

    private Context context;
    private ArrayList<String> interlocutorIDs;
    private ArrayList<String> dates;
    private HashMap<String, String> contacts;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mNameTextView;
        public TextView mDateTextView;

        public ViewHolder(View view, TextView name, TextView date) {
            super(view);
            mNameTextView = name;
            mDateTextView = date;
        }
    }

    public ChatsListAdapter(Context context) {

        interlocutorIDs = new ArrayList<String>();
        dates = new ArrayList<String>();
        contacts = new HashMap<String, String>();

        mAuth = FirebaseAuth.getInstance();
        mMetadataRef = FirebaseDatabase.getInstance().getReference().child("messagesMetadata").child(mAuth.getCurrentUser().getUid());
        mContactsRef = FirebaseDatabase.getInstance().getReference().child("contacts").child(mAuth.getCurrentUser().getUid());

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                MessageMetadata messageMetadata = dataSnapshot.getValue(MessageMetadata.class);

                interlocutorIDs.add(messageMetadata.interlocutorID);
                dates.add(messageMetadata.sentDate);

                //Get the instance of the outer class (ChatarrayAdapter) having created this inner class (ChildEventListener)
                ChatsListAdapter adapter = (ChatsListAdapter) ChatsListAdapter.this;

                //Necessary to tell the observers of the array(i.e the listview) to refresh
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
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        };
        mMetadataRef.addChildEventListener(childEventListener);

        ValueEventListener contactsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Key: UID, Value: user name
                contacts = (HashMap<String,String>) dataSnapshot.getValue();

                //Notify the observers (i.e. the listView) to refresh
                ChatsListAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadContacts:onCancelled", databaseError.toException());
            }
        };
        mContactsRef.addValueEventListener(contactsListener);

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View rowView  = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chats_list_item, parent, false);

        TextView nameText = (TextView) rowView.findViewById(R.id.contactName);
        TextView dateText = (TextView) rowView.findViewById(R.id.dateReceived);
        // Here I can set the view's size, margins, paddings and layout parameters

        ViewHolder viewHolder = new ViewHolder(rowView, nameText, dateText);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //Get element from your dataset at this position.
        //Replace the contents of the view with that element

        //Get the name of the sender from its UID
        String name = contacts.get(interlocutorIDs.get(position));

        //Set the textfields to the proper value
        viewHolder.mNameTextView.setText(name);
        viewHolder.mDateTextView.setText(dates.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return interlocutorIDs.size();
    }
}