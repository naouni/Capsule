package com.example.nordineaouni.TimeCapsule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by nordineaouni on 01/03/17.
 */

public class ChatAdapter {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textTextView;
        private TextView dateTextView;

        public ViewHolder(View view, TextView name, TextView date) {
            super(view);
            textTextView = name;
            dateTextView = date;
        }
    }
}
