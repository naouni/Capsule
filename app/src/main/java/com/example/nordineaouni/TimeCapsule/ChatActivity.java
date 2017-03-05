package com.example.nordineaouni.TimeCapsule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by nordineaouni on 01/03/17.
 */

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        Intent intent = getIntent();//Get the intent that started this activity(see ChatsListFragment.onCreateView())
        String conversationID = intent.getStringExtra("conversationID");
        chatAdapter = new ChatAdapter(conversationID);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);

    }
}
