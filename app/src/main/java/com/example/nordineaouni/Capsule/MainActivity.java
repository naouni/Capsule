package com.example.nordineaouni.Capsule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by nordineaouni on 03/01/17.
 */

public class MainActivity extends AppCompatActivity implements CapsuleFragment.CapsulePageFragmentListener, ChatsListFragment.OnChatsListClickListener, AdapterView.OnItemClickListener {

    private String TAG = getClass().toString();

    private SampleFragmentPagerAdapter sfpAdapter;
    private final int pageNumber =  2;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ViewPager and set its PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        //The following integer should be equal to the number of tabs minus 1.
        //Tells android how many pages it should keep in memory
        viewPager.setOffscreenPageLimit(pageNumber);

        sfpAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this);
        viewPager.setAdapter(sfpAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        //In order to make the screen scrollabe when typing in the edit text
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {

                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                    Intent mIntent = new Intent(getBaseContext(), LogInActivity.class);
                    startActivity(mIntent);
                }
            }
        };
    }
    
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_capsule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"item was clicked  "+item.toString());
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this,"Settings are not implemented yet", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_signOut:
                auth.signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPictureTaken(Bitmap imageBitmap){
        ImageView mImageView = (ImageView) sfpAdapter.getFragment(2).getView().findViewById(R.id.imageView);
        mImageView.setImageBitmap(imageBitmap);
    }

    //Interface method TODO: is it still used ?
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO
        Toast.makeText(this, "item "+ position+" clicked", Toast.LENGTH_SHORT).show();
    }

    //Interface method to communicate with the chatsListFragment
    @Override
    public void OnChatClick() {
        //TODO: launch intent to chat activity
    }
}
