package utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ItemAdapter extends TypeAdapter<UserTaskList> {

	@Override
	public UserTaskList read(JsonReader in) throws IOException {
		final UserTaskList userTaskList = new UserTaskList();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"EEE MMM d HH:mm:ss z yyyy");
		Date startDate = null;
		Date endDate = null;
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
			case "Username":
				userTaskList.setUserName(in.nextString());
				break;
			case "TaskList":
				in.beginArray();
				ArrayList<Item> taskArrayList = new ArrayList<Item>();
				while (in.hasNext()) {
					in.beginObject();
					final Item task = new Item();
					while (in.hasNext()) {
						switch (in.nextName()) {
						case "Id":
							task.setId(in.nextInt());
							break;
						case "Title":
							task.setTitle(in.nextString());
							break;
						case "Priority":
							task.setPriority(in.nextString());
							break;
						case "Description":
							task.setDescription(in.nextString());
							break;
						case "Label":
							task.setLabel(in.nextString());
							break;
						case "Status":
							task.setStatus(in.nextString());
							break;
						case "StartDate":
							try {
								task.setStartDate(formatter.parse(in
										.nextString()));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							break;
						case "EndDate":
							try {
								task.setEndDate(formatter.parse(in.nextString()));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							break;
						}
					}
					taskArrayList.add(task);
					in.endObject();
				}
				userTaskList.setTaskArray(taskArrayList
						.toArray(new Item[taskArrayList.size()]));
				in.endArray();
				break;
			}
		}
		in.endObject();

		return userTaskList;
	}

	@Override
	public void write(JsonWriter out, UserTaskList userTaskList)
			throws IOException {
		out.beginObject();

		out.name("Username").value(userTaskList.getUserName());
		out.name("TaskList").beginArray();

		if (userTaskList.getTaskArrayList() != null) {
			ArrayList<Item> taskList = userTaskList.getTaskArrayList();			

		for (final Item task : taskList) {
			out.beginObject();
			out.name("Id").value(task.getId());
			out.name("Title").value(task.getTitle());
			out.name("Priority").value(task.getPriority());
			out.name("Description").value(task.getDescription());
			out.name("Label").value(task.getLabel());
			out.name("Status").value(task.getStatus());
			out.name("StartDate").value(task.getStartDate().toString());
			out.name("EndDate").value(task.getEndDate().toString());
			out.endObject();

		}
		out.endArray();
		out.endObject();

	}
}
}
