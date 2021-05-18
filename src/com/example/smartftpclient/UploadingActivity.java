package com.example.smartftpclient;
 
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UploadingActivity extends Activity implements OnClickListener {
	 // Progress Dialog
    private ProgressDialog pDialog;
    private static FTPClient ftpClient;
    // Progress dialog type (0 - for Horizontal progress bar)
  public static final int progress_bar_type = 0; 
  private Button upload;
  private TextView showPathTextBox;
  private TextView uploadingStateTextBox;
  public FtpConnection ftp;
  private static final int REQUEST_ID = 2;
  private static final int PICK_IMAGE = 1;
  private int uploadingSate;
  private String path="";
  private MyUploaderClass uploadClass;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_uploading_main);
         uploadClass=new MyUploaderClass();
        ftp=new FtpConnection(this);
        showPathTextBox=(TextView) findViewById(R.id.link);
        uploadingStateTextBox=(TextView) findViewById(R.id.state);
        upload=(Button) findViewById(R.id.button1);
        upload.setOnClickListener(this);
    
    }

    

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			exploreFileToUpload();
			break;

		default:
			break;
		}
	}
	 
	  /**
	   * Showing Dialog
	   * */
	  @Override
	  protected Dialog onCreateDialog(int id) {
	      switch (id) {
	      case progress_bar_type:
	          pDialog = new ProgressDialog(this);
	          pDialog.setMessage("Uploading file.....");
	          pDialog.setIndeterminate(false);
	          pDialog.setMax(100);
	          pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	          pDialog.setCancelable(true);
	       /*   pDialog.setButton("CANCEL", new DialogInterface.OnClickListener() {

                  public void onClick(DialogInterface dialog, int which) {
                 Toast.makeText(getBaseContext(), "Now it is disalbe", Toast.LENGTH_SHORT).show();
                 }
              });*/
	          pDialog.show();
	          return pDialog;
	      default:
	          return null;
	      }
	  }
  private void exploreFileToUpload()
  {
	  //////////////////////////////////////
      // selecting file from sdcard
       Intent intent = new Intent();
       intent.setAction(Intent.ACTION_GET_CONTENT);
       intent.addCategory(Intent.CATEGORY_OPENABLE);
       intent.setType("*/*");
       startActivityForResult(intent, REQUEST_ID);
////////////////////////////////////////////
  }
  @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        
	        if (requestCode == REQUEST_ID && resultCode == Activity.RESULT_OK) {
	                try {
	                 int res;
	                Uri uri =data.getData();
	               path = getPath(this, uri);
	               showPathTextBox.setText(path);
	              if((path!=null)&&(path.length()!=0))
	              { 
	               startUploading();
	               }else{
	                	uploadingStateTextBox.setText("File not Available");
	                }
	                } catch (Exception e) {
	                        e.printStackTrace();
	                        
	                }
	                
	                
	                }
	      
	}
  private void startUploading()
  {
	    
    //   uploadClass.execute(path);
     new MyUploaderClass().execute(path);
  }
   
  private void stopUploading()
  {
	 if(uploadClass.running)
	 {
     //  uploadClass.cancel(false);
	 }
  }
  
 private class MyUploaderClass extends AsyncTask<String, String , String> 
 {
	 boolean running=true;
	
	 @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			  super.onPreExecute();
			  uploadingStateTextBox.setText("Start Uploading");
			  showDialog(progress_bar_type);
			 }

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		     super.onPostExecute(result);
		
		 dismissDialog(progress_bar_type);
	 Toast.makeText(getBaseContext(), ""+result, Toast.LENGTH_SHORT).show();
	 uploadingStateTextBox.setText(""+result);   
	 
	}
	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		 
		 pDialog.setProgress(Integer.parseInt(values[0]));  
	  
	}
	 
	
	@Override
	protected String doInBackground(String... params) {
   
		String response="error";
		boolean state=ftp.ftpConnect("ftp.drivehq.com", "naqib321", "03469280195", 21);
		 long filesize=0;
		 ftpClient= FtpConnection.mFTPClient; 
		    try {
		    	 File file=new File(params[0]);
		         FileInputStream srcFileStream = new FileInputStream(file);
                 filesize=file.length();
		        // change working directory to the destination directory
		      //  if (ftpChangeDirectory(desDirectory)) {
		        OutputStream  out = ftpClient.storeFileStream("/Audio/"+file.getName());//(desFileName, srcFileStream);
		       byte[] data=new byte[2048];
		       int l;
		       long totalUpload = 0;
		       while((l=srcFileStream.read(data))!=-1)
		       {
		    	   totalUpload+=l;
		    	   Log.i("total download", ""+(int)totalUpload/1024);
		            publishProgress(""+(int)((totalUpload*100)/filesize));
		        	out.write(data, 0, l);   
		        //	if (isCancelled())break;
		        	 
		       }
		       
		        out.flush();
		        out.close();
		      //  }
		       if(totalUpload>=(filesize-2048))
		       { response="complete";}
		        srcFileStream.close();
		   
		    } catch (Exception e) {
		        Log.d("TAG", "upload failed");
		    }
          ftp.ftpDisconnect();
		  return response;

	}
  

  
 
}
 public static String getPath(Context context, Uri uri) throws URISyntaxException {
     if ("content".equalsIgnoreCase(uri.getScheme())) {
         String[] projection = { "_data" };
         Cursor cursor = null;

         try {
             cursor = context.getContentResolver().query(uri, projection, null, null, null);
             int column_index = cursor
             .getColumnIndexOrThrow("_data");
             if (cursor.moveToFirst()) {
                 return cursor.getString(column_index);
             }
         } catch (Exception e) {
             // Eat it
         }
     }

     else if ("file".equalsIgnoreCase(uri.getScheme())) {
         return uri.getPath();
     }

     return null;
 } 
	 

}