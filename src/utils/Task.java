package utils;


import java.text.DateFormat;
import java.util.Date;

public class Task {
	private String title;
	private String priority;
	private String decription;
	private Date startDate;
	private Date endDate;
	
	
	public String getDecription() {
		return decription;
	}
	public void setDecription(String decription) {
		this.decription = decription;
	}
		
	
	public Task(String title, String priority, String decription,
			Date startDate, Date endDate) {
		super();
		this.title = title;
		this.priority = priority;
		this.decription = decription;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Task() {
		
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartdate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	//Debugging Method
	public void printInfo(){
		System.out.println("Title: " + getTitle());
		System.out.println("Priority: " + getPriority());
		System.out.println("Description: " + getDecription());
		System.out.println("StartDate: " + getStartDate());
		System.out.println("EndDate: " + getEndDate());
	}
}
