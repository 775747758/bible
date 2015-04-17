package com.wujie.help;

public class NoteInfo {
    private int id;
    private String text;
    private int year;
    private int month;
    private int day;
    private String time;
    private String week;
    private String noteClass;
    private String title;
	public NoteInfo() {
		// TODO Auto-generated constructor stub
		
	}
	public NoteInfo(int id, String text, int year, int month, int day,
			String time, String week, String noteClass, String title) {
		super();
		this.id = id;
		this.text = text;
		this.year = year;
		this.month = month;
		this.day = day;
		this.time = time;
		this.week = week;
		this.noteClass = noteClass;
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getNoteClass() {
		return noteClass;
	}
	public void setNoteClass(String noteClass) {
		this.noteClass = noteClass;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
