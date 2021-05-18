package com.example.smartftpclient;
 

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
 
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
Button btnBrowseSeverFile;
Button btnUploadfile;
Button btnServerSetting;
public static FTPClient mFTPClient = null;
public FtpConnection ftpp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       ftpp=new FtpConnection(this);
         btnBrowseSeverFile=(Button)findViewById(R.id.bntBrowse);
         btnUploadfile=(Button)findViewById(R.id.bntUpload);
         btnServerSetting=(Button)findViewById(R.id.bntSetting);
         
         btnBrowseSeverFile.setOnClickListener(this);
         btnUploadfile.setOnClickListener(this);
         btnServerSetting.setOnClickListener(this);
         
    
    }
 	public void onClick(View v)
	{
 		switch (v.getId()) {
		case R.id.bntBrowse:
			startActivity(new Intent(this,ServerItemsActivity.class));
			break;
        case R.id.bntUpload:
      // new MyUploaderClass().execute("path");
     startActivity(new Intent(this,UploadingActivity.class));
			break;
       case R.id.bntSetting:
    	   startActivity(new Intent(this,ServerSettingActivity.class));
			break;
        default:
			break;
		}
		// TODO Auto-generated method stub
		
	}
 	
 	
 	
 	
 	
 	
 	
	private class MyUploaderClass extends AsyncTask<String, Integer , String> 
	 { private FTPClient ftp = null;
		 boolean running=true;

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		 Toast.makeText(getBaseContext(), "complete .."+result, Toast.LENGTH_SHORT).show();
		 	}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			  	 
		}
		 
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			  super.onPreExecute();
			  
			  Toast.makeText(getBaseContext(), "uploading Start", Toast.LENGTH_SHORT).show();
			 }

		@Override
		protected String doInBackground(String... params) {
			ftp = new FTPClient();
          Boolean stat=false;
          
	 //stat=	ftpp.ftpConnect("ftp.drivehq.com", "naqib321", "03469280195", 21);
        stat=	ftpConnect("ftp.drivehq.com", "naqib321", "03469280195", 21);
     //   boolean res=ftpDownload("/picture/my.jpg", "mnt/sdcard/my.jpg");
        boolean res=ftpUpload("mnt/sdcard/2012.jpg", "/Audio/myPic.jpg", "Audio") ;  
			return res+"result.."+stat;
		  }
	  
 
	}
 // ftp connection mehtods.....	
 public boolean ftpConnect(String host, String username, String password, int port)
     {
try {
   mFTPClient = new FTPClient();
// connecting to the host
     mFTPClient.connect(host, port);

// now check the reply code, if positive mean connection success
   // if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
// login using username & password
    boolean status = mFTPClient.login(username, password);

/* Set File Transfer Mode
*
* To avoid corruption issue you must specified a correct
* transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
* EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE
* for transferring text, image, and compressed files.
*/
 mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
 mFTPClient.enterLocalPassiveMode();

return true;//status;
//}
} catch(Exception e) {
Log.d("connection msg", "Error: could not connect to host " + host );
}
return false;
}
public static String ftpGetCurrentWorkingDirectory() {
    try {
        String workingDir = mFTPClient.printWorkingDirectory();
        return workingDir;
    } catch (Exception e) {
        Log.d("TAG", "Error: could not get current working directory.");
    }

    return null;
}
public static boolean ftpChangeDirectory(String directory_path) {
    try {
        mFTPClient.changeWorkingDirectory(directory_path);
    } catch (Exception e) {
        Log.d("TAG", "Error: could not change directory to " + directory_path);
    }

    return false;
}
public static void ftpPrintFilesList(String dir_path) {
    try {
        FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
        int length = ftpFiles.length;

        for (int i = 0; i < length; i++) {
            String name = ftpFiles[i].getName();
            boolean isFile = ftpFiles[i].isFile();

            if (isFile) {
                Log.i("TAG", "File : " + name);
            } else {
                Log.i("TAG", "Directory : " + name);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public static boolean ftpRemoveFile(String filePath) {
    try {
        boolean status = mFTPClient.deleteFile(filePath);
        return status;
    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
public static boolean ftpDownload(String srcFilePath, String desFilePath) 
{
    boolean status = false;
    try {
        FileOutputStream desFileStream = new FileOutputStream(desFilePath);  
        InputStream instream = mFTPClient.retrieveFileStream(srcFilePath);
        
        Log.i("inputstream", instream.available()+""+instream);
           int l;
        byte[] tmp = new byte[1024];
        int bytesDownloaded = 0;
        while ((l = instream.read(tmp)) != -1) {
        	 bytesDownloaded+=1024;
        	desFileStream.write(tmp, 0, l);
           Log.i("total download", ""+(int)bytesDownloaded/1024);
         }
        desFileStream.close();
 
        return status;
    } catch (Exception e) {
        Log.d("TAG", "download failed");
    }

    return status;
}
public static boolean ftpUpload(String srcFilePath, String desFileName,
        String desDirectory) 
  {
    boolean status = false;
    try {
        FileInputStream srcFileStream = new FileInputStream(srcFilePath);

        // change working directory to the destination directory
      //  if (ftpChangeDirectory(desDirectory)) {
        OutputStream  out = mFTPClient.storeFileStream(desFileName);//(desFileName, srcFileStream);
       byte[] data=new byte[1024];
       int l;
       long totalUpload = 0;
       while((l=srcFileStream.read(data))!=-1)
       {
    	   totalUpload+=l;
    	   Log.i("total download", ""+(int)totalUpload/1024);
        	out.write(data, 0, l);   
       }
       status=true;
       out.flush();
        out.close();
      //  }
       
        srcFileStream.close();
        return status;
    } catch (Exception e) {
        Log.d("TAG", "upload failed");
    }

    return status;
 }
}
