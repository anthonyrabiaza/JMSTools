package org.antsoftware.messaging.wrapper.scenarios;

import java.util.Date;

public class BenchmarkingTools {
	
	private static final int WATERMARK_MAX = 50;
	boolean benchmarking = false;
	int counter = 0;
	Date firstTrigger;
	
	public BenchmarkingTools() {
		String benchmarkingProperty = System.getProperty("benchmark");
		if(benchmarkingProperty!=null) {
			benchmarking = Boolean.valueOf(benchmarkingProperty);
		}
	}
	
	public boolean isEnabled() {
		return benchmarking;
	}
	
	public void newTrigger() throws Exception {
		counter++;
		String screenDisplay = "";
		int displayCounter = counter%50;
		
		if(firstTrigger == null) {
			firstTrigger = new Date();
		}
		
		Date now = new Date();
		long diff = (now.getTime() - firstTrigger.getTime())/1000;
		
		//After 2 minutes idle, reset
		if(diff>120) {
			counter = 0;
			firstTrigger = new Date();
		}
				
		for (int i = 0; i < WATERMARK_MAX; i++) {
			screenDisplay += i<displayCounter?"=":i==displayCounter?">":" ";
		}
		
		long perf = diff==0?0:counter/diff;
		screenDisplay = "\rLoad: [" + screenDisplay + "] " + (counter+1) + " tx, " + perf + " tx/s, last tx at " + now + " ";
		System.out.write(screenDisplay.getBytes());
	}
}
