package com.example.nordineaouni.Capsule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Button addFriendsButton = (Button) findViewById(R.id.addFriends);
        Button myFriendsButton = (Button) findViewById(R.id.myFriends);

        addFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddFriendsActivity.class);
                startActivity(intent);
            }
        });

        myFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MyFriendsActivity.class);
                startActivity(intent);
            }
        });
    }





}
