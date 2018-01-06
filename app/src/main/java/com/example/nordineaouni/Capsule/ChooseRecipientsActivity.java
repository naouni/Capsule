package com.example.nordineaouni.Capsule;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChooseRecipientsActivity extends AppCompatActivity {

    final String TAG = getClass().toString();
    ChooseRecipientsAdapter adapter;
    List<String> currentSelectedFriendsId = new ArrayList<String>();
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_recipients);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        adapter = new ChooseRecipientsAdapter(new ChooseRecipientsAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(String item) {
                currentSelectedFriendsId.add(item);

                for (String id :currentSelectedFriendsId){
                    Log.d(TAG, id);
                    Toast.makeText(getBaseContext(), id, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemUncheck(String item) {
                currentSelectedFriendsId.remove(item);

                for (String id :currentSelectedFriendsId){
                    Log.d(TAG, id);
                    Toast.makeText(getBaseContext(), id, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean shouldBeChecked(String friendUserId){

                boolean shouldBeChecked = currentSelectedFriendsId.contains(friendUserId);

                return shouldBeChecked;
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chooseRecipientsActivityRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Firebase authentication and database
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        final String CAPSULES_NODE = getResources().getString(R.string.capsulesNode);
        final String USER_CAPSULES_NODE = getResources().getString(R.string.userCapsulesNode);
        final DatabaseReference capsulesRef = FirebaseDatabase.getInstance().getReference().child(CAPSULES_NODE);
        final DatabaseReference userCapsulesRef = FirebaseDatabase.getInstance().getReference().child(USER_CAPSULES_NODE).child(userId);


        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        //Add listener on floating action button to send the capsule once and for all.
        FloatingActionButton sendCapsuleButton = (FloatingActionButton) findViewById(R.id.sendFloatingActionButton);
        sendCapsuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //read data set earlier in the creation process of the capsule
                double defaultValue = 0;
                long defaultValueRadius = 1l;
                String text = getIntent().getStringExtra("text");
                long openingDateAsNumber = getIntent().getLongExtra("openingDate", -1l);
                // Get the data set on this screen
                double latitude = getIntent().getDoubleExtra("latitude", defaultValue);
                double longitude = getIntent().getDoubleExtra("longitude", defaultValue);
                long radius = getIntent().getLongExtra("radius", defaultValueRadius);

                // Get strings representing the dates from the UNIX elapsed time
                Date todaysDate = calendar.getTime();
                String todaysDateAsString = dateFormat.format(todaysDate);
                Date openingDateAsDate = new Date(openingDateAsNumber);
                String openingDateAsString = dateFormat.format(openingDateAsDate);

                // Create a capsule object and push it
                Capsule newCapsule = new Capsule(text, userId, todaysDateAsString, openingDateAsString, latitude, longitude, radius);

                String key = capsulesRef.push().getKey(); // Create a unique key that will be used as ID for the the new capsule entries

                // Save the capsule in the database
                capsulesRef.child(key).setValue(newCapsule);
                userCapsulesRef.child(key).setValue(newCapsule);

                //Show confirmation message to user
                Toast.makeText(ChooseRecipientsActivity.this, "Capsule sent", Toast.LENGTH_SHORT).show(); //Confirmation message to user TODO: use a string variable

                //Bring user back to main screen
                Intent intent = new Intent(ChooseRecipientsActivity.this, MainActivity.class);
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
