package com.eseteam9.shoppyapp;

import com.eseteam9.shoppyapp.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WelcomeScreen extends Activity {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
       
        //Checks if database of program exists, otherwise continues to DisplayLists
        if (new UserHandler(this).existsUser()){
            Intent intent = new Intent(this, MainActivity.class);
    	    startActivity(intent);
        }
        
        //new DatabaseTests(this);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome_screen, menu);
		return true;
	}
    
	//Called when clicking on "Save"-Button
	public void createDatabase(View view){
	    EditText editText = (EditText) findViewById(R.id.nickname);
	    String nickname = editText.getText().toString().trim();
	    
		if (nickname.length() == 0)
	    	Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
		else{
		    //get Phone number if possible
		    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		    String myNumber = tm.getLine1Number();
		    
		    //Add Entry in DB    
		    new UserHandler(this).add(new User(nickname, myNumber));
		    
	        //Switch to DisplayListActivity
	        Intent intent = new Intent(this, MainActivity.class);
		    startActivity(intent);
		}
	}

}
