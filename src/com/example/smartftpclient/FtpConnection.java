package com.example.smartftpclient;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class FtpConnection {

    private static final String TAG = "Ftp Connection";
	public static FTPClient mFTPClient = null;
	public Context context;
	public List<String> files ;
    public List<String> directories ;
	public FtpConnection(Context c)
	{
		context=c;
	}
	 
    public boolean ftpConnect(String host, String username, String password, int port)
	        {
	         
	            mFTPClient = new FTPClient();
	            try {
					mFTPClient.connect(host, port);
				
	            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
	               {
	                boolean status = mFTPClient.login(username, password);
	                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
	                mFTPClient.enterLocalPassiveMode();
	                Log.d(TAG, "Connect to " + host + " succesfully");

	                return status;
	               }} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						 return  false; 
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						 return  false;   
					}
	      return  false;     
	   }
    
	    // Method to disconnect from FTP server:
   public static boolean ftpDisconnect() {
	        try {
	            mFTPClient.logout();
	            mFTPClient.disconnect();
	            return true;
	        } catch (Exception e) {
	            Log.d(TAG, "Error occurred while disconnecting from ftp server.");
	            return false;
	        }

	     //   return false;
	    }
      // method to get Uploading directory 
   public static String ftpGetCurrentWorkingDirectory()
        {
	        try {
	            String workingDir = mFTPClient.printWorkingDirectory();
	            return workingDir;
	        } catch (Exception e) {
	            Log.d(TAG, "Error: could not get current working directory.");
	        }

	        return null;
	    }
    // method to change uploading directory  
  public static boolean ftpChangeDirectory(String directory_path) 
       {
	        try {
	            mFTPClient.changeWorkingDirectory(directory_path);
	        } catch (Exception e) {
	            Log.d(TAG, "Error: could not change directory to " + directory_path);
	        }

	        return false;
	   }
  
	    // Method to create new directory:
  public static boolean ftpMakeDirectory(String new_dir_path)
       {
	      try {
	            boolean status = mFTPClient.makeDirectory(new_dir_path);
	            return status;
	         } catch (Exception e) {
	            Log.d(TAG, "Error: could not create new directory named "
	                    + new_dir_path);
	        }

	        return false;
	    }

	    // Method to delete/remove a directory:
  public static boolean ftpRemoveDirectory(String dir_path)
	       {
	         try {
	            boolean status = mFTPClient.removeDirectory(dir_path);
	            return status;
	          } catch (Exception e) {
	            Log.d(TAG, "Error: could not remove directory named " + dir_path);
	             }

	        return false;
	       }
  // print list of all files
   public static void ftpPrintFilesList(String dir_path)
       {
	        try {
	            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
	            int length = ftpFiles.length;

	            for (int i = 0; i < length; i++) {
	                String name = ftpFiles[i].getName();
	               //   ftpFiles[i].getSize();
	                boolean isFile = ftpFiles[i].isFile();

	                if (isFile) {
	                    Log.i(TAG, "File : " + name);
	                } else {
	                    Log.i(TAG, "Directory : " + name);
	                }
	            }
	            } catch (Exception e) {
	            e.printStackTrace();
	         }
	     }
   public FTPFile[] ftpAllFilesList(String dir_path)
   {FTPFile[] ftpFiles = null;
        try {
           ftpFiles = mFTPClient.listFiles(dir_path);
            } catch (Exception e) {
            e.printStackTrace();
         }
        return ftpFiles;
     }
   // print list of all files
   public  List<String> ftpGetFilesList(String dir_path)
       {FTPFile[] ftpFiles = null;
       
	        try {
	            ftpFiles = mFTPClient.listFiles(dir_path);
	            int length = ftpFiles.length;
	            files =new ArrayList<String>();
	            directories = new ArrayList<String>();
	            for (int i = 0; i < length; i++) {
	                String name = ftpFiles[i].getName();
	                boolean isFile = ftpFiles[i].isFile();

	                if (isFile) {
	                	  files.add(name);
						// Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();
	                    Log.i(TAG, "File : " + name);
	                } else {
	                   files.add(name);
	                	directories.add(name+"    directory");
	               	// Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();
		                
	                	Log.i(TAG, "Directory : " + name);
	                }
	            }
	            } catch (Exception e) {
	            e.printStackTrace();
	         }
	        return files;
	     }
   public FTPFile[] ftpDirectory()
   {
	   FTPFile[] files=null;
	   try {
		  files = mFTPClient.listFiles();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	    return files;  
   }
   
   
	 // method to delete file from ftp server  
   public boolean ftpRemoveFile(String filePath)
       {
	        boolean status=false;
	        try {
	            status = mFTPClient.deleteFile(filePath);
	             return status;
	            } catch (Exception e) {
	            e.printStackTrace();
	           status=false;
	            }
	        

	        return status;
	    }
   //get file Size
   public long getFileSize(String filePath)   {
	    long fileSize = 0;
	    FTPFile[] files;
		try {
			files = mFTPClient.listFiles(filePath);
			 if (files.length == 1 && files[0].isFile()) {
			        fileSize = files[0].getSize();
			    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	    Log.i(TAG, "File size = " + fileSize);
	    return fileSize;
	}
   // method to rename sever file
   public boolean ftpRenameFile(String from, String to) 
         {
	     Boolean status=false;
	        try {
	             status = mFTPClient.rename(from, to);
	            return status;
	        } catch (Exception e) {
	            Log.d(TAG, "Could not rename file: " + from + " to: " + to);
	            status=false;
	            return status;
	        }

	        
	    }
   
	    // Method to upload a file to FTP server:
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
	  // Method to download a file from FTP server:
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
		        instream.close();
		        status =true;
		        return status;
		    } catch (Exception e) {
		        Log.d("TAG", "download failed");
		    }
		    return status;
	    }
	    
	  
	} // end of the ftp Connection class


