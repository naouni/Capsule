package com.example.nordineaouni.Capsule;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        private LinearLayout linearLayout;
        private LinearLayout linearLayout2;

        public ViewHolder(View view, TextView senderName, TextView text, LinearLayout linearLayout, LinearLayout linearLayout2) {
            super(view);
            senderTextView = senderName;
            textTextView = text;
            this.linearLayout = linearLayout;
            this.linearLayout2 = linearLayout2;
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
                .inflate(R.layout.capsule, parent, false);
        TextView text = (TextView) rowView.findViewById(R.id.txtMessage);
        TextView senderName = (TextView) rowView.findViewById(R.id.txtInfo);

        LinearLayout linearLayout =  (LinearLayout) rowView.findViewById(R.id.contentWithBackground);
        LinearLayout linearLayout2 = (LinearLayout) rowView.findViewById(R.id.content);

        // Here I can set the view's size, margins, paddings and layout parameters

        ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(rowView, senderName, text, linearLayout, linearLayout2);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //If it is an incoming capsule modify background color and align it to the left
        if( !capsulesList.get(position).getSenderID().equals(auth.getCurrentUser().getUid()) ) {
            holder.linearLayout.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.linearLayout.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.linearLayout.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.linearLayout2.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.linearLayout2.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.textTextView.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.textTextView.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.senderTextView.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.senderTextView.setLayoutParams(layoutParams);
        }

        //Get the capsule's content and the sender's name
        Capsule capsule = capsulesList.get(position);
        String text = capsule.getText();
        String senderID = capsule.getSenderID();
        String senderName = contactsList.get(senderID);

        //Date and time information
        Calendar c = Calendar.getInstance();
        Date todayDate = c.getTime();
        String openingDateString = capsule.getOpeningDate();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date openingDate = null;
        try {
            //Get Date object from the date string
            openingDate = dateFormatter.parse(openingDateString);
        }catch (ParseException e1){
            e1.printStackTrace();//Handle parse exception
        }

        Log.d(TAG, "Today: "+todayDate.toString());
        Log.d(TAG, "OpeningDate: "+openingDate.toString() );

        //Checks if date and time requirement are met
        if(todayDate.compareTo(openingDate) >= 0  ){
            //todayDate is after or equal to OpeningDate
            //TODO: show content
            holder.senderTextView.setText(senderName);
            holder.textTextView.setText(text);
        //Hide the capsule's content
        }else {
            //TODO: improve the way the content is hidden
            holder.senderTextView.setText(senderName);
            holder.textTextView.setText("????????");
        }

    }

    @Override
    public int getItemCount() {
        return capsulesList.size();
    }
}
