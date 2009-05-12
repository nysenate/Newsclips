package org.nysenatecio.clips;

import java.io.*;

public class StreamErrorReader extends Object implements Runnable {

	private Process process;
	private boolean showDisplay = false;
	
	public StreamErrorReader (Process process)
	{
		this.process = process;
		
		Thread thread = new Thread (this);
		thread.start();
	}
	
	public void run ()
	{
		InputStream is = process.getErrorStream();
		  int i = -1;
	      try{ 
	    	  while ((i = is.read()) !=-1){
	    	//	 if (showDisplay)System.out.print((char)i);
	    	  } 
	      } catch(IOException ioe){}
	}

	public boolean isShowDisplay() {
		return showDisplay;
	}

	public void setShowDisplay(boolean showDisplay) {
		this.showDisplay = showDisplay;
	}
}
