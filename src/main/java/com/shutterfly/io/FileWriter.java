package com.shutterfly.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shutterfly.entity.Customer;
import com.shutterfly.utility.Constants;

public class FileWriter {

	private static final Logger LOGGER = Logger.getLogger(FileWriter.class.getName());

	public void write(ArrayDeque<Customer> topXCust, String outputDir) throws FileNotFoundException {
		
		File dir = new File(outputDir);
		File outputFile = new File(dir, "output_" + System.currentTimeMillis() + ".txt");
		
		LOGGER.log(Level.INFO, "Starting to write to a file");
		
		try {
			PrintWriter writer = new PrintWriter(outputFile);
			for (Customer customer : topXCust) {
				double simpleLTV =  customer.getAvgAmountPerWeek()
									* Constants.AVG_CUST_LIFESPAN_YEARS 
									* Constants.NUM_WEEKS_IN_YEAR;
				
				writer.println(customer.getKey() +", " +simpleLTV+", "+customer.getAvgAmountPerWeek());
				System.out.println(customer.getKey() +", " +simpleLTV+", "+customer.getAvgAmountPerWeek());
			}
			writer.close();
			LOGGER.log(Level.INFO, "Finished writing to the file {0}", outputFile.getName());
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			throw e;
		}
	}

}
