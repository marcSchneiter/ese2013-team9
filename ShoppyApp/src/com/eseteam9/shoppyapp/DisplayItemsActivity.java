package com.eseteam9.shoppyapp;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class DisplayItemsActivity extends Activity {
	private int listId;
	private ListItemAdapter adapter;
	private List<Item> items; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_items);
		
		//Get ListID and Name
		Intent intent = getIntent();
		this.listId = intent.getIntExtra("LIST_ID", 0);
		this.setTitle(intent.getStringExtra("LIST_NAME"));
		
		//Initialize ListView
		ListView lv = (ListView)findViewById(R.id.itemoverview);
		registerForContextMenu(lv);
		lv.setClickable(true);
		
		//Create Adapter
		LocalDatabaseHandler db = new LocalDatabaseHandler(this);
		this.items = db.getAllItems(this.listId);
		this.adapter = new ListItemAdapter(this, R.id.itemoverview,  this.items);
		lv.setAdapter(adapter);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_items, menu);
		return true;
	}
	
	
	//Creates ContextMenu (from arrays.xml) when pressing&holding an item
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	  if (v.getId()==R.id.itemoverview) {
	    menu.setHeaderTitle("Options");
	    String[] menuItems = getResources().getStringArray(R.array.context_menu);
	    for (int i = 0; i<menuItems.length; i++) {
	      menu.add(Menu.NONE, i, i, menuItems[i]);
	    }
	  }
	}
	
	//Reads what is selected from ContextMenu
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo(); 
	    int itemId = items.get(menuInfo.position).id;
	    LocalDatabaseHandler db = new LocalDatabaseHandler(this);
	    
	    switch (item.getItemId()) {
		  case 0:
		    db.deleteItem(itemId);
		    updateView();
		    return true;
		    
		  default:
		    return super.onContextItemSelected(item);
		}
	}
	public void updateView(){
		LocalDatabaseHandler db = new LocalDatabaseHandler(this);
		adapter.clear();
		adapter.addAll(db.getAllItems(this.listId));
		adapter.notifyDataSetChanged();
        db.close();
	}
	
	
	//Options on Taskbar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.add_list:
	            openAddListView();
	            return true;
	        case R.id.action_settings:
	            //openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void openAddListView(){
		Intent intent = new Intent(this, AddItemActivity.class);
		intent.putExtra("LIST_ID", this.listId);
		startActivity(intent);
	}

}