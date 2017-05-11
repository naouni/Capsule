package com.example.nordineaouni.Capsule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nordineaouni on 21/04/17.
 */

public class NewChatAdapter extends BaseAdapter implements Filterable {


    private List<String> contactsList;
    private List<String> filteredContactsList;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();

    public NewChatAdapter(Context context, List<String> data) {
        this.filteredContactsList = data ;
        this.contactsList = data ;
        mInflater = LayoutInflater.from(context);
    }

    //Perfoms filtering using the mFilter instance field
    public void filter(CharSequence constraint){
        mFilter.publishResults(constraint, mFilter.performFiltering(constraint));
    }

    public int getCount() {
        return filteredContactsList.size();
    }

    public Object getItem(int position) {
        return filteredContactsList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.search_result_item, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.contactName);

            // Bind the data efficiently with the holder.
            convertView.setTag(holder);//TODO: What does this line ?
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // If weren't re-ordering this you could rely on what you set last time
        holder.text.setText(filteredContactsList.get(position));

        return convertView;
    }

    static class ViewHolder {
        TextView text;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String searchPattern = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            //Base case where the search pattern is the empty string
            if(searchPattern.equals("")){
                Log.d("Filter","query is null");
                results.values = contactsList;
                results.count = contactsList.size();
                return results;
            }

            int count = contactsList.size();
            final ArrayList<String> filteredList = new ArrayList<String>(count);

            for(String filterableString: contactsList) {
                if (filterableString.toLowerCase().contains(searchPattern)) {
                    filteredList.add(filterableString);
                }
            }


            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredContactsList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }

    }
}
