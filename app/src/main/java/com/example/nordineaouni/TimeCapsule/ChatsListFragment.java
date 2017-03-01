package com.example.nordineaouni.TimeCapsule;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * Created by nordineaouni on 25/02/17.
 */

public class ChatsListFragment extends Fragment  {

    //TAG for this class
    private String TAG =  getClass().toString();

    //Interface to communicate with the main activity
    public interface OnChatsListClickListener {
        public void OnChatClick();
    }

    OnChatsListClickListener mainActivityCallback;

    //Adapter connected to the array to display the contacts
    private ChatsListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    public static ChatsListFragment newInstance() {
        ChatsListFragment chatsListFragment = new ChatsListFragment();
        return chatsListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ChatsListAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chats_list_fragment_page, container, false);

        //Get the recyclerView and set its adapter. Adapter is created in onCreate()
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        //TODO: launch chat activity
                        //Needs messages content, time and sender
                    }
                });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mainActivityCallback = (OnChatsListClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnclickChatsListListener");
        }
    }
}
