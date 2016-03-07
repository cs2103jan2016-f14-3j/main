package utils;


import java.text.DateFormat;
import java.util.Date;

public class Task implements Comparable<Task>{
	private String title;
	private String priority;
	private String decription;
	private String status;
	private String label;
	private Date startDate;
	private Date endDate;
	
	

	public Task(String title, String priority, String decription,
			String status, String label, Date startDate, Date endDate) {
		super();
		this.title = title;
		this.priority = priority;
		this.decription = decription;
		this.status = status;
		this.label = label;
		this.startDate = startDate;
		this.endDate = endDate;
	}	
	
//	public Task(String title, String priority, String decription,
//			Date startDate, Date endDate) {
//		super();
//		this.title = title;
//		this.priority = priority;
//		this.decription = decription;
//		this.startDate = startDate;
//		this.endDate = endDate;
//	}

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
	public String getDecription() {
		return decription;
	}
	public void setDecription(String decription) {
		this.decription = decription;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
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
		System.out.println("");
		System.out.println("Title: " + getTitle());
		System.out.println("Priority: " + getPriority());
		System.out.println("Description: " + getDecription());
		System.out.println("Status: " + getStatus());
		System.out.println("Label: " + getLabel());
		System.out.println("StartDate: " + getStartDate());
		System.out.println("EndDate: " + getEndDate());
		System.out.println("");
	}
	@Override
	public int compareTo(Task o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
