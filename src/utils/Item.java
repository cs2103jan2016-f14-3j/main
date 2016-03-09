package utils;


import java.text.DateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.adapter.JavaBeanStringProperty;

public class Item implements Comparable<Item>{
	private int id;
	private String title;
	private String priority;
	private String description;
	private String status;
	private String label;
	private Date startDate;
	private Date endDate;
	
	

	public Item(int id, String title, String priority, String description,
			String status, String label, Date startDate, Date endDate) {
		super();
		this.id = id;
		this.title = title;
		this.priority = priority;
		this.description = description;
		this.status = status;
		this.label = label;
		this.startDate = startDate;
		this.endDate = endDate;
	}	
	
//	public Task(String title, String priority, String description,
//			Date startDate, Date endDate) {
//		super();
//		this.title = title;
//		this.priority = priority;
//		this.description = description;
//		this.startDate = startDate;
//		this.endDate = endDate;
//	}


	public Item() {
		
	}	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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

		System.out.println("Task ID: " + getId());

		System.out.println("Title: " + getTitle());
		System.out.println("Priority: " + getPriority());
		System.out.println("Description: " + getDescription());
		System.out.println("Status: " + getStatus());
		System.out.println("Label: " + getLabel());
		System.out.println("StartDate: " + getStartDate());
		System.out.println("EndDate: " + getEndDate());
		System.out.println("");
	}
	@Override
	public int compareTo(Item o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
