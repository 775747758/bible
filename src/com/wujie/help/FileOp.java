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
	              if (oldfile.exists()) { //�ļ�����ʱ 
	                  InputStream inStream = new FileInputStream(oldPath); //����ԭ�ļ� 
	                  FileOutputStream fs = new FileOutputStream(newPath); 
	                  byte[] buffer = new byte[1024]; 
	                  int length; 
	                  while ( (byteread = inStream.read(buffer)) != -1) { 
	                      bytesum += byteread; //�ֽ��� �ļ���С 
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
	             // System.out.println("���Ƶ����ļ���������"); 
	             // e.printStackTrace(); 
	              isok = false;
	          } 
	          return isok;

	      } 

  /** 
    * ���������ļ������� 
    * @param oldPath String ԭ�ļ�·�� �磺c:/fqf 
    * @param newPath String ���ƺ�·�� �磺f:/fqf/ff 
    * @return boolean 
    */ 
  public boolean copyFolder(String oldPath, String newPath) { 
   boolean isok = true;
      try { 
          (new File(newPath)).mkdirs(); //����ļ��в����� �������ļ��� 
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
              if(temp.isDirectory()){//��������ļ��� 
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
	    // �ж�Ŀ¼���ļ��Ƿ����  
	    if (!file.exists()) {  // �����ڷ��� false  
	        return flag;  
	    } else {  
	        // �ж��Ƿ�Ϊ�ļ�  
	       if (file.isFile()) {  // Ϊ�ļ�ʱ����ɾ���ļ�����  
	            return deleteFile(sPath);  
	      } else {  // ΪĿ¼ʱ����ɾ��Ŀ¼����  
	           return deleteDirectory(sPath);  
	       }  
	    }  
	}
   public boolean deleteFile(String sPath) {  
    	    boolean flag = false;  
    	   File file = new File(sPath);  
    	   // ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��  
	    if (file.isFile() && file.exists()) {  
	       file.delete();  
	       flag = true;  
	   }  
	   return flag;  
	}  
  public boolean deleteDirectory(String sPath) {  
      //���sPath�����ļ��ָ�����β���Զ�����ļ��ָ���  
     if (!sPath.endsWith(File.separator)) {  
         sPath = sPath + File.separator;  
       }  
       File dirFile = new File(sPath);  
      //���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�  
       if (!dirFile.exists() || !dirFile.isDirectory()) {  
           return false;  
       }  
       boolean flag = true;  
       //ɾ���ļ����µ������ļ�(������Ŀ¼)  
       File[] files = dirFile.listFiles();  
     for (int i = 0; i < files.length; i++) {  
          //ɾ�����ļ�  
           if (files[i].isFile()) {  
               flag = deleteFile(files[i].getAbsolutePath());  
               if (!flag) break;  
           } //ɾ����Ŀ¼  
           else {  
               flag = deleteDirectory(files[i].getAbsolutePath());  
               if (!flag) break;  
           }  
       }  
       if (!flag) return false;  
       //ɾ����ǰĿ¼  
       if (dirFile.delete()) {  
           return true;  
       } else {  
           return false;  
       }  
   }   
}
