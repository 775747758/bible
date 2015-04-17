package com.orange.church;

import java.util.List;

public class Church {
	
	private String pastorName;
	private String churchName;
	private String churchType;
	private String churchMission;
	private String churchSight;
	private String churchIconUri;
	private String churchLocation;
	private String churchLatitude;
	private String churchLongtitude;
	private String tableType="Church";
	private List<User> churchMember;
	private String churchCount;
	private String churchCreateDate;
	private String churchNotice;
	
	
	public String getChurchCount() {
		return churchCount;
	}
	public void setChurchCount(String churchCount) {
		this.churchCount = churchCount;
	}
	public String getChurchCreateDate() {
		return churchCreateDate;
	}
	public void setChurchCreateDate(String churchCreateDate) {
		this.churchCreateDate = churchCreateDate;
	}
	public String getChurchNotice() {
		return churchNotice;
	}
	public void setChurchNotice(String churchNotice) {
		this.churchNotice = churchNotice;
	}
	public Church(String pastorName, String churchName, String churchType, String churchMission, String churchSight, String churchIconUri) {
		super();
		this.pastorName = pastorName;
		this.churchName = churchName;
		this.churchType = churchType;
		this.churchMission = churchMission;
		this.churchSight = churchSight;
		this.churchIconUri = churchIconUri;
	}
	public String getPastorName() {
		return pastorName;
	}
	public void setPastorName(String pastorName) {
		this.pastorName = pastorName;
	}
	public String getChurchName() {
		return churchName;
	}
	public void setChurchName(String churchName) {
		this.churchName = churchName;
	}
	public String getChurchType() {
		return churchType;
	}
	public void setChurchType(String churchType) {
		this.churchType = churchType;
	}
	public String getChurchMission() {
		return churchMission;
	}
	public void setChurchMission(String churchMission) {
		this.churchMission = churchMission;
	}
	public String getChurchSight() {
		return churchSight;
	}
	public void setChurchSight(String churchSight) {
		this.churchSight = churchSight;
	}
	public String getChurchIconUri() {
		return churchIconUri;
	}
	public void setChurchIconUri(String churchIconUri) {
		this.churchIconUri = churchIconUri;
	}
	public String getChurchLocation() {
		return churchLocation;
	}
	public void setChurchLocation(String churchLocation) {
		this.churchLocation = churchLocation;
	}
	public String getChurchLatitude() {
		return churchLatitude;
	}
	public void setChurchLatitude(String churchLatitude) {
		this.churchLatitude = churchLatitude;
	}
	public String getChurchLongtitude() {
		return churchLongtitude;
	}
	public void setChurchLongtitude(String churchLongtitude) {
		this.churchLongtitude = churchLongtitude;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public List<User> getChurchMember() {
		return churchMember;
	}
	public void setChurchMember(List<User> churchMember) {
		this.churchMember = churchMember;
	}
	
	
	

}
