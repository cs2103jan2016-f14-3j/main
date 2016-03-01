package utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class TaskAdapter extends TypeAdapter<UserTaskList>{

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
	        ArrayList<Task> taskArrayList = new ArrayList<Task>();
	        while (in.hasNext()) {
	          in.beginObject();
	          final Task task = new Task();
	          while (in.hasNext()) {
	            switch (in.nextName()) {
	            case "Title":
	              task.setTitle(in.nextString());
	              break;
	            case "Priority":
	              task.setPriority(in.nextString());
	              break;	            
	          case "Decription":
	              task.setDecription(in.nextString());
	              break;
	          case "StartDate":
	        	  try
                  {
                    task.setStartdate(formatter.parse(in.nextString()));
                  }
                  catch (ParseException e)
                  {
                    e.printStackTrace();
                  }
	              break;
	          case "EndDate":
	        	  try
                  {
                    task.setEndDate(formatter.parse(in.nextString()));
                  }
                  catch (ParseException e)
                  {
                    e.printStackTrace();
                  }
	              break;
	            }
	          }
	          taskArrayList.add(task);
	          in.endObject();
	        }
	        userTaskList.setTaskArray(taskArrayList.toArray(new Task[taskArrayList.size()]));
	        in.endArray();
	        break;
	      }
	    }
	    in.endObject();

	    return userTaskList;
	}

	@Override
	public void write(JsonWriter out, UserTaskList userTaskList) throws IOException {
	    out.beginObject();
	    ArrayList<Task> taskList = userTaskList.getTaskArrayList();
	    out.name("Username").value(userTaskList.getUserName());
	    out.name("TaskList").beginArray();
	    for (final Task task : taskList) {
	      out.beginObject();
	      out.name("Title").value(task.getTitle());
	      out.name("Priority").value(task.getPriority());
	      out.name("Decription").value(task.getDecription());
	      out.name("StartDate").value(task.getStartDate().toString());
	      out.name("EndDate").value(task.getEndDate().toString());
	      out.endObject();
	    }
	    out.endArray();
	    out.endObject();
		
	}

}
