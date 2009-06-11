package gov.nysenate.newsclips;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.InputStream;
import java.security.MessageDigest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/***
 * 
 * The DayPIClient is a very basic class in Java that helps
 * you make a request to the Daylife APIs. 
 * 
 * You will need a daylife accesskey, sharedsecret and the 
 * daypi server name to initialize a DayPIClient object.
 * 
 *  You can then call the functiona name "call" with the 
 *  daypi method name and the list of input parameter. The call
 *  function returns the daypi response as a XML DOM object.
 *  
 *  @author Vineet Gupta (vineet at daylife dot com)
 */
public class DayPIClient {
	
	/*
	 * The auth_keys array contains the name of input params
	 * that the daypi expects to use  
	 */
	private final String[] auth_keys = {
			"topic_id", 
			"article_id", 
			"story_id", 
			"image_id", 
			"quote_id", 
			"set_id", 
			"query", 
			"name"
			};
	
	private final String _sharedsecret;
	private final String _accesskey;
	private final String _server;
	private final String _version;
	  
	public DayPIClient(String accesskey, String sharedsecret, String server, String version) {
		_accesskey = accesskey;
		_sharedsecret = sharedsecret;
		_server = server;
		_version = version;
	}
	
	public Document call(String method_name, Map params) {
		
		//Create Signature based on the input params
		String signature = this.create_signature(params);
		
		//Add accesskey and signature to the list of params
		params.put("accesskey", _accesskey);
		params.put("signature", signature);
		
		//Ensure sharedsecret is not part of the params 
		//that gets sent to the server
		if (params.containsKey("sharedsecret")) {
			params.remove("sharedsecret");
		}
		
		//Form the URL that needs to be called
		String url = create_url(method_name, params);
		
		//Make the HTTP GET call to the daypi server
		URL apiURL = null;
		try {
			apiURL = new URL(url);
		}
		catch (MalformedURLException e) {
			System.out.println("MalformedURLException: " + e.getMessage());
		    System.exit(1);
		}
		
		try {
			HttpURLConnection conn = (HttpURLConnection) apiURL.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			InputStream inputStream = conn.getInputStream();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(inputStream);
			//doc.normalizeDocument();
			doc.normalize();
			return doc;
		}
		catch(Exception e) {
			System.out.println("Exception: " + e.getMessage());
			System.exit(1);
		}
		return null;
	}
	
	private String create_url(String method_name, Map params) {
		String url = "";
		url = "http://" 
			+ this._server
			+ "/" + "xmlrest"
			+ "/" + "publicapi"
			+ "/" + this._version
			+ "/" + method_name
			+ "?" + this.params_to_string(params);
	//	System.out.println(url);
		return url;
	}
	
	private String params_to_string(Map params) {
		String qs = "";
		Iterator it = params.keySet().iterator();
		while (it.hasNext()) {
			String key = (String)it.next();
			Object value = (Object) params.get(key);
			if (value instanceof List) {
				List newval = (List) value;
				Collections.sort(newval);
				for (int i=0; i< newval.size(); i++){
					qs += key + "=" + this.url_encode((String)newval.get(i)) + "&";
				}
				
			} else {
				qs += key + "=" + this.url_encode((String) value) + "&";
			}
		}
		return qs;
	}
	
	private String url_encode(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			System.out.print("Unsuccessful attempt to encode " + value +" into UTF8");
			System.out.println("Exception: " + e.getMessage());
		    System.exit(1);
		}
		return null;
	}

	private String create_signature(Map params) {
		String strValue = "";
		for (int i = 0; i < auth_keys.length; i++) {
			if (params.containsKey(auth_keys[i])) {
				Object value = (Object)params.get(auth_keys[i]);
				if (value instanceof List) {
					List newval = (List) value;
					Collections.sort(newval);
					for (int j=0; j< newval.size(); j++){
						strValue += (String)newval.get(j);						
					}
					
				} else {
					strValue = (String) value;
				}
			}
		}
		return this.getMD5Hash(strValue);
	}
	
	private String getMD5Hash(String query) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(_accesskey);
		buffer.append(_sharedsecret);
		buffer.append(query);
		try {
		  MessageDigest md = MessageDigest.getInstance("MD5");
		  StringBuffer result = new StringBuffer();
		  byte bbytes[] = md.digest(buffer.toString().getBytes());
		  for (int i=0; i < bbytes.length; i++) {
			  byte b = bbytes[i];
			  result.append(Integer.toHexString((b & 0xf0) >>> 4));
			  result.append(Integer.toHexString(b & 0x0f));
		  }
		  return result.toString();
		}
		catch (java.security.NoSuchAlgorithmException e) {
			System.err.println("MD5 does not appear to be supported" + e);
			System.exit(1);
		}
		return "";
	}
}
