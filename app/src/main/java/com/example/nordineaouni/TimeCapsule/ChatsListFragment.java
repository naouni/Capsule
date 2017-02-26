package com.example.nordineaouni.TimeCapsule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * Created by nordineaouni on 25/02/17.
 */

public class ChatsListFragment extends Fragment {

    //TAG for this class
    private String TAG =  getClass().toString();


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

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
