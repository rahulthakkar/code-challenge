package com.shutterfly.entity;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.shutterfly.io.EventInputReader;

public class DataTest {

	private Data data;
	@Before
	public void setUp() throws Exception {
		String inputPath = "src\\test\\resources\\input.txt";
		EventInputReader evReader = new EventInputReader();
		data = evReader.populateEvents(inputPath);
	}

	@Test
	public void testUpdateTimestamps() {
		DateTime oldDateCollectionStart = data.getDataCollectionStart();
		DateTime oldDateCollectionEnd = data.getDataCollectionEnd();
		
		DateTime newDateCollectionStart = oldDateCollectionStart.minusHours(1);
		DateTime newDateCollectionEnd = oldDateCollectionEnd.plusHours(1);
		
		data.updateTimestamps(newDateCollectionStart);
		data.updateTimestamps(newDateCollectionEnd);
		assertEquals(newDateCollectionStart, data.getDataCollectionStart());
		assertEquals(newDateCollectionEnd, data.getDataCollectionEnd());
		
	}

}
