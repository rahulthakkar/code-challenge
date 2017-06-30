package com.shutterfly;

import java.util.Map;

import com.shutterfly.entity.Data;
import com.shutterfly.io.EventInputReader;
import com.shutterfly.io.MapFileWriter;

public class Main {
	
	public static void main(String[] args) {
		String inputPath = "input\\input.txt";
		String outputPath = "output";
		
		
		EventInputReader evReader = new EventInputReader();
		Data data = evReader.populateEvents(inputPath);
		Map<String, Double> topXCust =  data.topXSimpleLTVCust();
		MapFileWriter mapWriter = new MapFileWriter();
		mapWriter.write(topXCust, outputPath);
	}

}
