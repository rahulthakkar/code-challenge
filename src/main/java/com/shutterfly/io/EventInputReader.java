package com.shutterfly.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.shutterfly.entity.Data;
import com.shutterfly.entity.EventEntity;


public class EventInputReader {
	
	private static final Logger LOGGER = Logger.getLogger(EventInputReader.class.getName());

	public Data populateEvents(String path) throws FileNotFoundException {
		LOGGER.log(Level.INFO, "Starting the data ingestion");
		
		Data data = new Data();
		File inputFile = new File(path);
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(inputFile);
			JsonReader reader = Json.createReader(inputStream);
			JsonArray jsonArray = reader.readArray();
			
			for(JsonValue value : jsonArray) {
				JsonObject obj = (JsonObject) value;
				EventEntity event = EventEntityFactory.getEvent(obj);
				if(event!=null) {
					event.ingest(data);
				}
			}
			
			LOGGER.log(Level.INFO, "Finished ingesting the data");
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			throw e;
		}
		
		return data;
	}
}
