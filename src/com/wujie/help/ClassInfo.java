package com.wujie.help;

public class ClassInfo {

	private int id;
	private String name;
	private int ifsecret;
	private String secret;
	public ClassInfo()
	{
		id=0;
		name="";
		ifsecret=0;
		secret="";
	}
	
	public ClassInfo(int id, String name, int ifsecret, String secret) {
		super();
		this.id = id;
		this.name = name;
		this.ifsecret = ifsecret;
		this.secret = secret;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIfsecret() {
		return ifsecret;
	}
	public void setIfsecret(int ifsecret) {
		this.ifsecret = ifsecret;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
}