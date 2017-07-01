package com.shutterfly;

import java.util.ArrayDeque;

import com.shutterfly.entity.Customer;
import com.shutterfly.entity.Data;
import com.shutterfly.io.EventInputReader;
import com.shutterfly.io.FileWriter;

public class Main {
	
	public static void main(String[] args) {
		String inputPath = "input\\input.txt";
		String outputPath = "output";
		int x=10;
		
		EventInputReader evReader = new EventInputReader();
		Data data = evReader.populateEvents(inputPath);
		
		// First x in a stack
		ArrayDeque<Customer> topXCust =  data.topXSimpleLTVCust(x);
		FileWriter fileWriter = new FileWriter();
		fileWriter.write(topXCust, outputPath);
	}

}
