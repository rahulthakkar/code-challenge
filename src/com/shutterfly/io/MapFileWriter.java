package com.shutterfly.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

public class MapFileWriter {

	public void write(Map<String, Double> topXCust, String outputDir) {
		File dir = new File(outputDir);
		File outputFile = new File(dir, "output_"+System.currentTimeMillis()+".txt");
		
		try {
			PrintWriter writer = new PrintWriter(outputFile);
			for(Map.Entry<String, Double> entry : topXCust.entrySet()) {
				writer.write(entry.getKey() +"-->"+ entry.getValue());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			//TODO
		}
	}
	
}
