package utils;

import java.sql.Date;
import java.text.DateFormat;

public class Task {
	private String title;
	private String priority;
	private CustomDate Startdate;
	private CustomDate endDate;
	
	public Task(String title, String priority, String description,CustomDate Startdate, CustomDate endDate) {
		
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public CustomDate getStartdate() {
		return Startdate;
	}
	public void setStartdate(CustomDate startdate) {
		Startdate = startdate;
	}
	public CustomDate getEndDate() {
		return endDate;
	}
	public void setEndDate(CustomDate endDate) {
		this.endDate = endDate;
	}
	//Debugging Method
	public void printInfo(){
		System.out.println("Title: " + getTitle());
		System.out.println("Priority: " + getPriority());
		System.out.println("Description");
	}
}
