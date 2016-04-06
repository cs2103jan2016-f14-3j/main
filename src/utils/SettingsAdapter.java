package utils;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * @@author A0121628L This method helps write Settings into json string and read
 *          Json string to create Settings object
 */
public class SettingsAdapter extends TypeAdapter<Settings> {

	@Override
	public Settings read(JsonReader in) throws IOException {
		Settings settings = new Settings();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
			case "storagePath":
				settings.setStoragePath(in.nextString());
				break;
			case "backgroundColour":
				settings.setBackgroundColour(in.nextString());
				break;
			case "tabColour":
				settings.setTabColour(in.nextString());
				break;
			case "buttonColour":
				settings.setButtonColour(in.nextString());
				break;
			case "oddCellColour":
				settings.setOddCellColour(in.nextString());
				break;
			case "evenCellColour":
				settings.setEvenCellColour(in.nextString());
				break;
			}
		}
		in.endObject();
		return settings;
	}

	@Override
	public void write(JsonWriter out, Settings settings) throws IOException {
		out.beginObject();

		out.name("storagePath").value(settings.getStoragePath());
		out.name("backgroundColour").value(settings.getBackgroundColour());
		out.name("tabColour").value(settings.getTabColour());
		out.name("buttonColour").value(settings.getButtonColour());
		out.name("oddCellColour").value(settings.getOddCellColour());
		out.name("evenCellColour").value(settings.getEvenCellColour());

		out.endObject();
		out.close();
	}

}
