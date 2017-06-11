package com.example.nordineaouni.Capsule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by nordineaouni on 25/02/17.
 */

public class ChatsListFragment extends Fragment  {

    //TAG for this class
    private String TAG = getClass().toString();

    //Interface to communicate with the main activity
    public interface OnChatsListClickListener {
        public void OnChatClick();
    }

    OnChatsListClickListener mainActivityCallback;

    //Adapter connected to the array to display the contacts
    private ChatsListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    //I don't exactly remember why but it was more convenient not to use a constructor
    public static ChatsListFragment newInstance() {
        return new ChatsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ChatsListAdapter();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chats_list_fragment_page, container, false);

        //Get the recyclerView and set its adapter. Adapter is created in onCreate()
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.chatsListRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        //Launch an instance of ChatActivity to display the chosen conversation
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        Conversation conversation = adapter.getConversation(position);
                        intent.putExtra("conversationID", conversation.getInterlocutorID());//Note at that point interlocutorID also defines the conversationID
                        startActivity(intent);
                    }
                });

        //Button to create a new activity
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newChatIntent =  new Intent(getContext(), NewChatActivity.class );
                startActivity(newChatIntent);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_chatslist_fragment, menu);
        super.onCreateOptionsMenu(menu,inflater);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_chatslist_fragment).getActionView();

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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //TODO: I think that this is not used. I use ItemClickSupport's implementation instead
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