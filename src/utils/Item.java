package utils;

import java.util.Comparator;
import java.util.Date;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;

/**
 * @@author A0121628L
 *
 */
public class Item implements Comparator<Item> {
	private Long id;
	private String title;
	private String type;
	private String priority;
	private String description;
	private String status;
	private String label;
	private Date startDate;
	private Date endDate;
	private Boolean checkBox;
	private boolean isRecurring;
	private Long prevId;
	private Long nextId;
	
	public boolean isRecurring() {
		return isRecurring;
	}

	// Check box code for testing for now
	private SimpleBooleanProperty checked = new SimpleBooleanProperty(false);

	public SimpleBooleanProperty checkedProperty() {
		return this.checked;
	}

	public Boolean getChecked() {
		return this.checkedProperty().get();
	}

	public void setChecked(final Boolean checked) {
		this.checkedProperty().set(checked);
	}

	public Item(Long id, String type, String title, String priority,
			String description, String status, String label, Date startDate,
			Date endDate) {
		super();
		this.id = id;
		this.type = type;
		this.title = title;
		this.priority = priority;
		this.description = description;
		this.status = status;
		this.label = label;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isRecurring = false;
		this.prevId = null;
		this.nextId = null;
	}

	public Item() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long i) {
		this.id = i;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Boolean getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(Boolean checkBox) {
		this.checkBox = checkBox;
	}
	
	public boolean getIsRecurring() {
		return isRecurring;
	}
	
	public void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	public Long getPrevId() {
		return prevId;
	}

	public void setPrevId(Long prevId) {
		this.prevId = prevId;
	}

	public Long getNextId() {
		return nextId;
	}

	public void setNextId(Long nextId) {
		this.nextId = nextId;
	}

	// Debugging Method
	public void printInfo() {
		System.out.println("");
		System.out.println("Task ID: " + getId());
		System.out.println("Type: " + getType());
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
	public int compare(Item item1, Item item2) {
		if (item2 == null) {
			return 1;
		}
		if (item1 == null) {
			return -1;
		}
		if (item1.getEndDate().compareTo(item2.getEndDate()) > 0) {
			return 1;
		}
		else if (item1.getEndDate().compareTo(item2.getEndDate()) < 0) {
			return -1;
		} 
		else if (item1.getEndDate().compareTo(item2.getEndDate()) == 0) {
			return 0;
		}
		return 0;
	}
}
