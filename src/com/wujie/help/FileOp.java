package com.wujie.help;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileOp {

	public FileOp() {
		// TODO Auto-generated constructor stub
	}
	public boolean copyFile(String oldPath, String newPath) { 
	   	   boolean isok = true;
	          try { 
	              int bytesum = 0; 
	              int byteread = 0; 
	              File oldfile = new File(oldPath); 
	              if (oldfile.exists()) { //文件存在时 
	                  InputStream inStream = new FileInputStream(oldPath); //读入原文件 
	                  FileOutputStream fs = new FileOutputStream(newPath); 
	                  byte[] buffer = new byte[1024]; 
	                  int length; 
	                  while ( (byteread = inStream.read(buffer)) != -1) { 
	                      bytesum += byteread; //字节数 文件大小 
	                      //System.out.println(bytesum); 
	                      fs.write(buffer, 0, byteread); 
	                  } 
	                  fs.flush(); 
	                  fs.close(); 
	                  inStream.close(); 
	              }
	              else
	              {
	   			isok = false;
	   		   }
	          } 
	          catch (Exception e) { 
	             // System.out.println("复制单个文件操作出错"); 
	             // e.printStackTrace(); 
	              isok = false;
	          } 
	          return isok;

	      } 

  /** 
    * 复制整个文件夹内容 
    * @param oldPath String 原文件路径 如：c:/fqf 
    * @param newPath String 复制后路径 如：f:/fqf/ff 
    * @return boolean 
    */ 
  public boolean copyFolder(String oldPath, String newPath) { 
   boolean isok = true;
      try { 
          (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
          File a=new File(oldPath); 
          String[] file=a.list(); 
          File temp=null; 
          for (int i = 0; i < file.length; i++) { 
              if(oldPath.endsWith(File.separator)){ 
                  temp=new File(oldPath+file[i]); 
              } 
              else
              { 
                  temp=new File(oldPath+File.separator+file[i]); 
              } 

              if(temp.isFile()){ 
                  FileInputStream input = new FileInputStream(temp); 
                  FileOutputStream output = new FileOutputStream(newPath + "/" + 
                          (temp.getName()).toString()); 
                  byte[] b = new byte[1024 * 5]; 
                  int len; 
                  while ( (len = input.read(b)) != -1) { 
                      output.write(b, 0, len); 
                  } 
                  output.flush(); 
                  output.close(); 
                  input.close(); 
              } 
              if(temp.isDirectory()){//如果是子文件夹 
                  copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
              } 
          } 
      } 
      catch (Exception e) { 
   	    isok = false;
      } 
      return isok;
  }
	   	////////////////////////////////////////////////////////////
  public boolean DeleteFolder(String sPath) {  
	  boolean  flag = false;  
	   File file = new File(sPath);  
	    // 判断目录或文件是否存在  
	    if (!file.exists()) {  // 不存在返回 false  
	        return flag;  
	    } else {  
	        // 判断是否为文件  
	       if (file.isFile()) {  // 为文件时调用删除文件方法  
	            return deleteFile(sPath);  
	      } else {  // 为目录时调用删除目录方法  
	           return deleteDirectory(sPath);  
	       }  
	    }  
	}
   public boolean deleteFile(String sPath) {  
    	    boolean flag = false;  
    	   File file = new File(sPath);  
    	   // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	       file.delete();  
	       flag = true;  
	   }  
	   return flag;  
	}  
  public boolean deleteDirectory(String sPath) {  
      //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
     if (!sPath.endsWith(File.separator)) {  
         sPath = sPath + File.separator;  
       }  
       File dirFile = new File(sPath);  
      //如果dir对应的文件不存在，或者不是一个目录，则退出  
       if (!dirFile.exists() || !dirFile.isDirectory()) {  
           return false;  
       }  
       boolean flag = true;  
       //删除文件夹下的所有文件(包括子目录)  
       File[] files = dirFile.listFiles();  
     for (int i = 0; i < files.length; i++) {  
          //删除子文件  
           if (files[i].isFile()) {  
               flag = deleteFile(files[i].getAbsolutePath());  
               if (!flag) break;  
           } //删除子目录  
           else {  
               flag = deleteDirectory(files[i].getAbsolutePath());  
               if (!flag) break;  
           }  
       }  
       if (!flag) return false;  
       //删除当前目录  
       if (dirFile.delete()) {  
           return true;  
       } else {  
           return false;  
       }  
   }   
}
