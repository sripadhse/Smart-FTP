package com.example.smartftpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.Notification.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class ServerItemsActivity  extends ListActivity  {
	 // Progress Dialog
     private ProgressDialog pDialog;
     public static final int progress_bar_type = 0; 
     public static final int progress_bar_type1 = 1; 
  	 private List<String> items = null;
	 private FtpConnection ftpConn;
	 private List<String> list;
	 private List<String> fileSizeList;
	 private List<String> asDirectory;
	 private FTPFile[] Ftpfiles;
	 private  Dialog progress;
	 int selectedRow=0;
	 private String path="";
	 private String fileName="";
	 private Boolean running=false;
	 private String renameFileName="";
	 private String[] commandArray=new String[] {"Create","Delete","Rename","Download"};
	 private static FTPClient ftpClient;
	 @Override
      public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        setContentView(R.layout.list_main);
	        ftpConn=new FtpConnection(this);
	        refereshList();
		 }
	 public void refereshList()
	 { 
		 showDialog(progress_bar_type);
		 new Thread(new Runnable() {
		     	public void run() {
					// TODO Auto-generated method stub
		     		if(running==false){
		     		   running=ftpConn.ftpConnect("ftp.drivehq.com", "naqib321", "03469280195", 21);
		     		 }
		     		runOnUiThread(new Runnable() {
						    public void run() {
							   getFiles(ftpConn.ftpDirectory());
							   cancelDialge();
						    	}
						    });
				}
			}).start();     
	 }
	    @Override
	    protected void onListItemClick(ListView l, View v, int position, long id){
	    	 
	      selectedRow = (int)id;
	     
	        if(selectedRow == 0){
	        	path="";
	          showDialog(progress_bar_type);
	        	new Thread(new Runnable() {
				 	public void run() {
						runOnUiThread(new Runnable() {
						 	public void run() {	 
							      getFiles(ftpConn.ftpDirectory());
							      cancelDialge();
							     }
						});
					 }
				}).start();
	            
	        }else{
	             String state=asDirectory.get(selectedRow);
	               if(state.equals("D")){
	            	   path+="/"+items.get(selectedRow);
	            	   fileName=items.get(selectedRow);
	            	   showDialog(progress_bar_type); 
	            	   new Thread(new Runnable() {
	   				 	public void run() {
	   						runOnUiThread(new Runnable() {
	   						 	public void run() {	 
	   							    getAll();
	   							     }
	   						});
	   					 }
	   				}).start();
	            }
	              else{
	            	 
	            	  fileName=items.get(selectedRow);
	              
	            	  showDialog();
	            }
	        }
	       }
	    private void showDialog()
	     {
	    	 android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);  
	         builder.setTitle("Single Choice");  
	         builder.setItems(commandArray, new DialogInterface.OnClickListener() {  
	         public void onClick(DialogInterface dialog, int which) {
	        	      
	        	      takeAction(which);
	        	      dialog.dismiss();  
	           }  
	         });  
	         builder.setNegativeButton("cancel",  
	             new DialogInterface.OnClickListener() {  
	                
	               public void onClick(DialogInterface dialog, int which) {  
	                 dialog.dismiss();  
	               }  
	             });  
	         AlertDialog alert = builder.create();  
	         alert.show();  
	     }
	    private void takeAction(int which)
	    {
	    	   if(commandArray[which].equals("Rename"))
        	   {
        		   inputDialog();
        	   }else if(commandArray[which].equals("Download"))
        	       {
        		   FtpOperation ftpOper=new FtpOperation();
        		                ftpOper.execute("");
        	        }
        	   else if(commandArray[which].equals("Create"))
    	       {
    		   
        		   Toast.makeText(getBaseContext(), "Create operation are not added Now", Toast.LENGTH_SHORT).show();
          	       
    	        }
        	   else if(commandArray[which].equals("Delete"))
    	       {  showDialog(progress_bar_type);
    		   new Thread(new Runnable() {
				 public void run() {
				  final boolean b= ftpConn.ftpRemoveFile(path+"/"+fileName);
					runOnUiThread(new Runnable() {
					 	public void run() {	 
					 		 if (b==true) {
			    	        	 Toast.makeText(getBaseContext(), "Delete operation Complete", Toast.LENGTH_SHORT).show();
			    	    	     getAll(); 
			    	    	  }
			    	         else {
			    	        	 Toast.makeText(getBaseContext(), "Delete operation Failed", Toast.LENGTH_SHORT).show();
			    	        	 dismissDialog(progress_bar_type);   
			    	         }
			    	         }
					});
					 
		    	        
				}
		    	}).start();
    	       
    	       }
        	   
	    }
	    private void inputDialog()
	     {
	    	 AlertDialog.Builder alert = new AlertDialog.Builder(this);

	    	 alert.setTitle("Please Enter File Name");
	    	 final EditText input = new EditText(this);
	    	                input.setHint("Enter file Name");
	    	                input.setMaxLines(1);
	    	                input.setMaxEms(10);
	    	                 
	    	                
	    	 alert.setView(input);

	    	 alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    	 public void onClick(DialogInterface dialog, int whichButton) {
	    	    renameFileName= input.getText().toString();
	    	    if((renameFileName.length()!=0)&&(renameFileName!=null))
	    	    {
	    	    	final int i=fileName.lastIndexOf('.');
	    	        showDialog(progress_bar_type);
	      		   new Thread(new Runnable() {
	  				 public void run() {
	  				  final boolean b= ftpConn.ftpRenameFile(path+"/"+fileName, path+"/"+renameFileName+"."+fileName.substring(i+1));
	  					runOnUiThread(new Runnable() {
	  					 	public void run() {	 
	  					 		 if (b==true) {
	  			    	        	 Toast.makeText(getBaseContext(), "Delete operation Complete", Toast.LENGTH_SHORT).show();
	  			    	    	     getAll(); 
	  			    	    	  }
	  			    	         else {
	  			    	        	 Toast.makeText(getBaseContext(), "Rename operation Failed", Toast.LENGTH_SHORT).show();
	  			    	     	     dismissDialog(progress_bar_type);   
	  			    	          }
	  			    	         }
	  					});
	  					 
	  		    	        
	  				}
	  		    	}).start();	 
	    	    }
	    	   }
	    	 });

	    	 alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	   public void onClick(DialogInterface dialog, int whichButton) {
	    		   Toast.makeText(getBaseContext(), "Cancel is pressed", Toast.LENGTH_SHORT).show();
	          	    	    	   }
	    	 });

	    	 alert.show();
	     }
	    public void getAll()
	    {
	    	 getFiles(ftpConn.ftpAllFilesList(path));
	    	 cancelDialge();
	    }
	    private void getFiles(FTPFile[] files ){
	        items = new ArrayList<String>();
	        fileSizeList = new ArrayList<String>();
	        asDirectory = new ArrayList<String>();
	        items.add("Go Back");
	        fileSizeList.add("Go Back");
	        asDirectory.add("Go Back");
	        if(files.length>0)
	        {
	         for(FTPFile  file : files){
	         
	        	 if(file.isDirectory())
	        	 {
	        		 asDirectory.add("D");
	                 items.add(file.getName());
	                 fileSizeList.add(""+file.getSize());
	        	 }
	        	 else
	        	 {
	        		    asDirectory.add("F");
		                items.add(file.getName());
		                fileSizeList.add(""+file.getSize());
	        	 }
	        }
	       
	        }
	  
	        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,R.layout.file_list_row, items);
	        setListAdapter(fileList);
	       
	        
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
		      case progress_bar_type1:
		          pDialog = new ProgressDialog(this);
		          pDialog.setMessage("Downloading file.....");
		          pDialog.setIndeterminate(false);
		          pDialog.setMax(100);
		          pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		          pDialog.setCancelable(true);
		           pDialog.show();
		          return pDialog; 
		     
		      default:
		          return null;
		      }
        }
	   
	    public void cancelDialge()
	    {
	    	  dismissDialog(progress_bar_type);
	    }
	   
	 	private class FtpOperation extends AsyncTask<String, String, String>
		{
			private String response="";
			private Boolean status=false;
         	@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				 
				 showDialog(progress_bar_type1);
				 
				 }
			@Override
			protected void onCancelled() {
				// TODO Auto-generated method stub
				super.onCancelled();
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				 dismissDialog(progress_bar_type1);
				 pDialog=null;
			   }

			@Override
			protected void onProgressUpdate(String... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
				 pDialog.setProgress(Integer.parseInt(values[0]));  
			}

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
			 
			 	
			 	  ftpClient= FtpConnection.mFTPClient;
			 	  int filesize=(int)ftpConn.getFileSize(path+"/"+fileName);
			 	  long size=  Long.parseLong( fileSizeList.get(selectedRow));
			 	
			 	  boolean status = false;
					    try {
					        FileOutputStream desFileStream = new FileOutputStream("mnt/sdcard/"+fileName);  
					        InputStream instream = ftpClient.retrieveFileStream(path+"/"+fileName);
					     
					        Log.i("inputstream", instream.available()+""+instream);
					           int l;
					        byte[] tmp = new byte[2048];
					        int bytesDownloaded = 0;
					        while ((l = instream.read(tmp)) != -1) {
					        	 bytesDownloaded+=l;
					        	desFileStream.write(tmp, 0, l);
					            publishProgress(""+(int)((bytesDownloaded*100)/size));
					           Log.i("total download", ""+(int)((bytesDownloaded*100)/size));
					         }
					        desFileStream.close();
					        instream.close();
					        status =true;
					        ftpClient=null;
					    } catch (Exception e) {
					        Log.d("TAG", "download failed");
					        status=false;
					    }
					    return ""+status;
				 
				 
			}
			
		}
}