package com.eseteam9.shoppyapp.handlers;

import java.util.Date;

import com.eseteam9.shoppyapp.shoppingclasses.ShoppingList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
/**
 * Is used for the tests, does generally the same as the LocalDatabaseHandler, but optimized for testing.
 * 
 * @author S�bastien Broggi, Sammer Puran, Marc Schneiter, Lukas Galliker
 * @extends SQLiteOpenHelper
 */
public class MockDataBaseHandler extends  SQLiteOpenHelper {
	private final Context context;
	

	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "local.db"; 
	public static final String TABLE_NAME = "test_list";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ONLINEKEY = "onlineKey";
    private static final String KEY_ARCHIVED = "archived";
    private static final String KEY_TIMESTAMP = "timestamp";
    public MockDataBaseHandler(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
    public static void createTable(SQLiteDatabase db) {
        String CREATE_SHOPPING_LISTS_TABLE = "CREATE TABLE "+ TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        		+ KEY_TITLE + " TEXT, " + KEY_ONLINEKEY + " TEXT,"
                + KEY_ARCHIVED + " INTEGER," + KEY_TIMESTAMP
                + "DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_SHOPPING_LISTS_TABLE);
    }
    
	public static void dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}
	
    public void add(ShoppingList shoppingList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, shoppingList.title());
        values.put(KEY_ONLINEKEY, shoppingList.onlineKey());
        values.put(KEY_ARCHIVED, shoppingList.archived() ? 1 : 0);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    
    public ShoppingList get(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        
        ShoppingList list = new ShoppingList(context, cursor.getInt(0));
        db.close();
        return list;
    }
    
    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=" + id, null);
        db.close();
    }
    
    public boolean existsEntry(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_TITLE + " = '" + name + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0)
        	return true;
        db.close();
        return false;
    }
    
    public ShoppingList[] getAll() {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        ShoppingList returnArray[] = new ShoppingList[cursor.getCount()];
     
        if (cursor.moveToFirst()) {
        	int i = 0;
            do {
                returnArray[i] = new ShoppingList(context, cursor.getInt(0));
                i++;
            } while (cursor.moveToNext());
        }

        db.close();
        return returnArray;
    }
    
    public void update(int id, String name) {
  	  SQLiteDatabase db = this.getWritableDatabase();
  	  //db.update(TABLE_SHOPPING_LISTS, null, S_KEY_ID+ "="+ id, null);
  	  SQLiteStatement stmt = db.compileStatement("UPDATE " + TABLE_NAME + " SET " + KEY_TITLE + " = '" + name + "' WHERE "+ KEY_ID +" = "+ id );
  	  stmt.execute();
  	  db.close();
    }
    
    public void updateOnlineKey(int id, String key) {
    	  SQLiteDatabase db = this.getWritableDatabase();
    	  SQLiteStatement stmt = db.compileStatement("UPDATE " + TABLE_NAME + " SET " + KEY_ONLINEKEY + " = '" + key + "' WHERE "+ KEY_ID +" = "+ id );
    	  stmt.execute();
    	  db.close();
      }
    
    public int getCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        int returnInt = cursor.getCount();
        db.close();
        return returnInt;
    }

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		createTable(arg0);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}

