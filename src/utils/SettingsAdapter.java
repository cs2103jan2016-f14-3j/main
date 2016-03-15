package utils;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * @author A0121628L
 *
 */
public class SettingsAdapter extends TypeAdapter<Settings>{

	@Override
	public Settings read(JsonReader in) throws IOException {
		Settings settings = new Settings();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
			case "StoragePath":
				settings.setStoragePath(in.nextString());
			}
		}
		in.close();
		return settings;
	}

	@Override
	public void write(JsonWriter out, Settings settings) throws IOException {
		out.beginObject();
		out.name("StoragePath").value(settings.getStoragePath());
		out.close();
	}

}
