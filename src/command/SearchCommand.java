package command;

import java.util.ArrayList;

import main.POMPOM;
import utils.Item;

public class SearchCommand extends Command {
	
	private static final String MESSAGE_SEARCH = "Search resulted in %s result(s).";
	
	public ArrayList<Item> searchResults;
	private String keyword;
	
	public SearchCommand(String keyword) {
		this.searchResults = new ArrayList<Item>();
		this.keyword = keyword;
	}
	
	private ArrayList<Item> search() {
		
		ArrayList<Item> taskList = getTaskList();
		for (int i = 0; i < taskList.size(); i++) {
			Item currentTask = taskList.get(i);
			if (currentTask.getTitle().contains(keyword)) {
				searchResults.add(currentTask);
			}
		}
		
		return searchResults;
		
	}
	
	public String execute() {
		POMPOM.setSearchList(search());
		POMPOM.setCurrentTab(POMPOM.LABEL_SEARCH);
		returnMsg = String.format(MESSAGE_SEARCH, searchResults.size());
		return returnMsg;
	}

}
