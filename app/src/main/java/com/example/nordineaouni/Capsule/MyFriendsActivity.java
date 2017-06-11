package com.example.nordineaouni.Capsule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MyFriendsActivity extends AppCompatActivity {

    final String TAG = getClass().toString();
    NewChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        adapter = new NewChatAdapter();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myFriendsActivityRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        //TODO: Do something here if needed
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_friends_activity, menu);


        SearchView searchView = (SearchView) menu.findItem(R.id.menuMyFriendsActivity_search).getActionView();
        Log.d(TAG, "HEY" );
        Log.d(TAG, Boolean.toString(searchView==null) );

        //Attach a listener to the SearchView that will call the filtering function of the adapter
        // as the user types
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        });*/

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
