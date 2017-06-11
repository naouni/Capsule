package com.example.nordineaouni.Capsule;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nordineaouni on 11/06/17.
 */

public class AddFriendsAdapter extends RecyclerView.Adapter<AddFriendsAdapter.ViewHolder> {


    String TAG = getClass().toString();

    private List<User> usersList;
    private List<User> filteredUsersList;
    private HashMap<String, String> contacts;
    private AddFriendsAdapter.ItemFilter filter;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;

        public ViewHolder(View view, TextView name) {
            super(view);
            nameTextView = name;
        }
    }

    public AddFriendsAdapter() {

        usersList = new ArrayList<User>();
        filteredUsersList = usersList;

        filter = new ItemFilter();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren() ){
                    User user = userSnapshot.getValue(User.class);
                    user.setUserId(userSnapshot.getKey());//userId is the node's key in Firebase but is an instance field in Android
                    usersList.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Load user failed");
            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AddFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View rowView  = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_item, parent, false);

        TextView nameText = (TextView) rowView.findViewById(R.id.userNameField);

        AddFriendsAdapter.ViewHolder viewHolder = new AddFriendsAdapter.ViewHolder(rowView, nameText);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AddFriendsAdapter.ViewHolder viewHolder, int position) {
        //Get element from your dataset at this position.
        //Replace the contents of the view with that element

        //Get the name of the sender from its UID
        User user = filteredUsersList.get(position);
        String userName = user.getUserName();

        //Set the textfields to the proper value
        viewHolder.nameTextView.setText(userName);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filteredUsersList.size();
    }

    public User getUser(int position){
        return filteredUsersList.get(position);
    }

    //Perfoms filtering using the mFilter instance field
    public void filter(CharSequence constraint) {
        filter.publishResults(constraint, filter.performFiltering(constraint));
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //Set the searchPattern to lower case
            String searchPattern = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            int numberOfUsers = usersList.size();

            //If the search pattern is the empty string
            if(searchPattern.equals("")){
                results.values = usersList;
                results.count = numberOfUsers;
                return results;
            }

            final ArrayList<User> filteredList = new ArrayList<>(numberOfUsers);

            //Select matching conversation
            for(User user: usersList) {
                String userName = user.getUserName();
                String filterableUserName = userName.toLowerCase();
                if (filterableUserName.contains(searchPattern)) {
                    filteredList.add(user);
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredUsersList = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }
    }
}
