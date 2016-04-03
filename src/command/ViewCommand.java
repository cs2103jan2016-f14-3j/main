package command;

import main.POMPOM;

public class ViewCommand extends Command {

	private static final String MESSAGE_VIEW = "%s tab has been selected for viewing.";
	private static final String MESSAGE_ERROR = "%s is not a valid tab.";
	private String tab;

	public ViewCommand(String tab) {
		this.tab = tab;
	}

	@Override
	public String execute() {

		if (tab.equals(POMPOM.LABEL_COMPLETED_EVENT.toLowerCase())
				|| tab.equals(POMPOM.LABEL_COMPLETED_TASK.toLowerCase()) || tab.equals(POMPOM.LABEL_EVENT.toLowerCase())
				|| tab.equals(POMPOM.LABEL_SEARCH.toLowerCase()) || tab.equals(POMPOM.LABEL_TASK.toLowerCase())) {

			POMPOM.setCurrentTab(tab);
			returnMsg = String.format(MESSAGE_VIEW, tab);
			return returnMsg;

		} else {

			returnMsg = String.format(MESSAGE_ERROR, tab);
			return returnMsg;

		}

	}

}
