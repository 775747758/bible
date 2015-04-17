package com.wujie.help;

public class EditImage {

	public String path;
	public int start;
	
	public EditImage() {
		// TODO Auto-generated constructor stub
		
	}
	
	public EditImage(String path, int start) {
		super();
		this.path = path;
		this.start = start;
		
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	
	

}
