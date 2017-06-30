package com.shutterfly.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.shutterfly.entity.Data;


public class EventInputReader {
	
	public Data populateEvents(String path) {
		Data data = new Data();
		
		File inputFile = new File(path);
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(inputFile);
			JsonReader reader = Json.createReader(inputStream);
			JsonArray jsonArray = reader.readArray();
			
			for(JsonValue value : jsonArray) {
				JsonObject obj = (JsonObject) value;
				data.ingest(obj);
			}
			
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return data;
	}
}
