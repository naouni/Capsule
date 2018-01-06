package com.example.nordineaouni.Capsule;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
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
import java.util.List;

/**
 * Created by nordineaouni on 25/02/17.
 */

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ViewHolder> {

    String TAG = getClass().toString();

    private ArrayList<Conversation> conversationsList;
    private List<Conversation> filteredConversationsList;
    private HashMap<String, String> contacts;
    private ItemFilter filter;

    //Inner class. Holds references to the field we have to update on each row
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView dateTextView;

        public ViewHolder(View view, TextView name, TextView date) {
            super(view);
            nameTextView = name;
            dateTextView = date;
        }
    }

    public ChatsListAdapter() {

        conversationsList = new ArrayList<Conversation>();
        filteredConversationsList = conversationsList;
        contacts = new HashMap<String, String>();
        filter = new ItemFilter();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference conversationsRef = FirebaseDatabase.getInstance().getReference().child("conversations").child(auth.getCurrentUser().getUid());
        DatabaseReference contactsRef = FirebaseDatabase.getInstance().getReference().child("contacts").child(auth.getCurrentUser().getUid());

        conversationsRef.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                Conversation conversation = dataSnapshot.getValue(Conversation.class);
                conversationsList.add(conversation);

                //Get the instance of the outer class (ChatarrayAdapter) having created this inner class (ChildEventListener)
                ChatsListAdapter adapter = (ChatsListAdapter) ChatsListAdapter.this;

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

                //TODO:handle deletion
                String commentKey = dataSnapshot.getKey();
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

        contactsRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "ChatListAdapter contacts");
                //Key: UID, Value: user name
                contacts = (HashMap<String,String>) dataSnapshot.getValue();

                //Notify the observers (i.e. the listView) to refresh
                ChatsListAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadContacts:onCancelled", databaseError.toException());
            }
        });
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
        Conversation conversation = filteredConversationsList.get(position);
        String interlocutorName = contacts.get(conversation.getInterlocutorID());

        //Set the textfields to the proper value
        viewHolder.nameTextView.setText(interlocutorName);
        viewHolder.dateTextView.setText(conversation.getDateLastSent());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filteredConversationsList.size();
    }

    public Conversation getConversation(int position){
        return filteredConversationsList.get(position);
    }

    //Perfoms filtering using the mFilter instance field
    public void filter(CharSequence constraint){
        filter.publishResults(constraint, filter.performFiltering(constraint));
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //Set the searchPattern to lower case
            String searchPattern = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            int numberOfConversations = conversationsList.size();

            //If the search pattern is the empty string
            if(searchPattern.equals("")){
                results.values = conversationsList;
                results.count = numberOfConversations;
                return results;
            }

            final ArrayList<Conversation> filteredList = new ArrayList<>(numberOfConversations);

            //Select matching conversation
            for(Conversation conversation: conversationsList) {
                String conversationName = contacts.get(conversation.getInterlocutorID());
                String filterableConversationName = conversationName.toLowerCase();
                if (filterableConversationName.contains(searchPattern)) {
                    filteredList.add(conversation);
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredConversationsList = (ArrayList<Conversation>) results.values;
            notifyDataSetChanged();
        }
    }

}