package com.eseteam9.shoppyapp;

import java.util.List;

import com.eseteam9.shoppyapp.R;
import com.eseteam9.shoppyapp.shoppingclasses.ShoppingList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * 
 * This class converts the Array of ShoppingLists to it's String representation based on the listname and the items
 * based on the title.
 * 
 * @author S�bastien Broggi, Sammer Puran, Marc Schneiter, Lukas Galliker
 * @extends ArrayAdapter<ShoppingList>
 */
public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {
    private List<ShoppingList> lists;
    private Activity activity;
    
    public ShoppingListAdapter(Activity a, int textViewResourceId, List<ShoppingList> list){
    	super(a, textViewResourceId, list);
        this.lists = list;
        this.activity = a;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_row, null);
        }
 
        ShoppingList list = lists.get(position);
        if (list != null) {
            TextView itemView = (TextView) v.findViewById(R.id.listname);
            if (itemView != null) {
                itemView.setText(list.title());
            }
        }
        return v;
    }
}
