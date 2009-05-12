/*
 * This code is currently released under the Mozilla Public License.
 * http://www.mozilla.org/MPL/
 */
package org.nysenatecio.clips;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

public class WebScrapeUtils {

	private final static String HTTP_KEY = "http";
	private final static String MMS_KEY = "mms://";
	private final static String FORWARD_SLASH = "/";
	private final static String COLON = ":";
	private final static String PROTOCOL_DELIM = "://";
	
	private final static String[] COMMON_MEDIA_EXTS = 
	{".mov",".mpg",".jpg",".mp3",".wmv",".avi",".mp4","png",".gif"};
	
    public static Enumeration getMediaFromPage (String pageUrl, String strPageBody, String key, int max) throws IOException
    {
    		return getMediaFromStream (pageUrl, new StringReader(strPageBody), key, max);
    }

	public static String processString (String str)
	{
	    str = str.replace('"','_');        
	    str = str.replace('\'','_');     
	    str = str.replace('\\','_');
	    str = str.replace('/','_');
	    str = str.replace('|','_');
	    str = str.replace('<','_');
	    str = str.replace('>','_');        
	    str = str.replace('?','_');        
	    str = str.replace(':','_');
	    str = str.replace('*','_');
	    return str;
	}
    
	
	public static Enumeration getMediaFromStream (String pageUrl, Reader reader, String key, int maxResults) throws IOException
	{
	
		HtmlStreamTokenizer st = new HtmlStreamTokenizer (reader);
		
	    Hashtable results = new Hashtable ();
	
	    
	    String fileUrl = null;
	    
	    int ttype = -1;
	    String sval = null;
	    boolean isMedia = false;
	    
	    StringBuffer bUrl = new StringBuffer();
	    URL siteUrl = null;
	    
	    while ((ttype = st.nextToken()) != StreamTokenizer.TT_EOF)
        {   
        	
        		if (ttype == StreamTokenizer.TT_WORD && (sval = st.sval) != null)
        		{	
        			
        			
        			if (key != null && (sval.indexOf(key) == -1))
        				continue;
        			
	              
        			fileUrl = sval;
        			
		        isMedia = checkFileExtensionForMedia (fileUrl);
		        
		       
		        if (isMedia)
		        {
		           // try
		            //{
		            	
		            
		                if (!fileUrl.toLowerCase().startsWith(HTTP_KEY))
		                {
		                		//debug ("fixing url: " + fileUrl);
		                		bUrl.delete(0,bUrl.length());
		                		
		                        siteUrl = new URL (pageUrl);
		                        
		                        //if (siteUrl.getPort() != -1)
		                        	//port = ":" + siteUrl.getPort ();
		                        bUrl.append (siteUrl.getProtocol());
	                        	bUrl.append (PROTOCOL_DELIM);
	                        	bUrl.append (siteUrl.getHost());
	                        
	                        	if (siteUrl.getPort() != -1)
	                        	{
	                        		bUrl.append (COLON);
	                        		bUrl.append (siteUrl.getPort());
	                        	}
	                        	
		                        if (fileUrl.startsWith(FORWARD_SLASH))
		                        {
		                        	
		                        	
		                        	bUrl.append (fileUrl);
		                        	
		                            //fileUrl = siteUrl.getProtocol() + "://" + siteUrl.getHost() + port + fileUrl;
		                        }
		                        else
		                        {
		                        	String constructPath = siteUrl.getPath().substring (0,siteUrl.getPath().lastIndexOf('/')+1); 
		                            bUrl.append (constructPath);
		                            bUrl.append (fileUrl);                          		                            
		                        }
		                        
		                        fileUrl = bUrl.toString();
		
		                        results.put (fileUrl, fileUrl);
		                }
		                else
		                {
		                	  results.put (fileUrl, fileUrl);
		                }
		        
		              
		                
		                if (maxResults != -1 && results.size() >= maxResults)
		                		return results.elements();
		           
		                /*
		            }
		            catch (MalformedURLException mfe)
		            {
		            		debug ("error building media result URL: " + mfe.toString());
		            }*/
		        }
        		}
	        
	    }
	
	    return results.elements();
	}

    public static boolean checkFileExtensionForMedia (String fileUrl)
    {
    		String lCaseFileUrl = fileUrl.toLowerCase();
    		
    		for (int i = 0; i < COMMON_MEDIA_EXTS.length; i++)
    		{
    			if (lCaseFileUrl.endsWith(COMMON_MEDIA_EXTS[i]))
    				return true;
    		}
    		
    		return true;
    }
    
    public static String convertHtmlToText (String str, int maxLength) throws IOException
	{
    		
		return convertHtmlToText ( new java.io.ByteArrayInputStream(str.getBytes()),maxLength);
	}
	
	public static String convertHtmlToText (InputStream is, int maxLength) throws IOException
    {
        Reader in = new InputStreamReader (is);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int c;
        boolean intag=false;
        boolean inchar=false;
        boolean inbracket=false;
        c = in.read();
        
        while(c != -1) {
            if(c == '<') intag = true;
            if(c == '&') inchar = true;
            if(c == '{') inbracket = true;
            
            if(! intag && ! inchar && !inbracket) 
            	{
            		if (c != '\n')
            			out.write(c);    
            		
            		
            	}
        
            if(intag && c == '>')intag = false;
            if(inchar && c == ';') inchar = false;
            if(inbracket && c == '}') inbracket = false;
            
            if (maxLength != -1 && out.size() > maxLength)
            	break;
            else
            	c = in.read();
        }
        
        out.close();
        in.close();
        
        return out.toString();
    }
    
    public static void debug (String msg)
    {
        System.out.println ("WebScrapeUtils: " + msg);
    }
    
    public static byte[] loadBytesFromStream( java.io.InputStream in, int chunkSize )
    throws java.io.IOException {
    	
  if( chunkSize < 1 )
    chunkSize = 1024;

  int count;
  java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
  byte[] b = new byte[chunkSize];
  try {
    while( ( count = in.read( b, 0, chunkSize ) ) > 0 ) {
      bo.write( b, 0, count );
    }
    byte[] thebytes = bo.toByteArray();
    return thebytes;
  }
  finally {
    bo.close();
  }
}
}
