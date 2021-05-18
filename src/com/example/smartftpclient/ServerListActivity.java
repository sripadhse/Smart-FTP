package com.example.smartftpclient;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

public class ServerListActivity extends ListActivity{
    private SqliteDataBase dataBase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_main);
		dataBase=new SqliteDataBase(this);
		//dataBase.open();
		//dataBase.createServer("ftp.drivehq.com", "21", "naqib321", "03469280195");
		//Cursor c=dataBase.fetchAllServer();
		//c.moveToFirst();
		//String name = c.getString(c
       //         .getColumnIndex(SqliteDataBase.KEY_TITLE));
		//dataBase.close();
		Toast.makeText(getBaseContext(), "Its Will Display All Server List.. " , Toast.LENGTH_SHORT).show();
		
	}

}
