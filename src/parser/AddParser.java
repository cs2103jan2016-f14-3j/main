package parser;

import java.util.List;
import java.util.Scanner;
import java.util.spi.ResourceBundleControlProvider;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import command.AddCommand;
import command.AddRecurringCommand;
import command.Command;
import command.InvalidCommand;
import main.POMPOM;

/**
 * @@author A0121760R
 */
public class AddParser {

	public static final String MESSAGE_EMPTY_ERROR = "Title cannot be empty!";
	public static final String MESSAGE_DATE_ERROR = "\"%s\" is not a valid date!";
	public static final String MESSAGE_BOUND_ERROR = "\"%s\" contains an invalid date!";
	public static final String MESSAGE_RECURRING_ERROR = "\"%s\" is not valid! Usage: \"r:<interval> until <bound date>";
	public static final String MESSAGE_EXCLUSION_ERROR = "\"%s\" is not valid! Usage: \"x:<start date> to <end date>\"";
	public static final String MESSAGE_PRIORITY_ERROR = "\"%s\" is not valid. Only high, medium or low is accepted!";
	public static final String MESSAGE_END_BEFORE_FROM = "End date cannot be before Start date!";
	public static final String MESSAGE_START_DATE_SPECIFIED = "To add a recurring task, the start date must be specified!";
	public static final String MESSAGE_DATES_SPECIFIED = "To add an event, both start and end dates must be specified!";
	public static final String MESSAGE_UNKNOWN = "Unknown error occured!";

	private static final char TAG_FROM = 'f';
	private static final char TAG_END = 'e';
	private static final char TAG_RECURRING = 'r';
	private static final char TAG_LABEL = 'l';
	private static final char TAG_PRIORITY = 'p';
	private static final char TAG_EXCEPTION = 'x';

	private PrettyTimeParser timeParser = new PrettyTimeParser();
	private int[] delimiterIndex;
	private String title;
	private String from;
	private String end;
	private String recurring;
	private String label;
	private String priority;
	private String exclusion;
	private boolean isEmpty;
	private boolean isEvent;
	private boolean isFromError;
	private boolean isEndError;
	private boolean isPriorityError;
	private boolean isRecurring;
	private boolean isRecurringError;
	private boolean isBoundError;
	private boolean isTokenizeError;
	private boolean isExclusionError;
	private boolean isExclusionDateInvalid;
	private boolean isExclusionDateSeqError;
	private boolean hasExclusion;
	
	public AddParser(String commandArgument, boolean isEvent) {
		this.delimiterIndex = new int[100];
		int currentIndex = 0;
		
		if (commandArgument != null) {
			for (int i = 0; i < commandArgument.length(); i++) {
				if (commandArgument.charAt(i) == ':') {
					delimiterIndex[currentIndex] = i;
					currentIndex++;
				}
			}
		}

		setEmpty(false);
		setEvent(isEvent);
		setFromError(false);
		setEndError(false);
		setPriorityError(false);
		setRecurring(false);
		setRecurringError(false);
		setBoundError(false);
		setTokenizeError(false);
		setExclusionError(false);
		setExclusionDateInvalid(false);
		setExclusionDateSeqError(false);

		extractDataFields(commandArgument);
	}

	private void extractDataFields(String commandArgument) {

		// Extract title
		if (commandArgument == null) {
			setEmpty(true);
		} else if (delimiterIndex[0] != 0) {
			this.title = commandArgument.substring(0, delimiterIndex[0] - 1).trim();
		} else {
			this.title = commandArgument.trim();
		}

		if (this.title == null || this.title.equals("")) {
			setEmpty(true);
		}

		// Extract other data
		for (int i = 0; i < delimiterIndex.length; i++) {
			if (delimiterIndex[i] != 0) {
				switch (commandArgument.charAt(delimiterIndex[i] - 1)) {
				case (TAG_FROM):
					if ((i + 1) < 6 && delimiterIndex[i + 1] != 0) {
						this.from = commandArgument.substring(delimiterIndex[i] + 1, delimiterIndex[i + 1] - 1).trim();
					} else {
						this.from = commandArgument.substring(delimiterIndex[i] + 1).trim();
					}
					break;
				case (TAG_END):
					if ((i + 1) < 6 && delimiterIndex[i + 1] != 0) {
						this.end = commandArgument.substring(delimiterIndex[i] + 1, delimiterIndex[i + 1] - 1).trim();
					} else {
						this.end = commandArgument.substring(delimiterIndex[i] + 1).trim();
					}
					break;
				case (TAG_RECURRING):
					if ((i + 1) < 6 && delimiterIndex[i + 1] != 0) {
						this.recurring = commandArgument.substring(delimiterIndex[i] + 1, delimiterIndex[i + 1] - 1)
								.trim();
					} else {
						this.recurring = commandArgument.substring(delimiterIndex[i] + 1).trim();
					}
					break;
				case (TAG_LABEL):
					if ((i + 1) < 6 && delimiterIndex[i + 1] != 0) {
						this.label = commandArgument.substring(delimiterIndex[i] + 1, delimiterIndex[i + 1] - 1).trim();
					} else {
						this.label = commandArgument.substring(delimiterIndex[i] + 1).trim();
					}
					break;
				case (TAG_PRIORITY):
					if ((i + 1) < 6 && delimiterIndex[i + 1] != 0) {
						this.priority = commandArgument.substring(delimiterIndex[i] + 1, delimiterIndex[i + 1] - 1)
								.trim();
					} else {
						this.priority = commandArgument.substring(delimiterIndex[i] + 1).trim();
					}
					break;
				case (TAG_EXCEPTION):
					setHasExclusion(true);
					if ((i + 1) < 6 && delimiterIndex[i + 1] != 0) {
						this.exclusion = commandArgument.substring(delimiterIndex[i] + 1, delimiterIndex[i + 1] - 1)
								.trim();
					} else {
						this.exclusion = commandArgument.substring(delimiterIndex[i] + 1).trim();
					}
					break;
				}
			}
		}

	}

	private Date parseFromDate() {
		List<Date> startDate;
		if (this.from != null) {
			startDate = timeParser.parse(this.from);

			if (startDate.size() == 0) {
				setFromError(true);
				return null;
			} else {
				return startDate.get(0);
			}

		} else {
			return null;
		}

	}

	private Date parseEndDate(String end) {
		List<Date> endDate;
		if (end != null) {
			endDate = timeParser.parse(end);

			if (endDate.isEmpty()) {
				setEndError(true);
				return null;
			} else {
				return endDate.get(0);
			}

		} else {
			return null;
		}

	}

	private Date parseBoundDate(String date) {
		List<Date> boundDate;
		if (date != null) {
			boundDate = timeParser.parse(date);

			if (boundDate.isEmpty()) {
				setBoundError(true);
				return null;
			} else {
				return boundDate.get(0);
			}

		} else {
			return null;
		}

	}

	private Date parseXDate(String date) {
		List<Date> xDate;
		if (date != null) {
			xDate = timeParser.parse(date);

			if (xDate.isEmpty()) {
				setExclusionDateInvalid(true);
				return null;
			} else {
				return xDate.get(0);
			}

		} else {
			return null;
		}
	}

	private String parsePriority() {

		if (this.priority != null) {
			if (this.priority.equalsIgnoreCase("h") || this.priority.equalsIgnoreCase("high")) {
				return "High";
			} else if (this.priority.equalsIgnoreCase("m") || this.priority.equalsIgnoreCase("med")
					|| this.priority.equalsIgnoreCase("med")) {
				return "Medium";
			} else if (this.priority.equalsIgnoreCase("l") || this.priority.equalsIgnoreCase("low")) {
				return "Low";
			} else {
				setPriorityError(true);
				return null;
			}
		} else {
			return null;
		}

	}

	private String[] tokenize(String input, String delimiter) {
		Scanner s = new Scanner(input).useDelimiter(delimiter);
		String[] tokens = new String[2];
		int i = 0;
		while (s.hasNext()) {
			tokens[i] = s.next();
			i++;
		}

		s.close();

		if (i == 2) {
			return tokens;
		} else {

			if (delimiter.equalsIgnoreCase("\\s*until\\s*")) {
				setTokenizeError(true);
			} else {
				setExclusionError(true);
			}

			return null;
		}

	}

	private ArrayList<ArrayList<Date>> parseRecurringDates(Date startDate, Date endDate) {

		if (this.recurring == null) {

			setRecurring(false);
			return null;

		} else {

			setRecurring(true);
			String[] reucrringTokens = tokenize(this.recurring, "\\s*until\\s*");

			if (!isRecurringError() && !isBoundError() && !isTokenizeError()) {
				String interval = reucrringTokens[0];
				Date boundDate = parseBoundDate(reucrringTokens[1]);

				ArrayList<Date> recurringStartDates = new ArrayList<Date>();
				ArrayList<Date> recurringEndDates = new ArrayList<Date>();

				if (startDate != null && boundDate != null) {
					
					recurringStartDates.add(startDate);
					
					Date nextStartDate;
					Calendar calStart = Calendar.getInstance();
					calStart.setTime(startDate);

					for (int i = 0; i < 365; i++) {

						if (interval.equalsIgnoreCase("daily")) {
							calStart.add(Calendar.DAY_OF_MONTH, 1);
						} else if (interval.equalsIgnoreCase("weekly")) {
							calStart.add(Calendar.DAY_OF_MONTH, 7);
						} else if (interval.equalsIgnoreCase("biweekly") || interval.equalsIgnoreCase("fortnightly")) {
							calStart.add(Calendar.DAY_OF_MONTH, 14);
						} else if (interval.equalsIgnoreCase("monthly")) {
							calStart.add(Calendar.MONTH, 1);
						} else if (interval.equalsIgnoreCase("bimonthly")) {
							calStart.add(Calendar.MONTH, 2);
						} else if (interval.equalsIgnoreCase("annually") || interval.equalsIgnoreCase("yearly")) {
							calStart.add(Calendar.YEAR, 1);
						} else {
							setRecurringError(true);
						}

						nextStartDate = calStart.getTime();

						calStart.setTime(nextStartDate);

						if (nextStartDate.before(boundDate)) {
							recurringStartDates.add(nextStartDate);
						} else {
							break;
						}

					}
				}

				if (endDate != null && boundDate != null) {

					recurringEndDates.add(endDate);
					
					Date nextEndDate;
					Calendar calEnd = Calendar.getInstance();
					calEnd.setTime(endDate);

					for (int i = 0; i < recurringStartDates.size(); i++) {

						if (interval.equalsIgnoreCase("daily")) {
							calEnd.add(Calendar.DAY_OF_MONTH, 1);
						} else if (interval.equalsIgnoreCase("weekly")) {
							calEnd.add(Calendar.DAY_OF_MONTH, 7);
						} else if (interval.equalsIgnoreCase("biweekly") || interval.equalsIgnoreCase("fortnightly")) {
							calEnd.add(Calendar.DAY_OF_MONTH, 14);
						} else if (interval.equalsIgnoreCase("monthly")) {
							calEnd.add(Calendar.MONTH, 1);
						} else if (interval.equalsIgnoreCase("bimonthly")) {
							calEnd.add(Calendar.MONTH, 2);
						} else if (interval.equalsIgnoreCase("annually") || interval.equalsIgnoreCase("yearly")) {
							calEnd.add(Calendar.YEAR, 1);
						} else {
							setRecurringError(true);
						}

						nextEndDate = calEnd.getTime();

						calEnd.setTime(nextEndDate);
						recurringEndDates.add(nextEndDate);

					}
				}

				if (hasExclusion() && endDate != null && startDate != null && boundDate != null) {

					String[] exclusionTokens = tokenize(this.exclusion, "\\s*to\\s*");

					if (!isExclusionError() && !isExclusionDateInvalid()) {

						Date xStartDate = parseXDate(exclusionTokens[0]);
						Date xEndDate = parseXDate(exclusionTokens[1]);

						if (xStartDate == null || xEndDate == null) {
							setExclusionDateInvalid(true);

						} else if (xStartDate.after(xEndDate)) {
							setExclusionDateSeqError(true);

						} else {

							for (int i = 0; i < recurringStartDates.size(); i++) {

								startDate = recurringStartDates.get(i);
								endDate = recurringEndDates.get(i);

								System.out.println(startDate + ", " + endDate + ", " + xStartDate + ", " + xEndDate);

								if (xStartDate.before(endDate) && xEndDate.after(startDate)) {
									recurringStartDates.remove(i);
									recurringEndDates.remove(i);
									i--;
								}

							}
						}

					}

				}

				ArrayList<ArrayList<Date>> recurringDates = new ArrayList<ArrayList<Date>>();
				recurringDates.add(recurringStartDates);
				recurringDates.add(recurringEndDates);

				return recurringDates;
			} else {
				return null;
			}

		}

	}

	public Command parse() {
		String errorMsg;
		Date startDate = parseFromDate();
		Date endDate = parseEndDate(this.end);
		String parsedPriority = parsePriority();
		ArrayList<ArrayList<Date>> recurringDate = parseRecurringDates(startDate, endDate);

		if (isEmpty()) {
			errorMsg = MESSAGE_EMPTY_ERROR;
			InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
			return invalidCommand;

		} else if (startDate != null && endDate != null && endDate.before(startDate)) {
			errorMsg = MESSAGE_END_BEFORE_FROM;
			InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
			return invalidCommand;

		} else if (isFromError()) {
			errorMsg = String.format(MESSAGE_DATE_ERROR, this.from);
			InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
			return invalidCommand;

		} else if (isEndError()) {
			errorMsg = String.format(MESSAGE_DATE_ERROR, this.end);
			InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
			return invalidCommand;

		} else if (isPriorityError()) {
			errorMsg = String.format(MESSAGE_PRIORITY_ERROR, this.priority);
			InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
			return invalidCommand;

		} else if (isRecurringError() || isTokenizeError) {
			errorMsg = String.format(MESSAGE_RECURRING_ERROR, this.recurring);
			InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
			return invalidCommand;

		} else if (isBoundError()) {
			errorMsg = String.format(MESSAGE_BOUND_ERROR, this.recurring);
			InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
			return invalidCommand;

		} else if (isExclusionError()) {
			errorMsg = String.format(MESSAGE_EXCLUSION_ERROR, this.exclusion);
			InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
			return invalidCommand;

		} else if (isExclusionDateInvalid()) {
			errorMsg = String.format(MESSAGE_DATE_ERROR, this.exclusion);
			InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
			return invalidCommand;

		} else if (isExclusionDateSeqError()) {
			errorMsg = MESSAGE_END_BEFORE_FROM;
			InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
			return invalidCommand;

		} else if (isEvent()) {

			if (startDate == null || endDate == null) {
				errorMsg = MESSAGE_DATES_SPECIFIED;
				InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
				return invalidCommand;

			} else if (isRecurring()) {
				ArrayList<Date> recurringStartDates = recurringDate.get(0);
				ArrayList<Date> recurringEndDates = recurringDate.get(1);
				ArrayList<AddCommand> addList = new ArrayList<AddCommand>();
				for (int i = 0; i < recurringStartDates.size(); i++) {

					AddCommand toAdd = new AddCommand(POMPOM.LABEL_EVENT, title, null, parsedPriority,
							POMPOM.STATUS_PENDING, label, recurringStartDates.get(i), recurringEndDates.get(i), true);
					addList.add(toAdd);
				}

				AddRecurringCommand addRecurring = new AddRecurringCommand(addList);
				return addRecurring;

			} else if (!isRecurring()) {
				AddCommand add = new AddCommand(POMPOM.LABEL_EVENT, title, null, parsedPriority, POMPOM.STATUS_PENDING,
						label, startDate, endDate, true);
				return add;

			}

		} else if (!isEvent()) {
			if (isRecurring()) {
				if (startDate == null) {
					errorMsg = MESSAGE_START_DATE_SPECIFIED;
					InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
					return invalidCommand;

				} else {
					ArrayList<Date> recurringStartDates = recurringDate.get(0);
					ArrayList<Date> recurringEndDates = recurringDate.get(1);
					ArrayList<AddCommand> addList = new ArrayList<AddCommand>();
					for (int i = 0; i < recurringStartDates.size(); i++) {
						if (endDate == null) {
							AddCommand toAdd = new AddCommand(POMPOM.LABEL_TASK, title, null, parsedPriority,
									POMPOM.STATUS_PENDING, label, recurringStartDates.get(i), null, true);
							addList.add(toAdd);
						} else {
							AddCommand toAdd = new AddCommand(POMPOM.LABEL_TASK, title, null, parsedPriority,
									POMPOM.STATUS_PENDING, label, recurringStartDates.get(i), recurringEndDates.get(i),
									true);
							addList.add(toAdd);
						}
					}

					AddRecurringCommand addRecurring = new AddRecurringCommand(addList);
					return addRecurring;
				}

			} else {
				AddCommand add = new AddCommand(POMPOM.LABEL_TASK, title, null, parsedPriority, POMPOM.STATUS_PENDING,
						label, startDate, endDate);
				return add;
			}

		}

		errorMsg = MESSAGE_UNKNOWN;
		InvalidCommand invalidCommand = new InvalidCommand(errorMsg);
		return invalidCommand;

	}

	private boolean isEmpty() {
		return isEmpty;
	}

	private void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	private boolean isEvent() {
		return isEvent;
	}

	private void setEvent(boolean isEvent) {
		this.isEvent = isEvent;
	}

	private boolean isFromError() {
		return isFromError;
	}

	private void setFromError(boolean isFromError) {
		this.isFromError = isFromError;
	}

	private boolean isEndError() {
		return isEndError;
	}

	private void setEndError(boolean isEndError) {
		this.isEndError = isEndError;
	}

	private boolean isPriorityError() {
		return isPriorityError;
	}

	private void setPriorityError(boolean isPriorityError) {
		this.isPriorityError = isPriorityError;
	}

	private boolean isRecurring() {
		return isRecurring;
	}

	private void setRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	private boolean isRecurringError() {
		return isRecurringError;
	}

	private void setRecurringError(boolean isRecurringError) {
		this.isRecurringError = isRecurringError;
	}

	private boolean isBoundError() {
		return isBoundError;
	}

	private void setBoundError(boolean isBoundError) {
		this.isBoundError = isBoundError;
	}

	private boolean isTokenizeError() {
		return isTokenizeError;
	}

	private void setTokenizeError(boolean isTokenizeError) {
		this.isTokenizeError = isTokenizeError;
	}

	private boolean isExclusionError() {
		return isExclusionError;
	}

	private void setExclusionError(boolean isExceptionError) {
		this.isExclusionError = isExceptionError;
	}

	private boolean isExclusionDateInvalid() {
		return isExclusionDateInvalid;
	}

	private void setExclusionDateInvalid(boolean isExclusionDateInvalid) {
		this.isExclusionDateInvalid = isExclusionDateInvalid;
	}

	private boolean hasExclusion() {
		return hasExclusion;
	}

	private void setHasExclusion(boolean hasException) {
		this.hasExclusion = hasException;
	}

	private boolean isExclusionDateSeqError() {
		return isExclusionDateSeqError;
	}

	private void setExclusionDateSeqError(boolean isExclusionDateSeqError) {
		this.isExclusionDateSeqError = isExclusionDateSeqError;
	}
}