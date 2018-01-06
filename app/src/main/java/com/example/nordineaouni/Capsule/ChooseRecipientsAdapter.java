package com.example.nordineaouni.Capsule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by nordineaouni on 10/08/17.
 */

public class ChooseRecipientsAdapter extends RecyclerView.Adapter<ChooseRecipientsAdapter.ViewHolder> implements Filterable {


    private final String TAG = getClass().toString();

    private List<String> contactsIdList;
    private Map<String,String> contactsMap;
    private List<String> filteredContactsIdList;
    private ItemFilter filter;


    @NonNull
    private OnItemCheckListener onItemCheckListener;

    interface OnItemCheckListener {
        void onItemCheck(String friendId);
        void onItemUncheck(String friendId);
        boolean shouldBeChecked(String friendId);
    }

    public ChooseRecipientsAdapter(@NonNull OnItemCheckListener onItemCheckListener) {
        filter = new ItemFilter();
        filteredContactsIdList = new ArrayList<String>();

        this.onItemCheckListener = onItemCheckListener;

        //Get access to the current user's contact list
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference contactsRef = FirebaseDatabase.getInstance().getReference().child("contacts").child(auth.getCurrentUser().getUid());

        contactsRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "ContactsRef onDataChange");
                //Key: UID, Value: user name
                contactsMap = (HashMap<String,String>) dataSnapshot.getValue();

                //Convert the contacts info from a map to a list
                //The empty array of string passed to the toArray() function ensures a proper casting
                String[] contactsArray = (String[]) contactsMap.keySet().toArray(new String[0]);
                contactsIdList = new ArrayList<String>(Arrays.asList(contactsArray));

                //Update the filteredContactsIdList used to display the contacts' names on screen
                filteredContactsIdList = contactsIdList;

                //Notify the observer(s) (i.e. the listView) to refresh
                ChooseRecipientsAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadContacts:onCancelled", databaseError.toException());
            }
        });
    }

    public long getItemId(int position) {
        return position;
    }

    public String getContactId(int position){
        //TODO: shouldn't it be filteredContactsIdList here ?
        return contactsIdList.get(position);
    }

    //Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filteredContactsIdList.size();
    }

    public Filter getFilter() {
        return filter;
    }

    //Perfoms filtering using the filter instance field
    public void filter(CharSequence constraint){
        filter.publishResults(constraint, filter.performFiltering(constraint));
    }

    //2 private inner class. A viewHolder and an itemFilter
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //Set the searchPattern to lower case
            String searchPattern = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            int numberOfContacts = contactsIdList.size();

            //If the search pattern is the empty string
            if(searchPattern.equals("")){
                results.values = contactsIdList;
                results.count = numberOfContacts;
                return results;
            }

            final ArrayList<String> filteredList = new ArrayList<>(numberOfContacts);

            for(String contactId: contactsIdList) {
                //We filter on the names
                String contactName = contactsMap.get(contactId);
                String filterableContactName = contactName.toLowerCase();
                if (filterableContactName.contains(searchPattern)) {
                    filteredList.add(contactId);//We still use id's internally.
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredContactsIdList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View rowView;
        TextView contactName;
        CheckBox checkBox;

        public ViewHolder(View rowView) {
            super(rowView);
            this.rowView = rowView;
            contactName = (TextView) rowView.findViewById(R.id.chooseRecipients_adapter_FriendTextView);
            checkBox= (CheckBox) rowView.findViewById(R.id.chooseRecipients_adapter_checkBox);
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChooseRecipientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View rowView  = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_choose_recipients_list_item, parent, false);

        // Here I can set the view's size, margins, paddings and layout parameters

        ChooseRecipientsAdapter.ViewHolder viewHolder = new ChooseRecipientsAdapter.ViewHolder(rowView);


        return viewHolder;
    }

    //Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        //Get element from your dataset at this position.
        //Replace the contents of the view with that element

        //Get the name of the sender from its UID
        final String friendId = filteredContactsIdList.get(position);
        String contactName = contactsMap.get(friendId);

        //Update the content of the row at position with the friend's name
        viewHolder.contactName.setText(contactName);

        //Set if the checkbox should be checked or not.
        CheckBox checkBox = viewHolder.checkBox;
        //Check if the check box corresponding to this friend should be checked already
        if(onItemCheckListener.shouldBeChecked(friendId)){
            checkBox.setChecked(true); //Then tick it.
        }else{
            checkBox.setChecked(false); //Else don't
        }

        // set a listener on the tick box to add and remove friends from the recipient list.
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox box = (CheckBox) view;

                if (box.isChecked()) {
                    onItemCheckListener.onItemCheck(friendId);
                } else {
                    onItemCheckListener.onItemUncheck(friendId);
                }
            }
        });

    }
}