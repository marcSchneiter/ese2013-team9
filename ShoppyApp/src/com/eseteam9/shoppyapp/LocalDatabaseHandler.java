package com.eseteam9.shoppyapp;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ShoppyLocal";

    private static final String TABLE_USERS = "users";
    // Elements in table 'users'
    private static final String U_KEY_ID = "id";
    private static final String U_KEY_NAME = "name";
    private static final String U_KEY_KEY = "key";

    private static final String TABLE_SHOPPING_LISTS = "shopping_lists";
    // Elements in table 'shopping_lists'
    private static final String S_KEY_ID = "id";
    private static final String S_KEY_TITLE = "title";
    private static final String S_KEY_OWNER = "owner";
    private static final String S_KEY_ARCHIVED = "archived";
    private static final String S_KEY_TIMESTAMP = "timestamp";
 
    
    public static final String TABLE_ITEMS = "items";
    // Elements in table 'items'
    private static final String I_KEY_ID = "id";
    private static final String I_KEY_LIST_ID = "listid";
    private static final String I_KEY_NAME = "name";
    private static final String I_KEY_QUANTITY = "quantity";
    private static final String I_KEY_BOUGHT = "bought";
    private static final String I_KEY_TIMESTAMP = "timestamp";
    public LocalDatabaseHandler (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + U_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + U_KEY_NAME + " TEXT,"
                + U_KEY_KEY + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

    	String CREATE_TABLE = "CREATE TABLE "+ TABLE_SHOPPING_LISTS + "("
    			+ S_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + S_KEY_TITLE + " TEXT, " + S_KEY_OWNER
    			+ "TEXT, " + S_KEY_ARCHIVED + " BOOLEAN NOT NULL DEFAULT FALSE, " + S_KEY_TIMESTAMP
    			+ "DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
    	db.execSQL(CREATE_TABLE);
        
    	String CREATE_ITEMS_TABLE = "CREATE TABLE "+ TABLE_ITEMS + "("
    			+ I_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + I_KEY_LIST_ID + " INTEGER," + I_KEY_NAME + " TEXT, " + I_KEY_QUANTITY
    			+ "INTEGER, " + I_KEY_BOUGHT + " BOOLEAN NOT NULL DEFAULT FALSE, " + I_KEY_TIMESTAMP
    			+ "DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
    	db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        
        onCreate(db);
    }
    
    
    public boolean existsDatabase () {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0)
        	return true;
        db.close();
        return false;
    }    

    // USERS

    public void addUser (User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(U_KEY_NAME, user.name);
        values.put(U_KEY_KEY, user.key);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public User getUser (int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_USERS, new String[] { U_KEY_ID,
                U_KEY_NAME, U_KEY_KEY }, U_KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
 
        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return user;
    }

    // SHOPPING_LISTS
    public void addShoppingList (ShoppingList shoppingList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(S_KEY_TITLE, shoppingList.title);
        
        db.insert(TABLE_SHOPPING_LISTS, null, values);
        db.close();
    }
    
    public void deleteShoppingList (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SHOPPING_LISTS, S_KEY_ID + "=" + id, null);
        db.close();
    }
    
    public boolean existsShoppingList (String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SHOPPING_LISTS + " WHERE " + S_KEY_TITLE + " = '" + name + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0)
        	return true;
        db.close();
        return false;
    }    

    public ShoppingList getShoppingList (int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_SHOPPING_LISTS, new String[] { U_KEY_ID,
                U_KEY_NAME }, S_KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
 
        ShoppingList shoppingList = new ShoppingList(Integer.parseInt(cursor.getString(0)),
                									cursor.getString(1),
                									cursor.getString(2),
                									Boolean.parseBoolean(cursor.getString(3)),
                									cursor.getString(4));

        return shoppingList;
    }
    
    public List<ShoppingList> getAllShoppingLists() {
        List<ShoppingList> shoppingLists = new ArrayList<ShoppingList>();
        
        String selectQuery = "SELECT  * FROM " + TABLE_SHOPPING_LISTS;
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        if (cursor.moveToFirst()) {
            do {
            	ShoppingList shoppingList = new ShoppingList(Integer.parseInt(cursor.getString(0)),
															cursor.getString(1),
															cursor.getString(2),
															Boolean.parseBoolean(cursor.getString(3)),
															cursor.getString(4));
                shoppingLists.add(shoppingList);
            } while (cursor.moveToNext());
        }

        return shoppingLists;
    }
    
    
    
 // ITEMS
    public void add(Item item) {
    	SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(I_KEY_NAME, item.name);
        values.put(I_KEY_LIST_ID, item.listId);
        values.put(I_KEY_QUANTITY, item.quantity);
        values.put(I_KEY_BOUGHT, false);

        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }
    
    public void deleteItem (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ITEMS, I_KEY_ID + "=" + id, null);
        db.close();
    }

    public Item getItem (int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS + " WHERE " + I_KEY_ID + " = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor != null)
            cursor.moveToFirst();
 
        Item item = new Item(Integer.parseInt(cursor.getString(0)),
							Integer.parseInt(cursor.getString(1)),
							cursor.getString(2),
							Integer.parseInt(cursor.getString(3)),
							Boolean.parseBoolean(cursor.getString(4)),
							cursor.getString(5));

        return item;
    }
    
    public List<Item> getAllItems(int listId) {
        List<Item> items = new ArrayList<Item>();
        
        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS + " WHERE " + I_KEY_LIST_ID + " = " + listId;
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(Integer.parseInt(cursor.getString(0)),
									Integer.parseInt(cursor.getString(1)),
									cursor.getString(2),
									Integer.parseInt(cursor.getString(3)),
									Boolean.parseBoolean(cursor.getString(4)),
									cursor.getString(5));
                items.add(item);
            } while (cursor.moveToNext());
        }

        return items;
    }    
}