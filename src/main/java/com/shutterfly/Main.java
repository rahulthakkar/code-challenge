package com.shutterfly;

import java.io.IOException;
import java.util.ArrayDeque;

import com.shutterfly.entity.Customer;
import com.shutterfly.entity.Data;
import com.shutterfly.io.EventInputReader;
import com.shutterfly.io.FileWriter;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		// Relative paths for input file and output directory
		String inputPath = "input\\input.txt";
		String outputPath = "output";
		
		int x=10;
		
		EventInputReader evReader = new EventInputReader();
		Data data = evReader.populateEvents(inputPath);
		
		// First x LTV in a stack
		ArrayDeque<Customer> topXCust =  data.topXSimpleLTVCust(x);
		//System.out.println(topXCust);
		FileWriter fileWriter = new FileWriter();
		fileWriter.write(topXCust, outputPath);
	}

}
