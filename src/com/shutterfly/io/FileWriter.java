package com.shutterfly.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayDeque;

import com.shutterfly.entity.Customer;

public class FileWriter {

	public void write(ArrayDeque<Customer> topXCust, String outputDir) {
		File dir = new File(outputDir);
		File outputFile = new File(dir, "output_"+System.currentTimeMillis()+".txt");
		
		try {
			PrintWriter writer = new PrintWriter(outputFile);
			for(Customer customer : topXCust) {
				writer.write(customer.getCustID() +"-->"+ customer.getAvgAmountPerWeek());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			//TODO
		}
	}
	
}
