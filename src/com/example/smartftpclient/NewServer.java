package com.example.smartftpclient;
 import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewServer extends Activity implements OnClickListener {
  private Button btnAddServer;
  private TextView serverName;
  private TextView protNo;
  private TextView user;
  private TextView password;
  private ProgressDialog pDialog;
  public static final int progress_bar_type = 0; 
  private SqliteDataBase database;
  @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_server);
		database=new SqliteDataBase(this);
		serverName=(TextView) findViewById(R.id.editText1);
		protNo=(TextView) findViewById(R.id.editText2);
		user=(TextView) findViewById(R.id.editText3);
		password=(TextView) findViewById(R.id.editText4);
		btnAddServer=(Button) findViewById(R.id.button1);
		btnAddServer.setOnClickListener(this);
		
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), "sever add", Toast.LENGTH_SHORT).show();
	//    testValidation();
	    allRecord();
	    
	}
  private void testValidation()
  {
	  if((serverName.length()!=0)&&(protNo.length()!=0)&&(password.length()!=0)&&(user.length()!=0))
	  {
		  database.open();
		  database.createServer(serverName.getText().toString(), protNo.getText().toString(), user.getText().toString(), password.getText().toString());
	     
		  database.close();
	  }
  }
  private void allRecord()
  { database.open();
	   Cursor c=database.fetchAllServer();
	    Toast.makeText(getBaseContext(), ""+c, Toast.LENGTH_SHORT).show();
        
	   startManagingCursor(c);
		  c.moveToFirst();
		  //NEW
		/*  if (c .moveToFirst()) {

	            while (c.isAfterLast() == false) {
	               String  id= c.getString(c.getColumnIndex(SqliteDataBase.KEY_ROWID)); 	
		        	Log.i("id", ""+id);
	                String name = c.getString(c
	                        .getColumnIndex(SqliteDataBase.KEY_TITLE));
	                Log.i("Title", ""+name);   
	                Toast.makeText(getBaseContext(), ""+name, Toast.LENGTH_SHORT).show();
	                c.moveToNext();
	            }
	        }*/
		  database.close();
  }
  @Override
  protected Dialog onCreateDialog(int id) {
      switch (id) {
      case progress_bar_type:
          pDialog = new ProgressDialog(this);
          pDialog.setMessage("Please Wait.....");
          pDialog.setIndeterminate(false);
          pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
          pDialog.setCancelable(true);
          pDialog.show();
          return pDialog;
       default:
          return null;
      }
}

}
