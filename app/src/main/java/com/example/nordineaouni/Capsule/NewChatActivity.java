package com.example.nordineaouni.Capsule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


public class NewChatActivity extends AppCompatActivity {

    final String TAG = getClass().toString();
    FriendsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        adapter = new FriendsAdapter();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.newChatRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        //TODO:Does it create a conversation when there is no message in the database?
                        //Launch an instance of ChatActivity to display the chocsen conversation
                        Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                        String contactId = adapter.getContactId(position);
                        //Note that at that point the interlocutorID field from the Conversation
                        // class also defines the conversationID in the Firebase database.
                        // This ID will be used by the ChatActivity to retrieve existing messages
                        // or to create a new conversation otherwise.
                        intent.putExtra("conversationID", contactId);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_newchat_activity, menu);


        SearchView searchView = (SearchView) menu.findItem(R.id.search_newchat_activity).getActionView();

        //Attach a listener to the SearchView that will call the filtering function of the adapter
        // as the user types
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Filter the results using the adapter's method
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Filter the results using the adapter's method
                adapter.filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search_newchat_activity:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
