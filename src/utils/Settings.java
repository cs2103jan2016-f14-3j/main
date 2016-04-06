package utils;

/**
 * @@author A0121628L
 *Holder object which contains all the settings variables
 */
public class Settings {
	private String storagePath;
	private String backgroundColour;
	private String tabColour;
	private String buttonColour;
	private String oddCellColour;
	private String evenCellColour;
	

	public Settings() {
		
	}	

	public Settings(String storagePath, String backgroundColour,
			String tabColour, String buttonColour, String oddCellColour,
			String evenCellColour) {
		this.storagePath = storagePath;
		this.backgroundColour = backgroundColour;
		this.tabColour = tabColour;
		this.buttonColour = buttonColour;
		this.oddCellColour = oddCellColour;
		this.evenCellColour = evenCellColour;
	}
	public String getStoragePath() {
		return storagePath;
	}
	
	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}
	public String getBackgroundColour() {
		return backgroundColour;
	}
	public void setBackgroundColour(String backgroundColour) {
		this.backgroundColour = backgroundColour;
	}

	public String getTabColour() {
		return tabColour;
	}

	public void setTabColour(String tabColour) {
		this.tabColour = tabColour;
	}

	public String getButtonColour() {
		return buttonColour;
	}

	public void setButtonColour(String buttonColour) {
		this.buttonColour = buttonColour;
	}

	public String getOddCellColour() {
		return oddCellColour;
	}

	public void setOddCellColour(String oddCellColour) {
		this.oddCellColour = oddCellColour;
	}

	public String getEvenCellColour() {
		return evenCellColour;
	}

	public void setEvenCellColour(String evenCellColour) {
		this.evenCellColour = evenCellColour;
	}


	

}
