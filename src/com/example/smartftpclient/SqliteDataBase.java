package com.example.smartftpclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 

public class SqliteDataBase {

	private static final String DATABASE_NAME = "Record";                     
    private static final String DATABASE_TABLE = "my";               
    private static final int DATABASE_VERSION = 1;                          
    
    public static final String KEY_TITLE = "title";                         
    public static final String KEY_PORT = "port";
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ROWID = "id";                           
   
    private DatabaseHelper mDbHelper;                                      
    private SQLiteDatabase mDb;                                            
    private static final String DATABASE_CREATE =                          
    "create table " + DATABASE_TABLE + " ("
            + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_TITLE + " text not null, "
            + KEY_PORT +  " text not null, " 
            + KEY_USER_NAME +  " text not null, " 
            + KEY_PASSWORD + " text not null);"; 
   private final Context mCtx;                                                
   public SqliteDataBase(Context ctx) {                                   
           this.mCtx = ctx;
            }
       //
       // inner class DataBaseHelper
       //
   private static class DatabaseHelper extends SQLiteOpenHelper {              
       DatabaseHelper(Context context) {
           super(context, DATABASE_NAME, null, DATABASE_VERSION);          
       }
       @Override
       public void onCreate(SQLiteDatabase db) {                           
           db.execSQL(DATABASE_CREATE);                                    
       }
       
       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {                                      
           // Not used, but you could upgrade the database with ALTER           
//Scripts 
       }
}
   public SqliteDataBase open() throws android.database.SQLException {
       mDbHelper = new DatabaseHelper(mCtx);
       mDb = mDbHelper.getWritableDatabase();
       return this;
}
   public void close() {
	      mDbHelper.close();
	}
   //
   // Add new Reminder
  //
   public long createServer(String title, String port, String name, String password) {                                         
		           ContentValues initialValues = new ContentValues();
		           initialValues.put(KEY_TITLE, title);
		           initialValues.put(KEY_PORT, port);
		           initialValues.put(KEY_USER_NAME, name); 
		           initialValues.put(KEY_USER_NAME, password); 
		           return mDb.insert(DATABASE_TABLE, null, initialValues);  
		 }
   //
   // Delete Reminder 
   public boolean deleteServer(long rowId) {                            
	      
	        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;     
	    }
   //
   // Fetch all data from database..
   public Cursor fetchAllServer() {                                    
       return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
               KEY_PORT, KEY_USER_NAME, KEY_PASSWORD}, null, null, null, null, null);
   }
   //
   // Fetch single Reminder data from data base
   public Cursor fetchSever(long rowId) throws SQLException {          
       Cursor mCursor =
               mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                       KEY_TITLE, KEY_PORT, KEY_USER_NAME, KEY_PASSWORD}, KEY_ROWID + "=" + 
                       rowId, null,null, null, null, null);                           
       if (mCursor != null) {
           mCursor.moveToFirst();                                         
       }
       return mCursor;
   }
  //
  // Update Reminder data 
   public boolean updateServer(long rowId, String title, String port, String 
		   name, String password) {                                         
		           ContentValues args = new ContentValues();                          
		           args.put(KEY_TITLE, title);
		           args.put(KEY_PORT, port);
		           args.put(KEY_USER_NAME, port);
		           args.put(KEY_PASSWORD, password);
		        return 
		         mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0; 
		       }
}
