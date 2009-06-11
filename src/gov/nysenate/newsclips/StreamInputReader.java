package gov.nysenate.newsclips;

import java.io.*;

public class StreamInputReader extends Object implements Runnable {

	private Process process;
	
	public StreamInputReader (Process process)
	{
		this.process = process;
		
		Thread thread = new Thread (this);
		thread.start();
	}
	
	public void run ()
	{
		InputStream is = process.getInputStream();
		  int i = -1;
	      try{ 
	    	  while ((i = is.read()) !=-1){
	    		  
	    		//  System.out.print((char)i);
	    	  } 
	      } catch(IOException ioe){}
	}
}
