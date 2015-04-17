package com.orange.church;

public class User {
	
	private String userName;
	private String password;
	private String name;
	private String portraitUri;
	private String birthday;
	private String gender;
	private String userLocation;
	private String userLatitude;
	private String userLongtitude;
	private String tableType="User";
	private String churchCount;
	private Church church;
	private String qq;
	
	
	
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public User(String userName, String password, String name, String portraitUri, String birthday, String gender,String qq) {
		super();
		this.userName = userName;
		this.password = password;
		this.name = name;
		this.portraitUri = portraitUri;
		this.birthday = birthday;
		this.gender = gender;
		this.qq = qq;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPortraitUri() {
		return portraitUri;
	}
	public void setPortraitUri(String portraitUri) {
		this.portraitUri = portraitUri;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getUserLocation() {
		return userLocation;
	}
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	public String getUserLatitude() {
		return userLatitude;
	}
	public void setUserLatitude(String userLatitude) {
		this.userLatitude = userLatitude;
	}
	public String getUserLongtitude() {
		return userLongtitude;
	}
	public void setUserLongtitude(String userLongtitude) {
		this.userLongtitude = userLongtitude;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public String getChurchCount() {
		return churchCount;
	}
	public void setChurchCount(String churchCount) {
		this.churchCount = churchCount;
	}
	public Church getChurch() {
		return church;
	}
	public void setChurch(Church church) {
		this.church = church;
	}
	
	
	

}
