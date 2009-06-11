package gov.nysenate.newsclips;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class ClipsClient {

	
	 /**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * @return the limitSources
	 */
	public boolean isLimitSources() {
		return limitSources;
	}

	/**
	 * @param limitSources the limitSources to set
	 */
	public void setLimitSources(boolean limitSources) {
		this.limitSources = limitSources;
	}



	private static final Logger log = Logger.getLogger(ClipsClient.class.getName());

	
	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * @return the processing
	 */
	public boolean isProcessing() {
		return processing;
	}

	/**
	 * @param processing the processing to set
	 */
	public void setProcessing(boolean processing) {
		this.processing = processing;
	}



	String output = null;
	boolean processing = false;
	
	/**
	 * @return the sourceTypeFilter
	 */
	public int getSourceTypeFilter() {
		return sourceTypeFilter;
	}

	/**
	 * @param sourceTypeFilter the sourceTypeFilter to set
	 */
	public void setSourceTypeFilter(int sourceTypeFilter) {
		this.sourceTypeFilter = sourceTypeFilter;
	}

	/**
	 * @return the sourceRankFilter
	 */
	public int getSourceRankFilter() {
		return sourceRankFilter;
	}

	/**
	 * @param sourceRankFilter the sourceRankFilter to set
	 */
	public void setSourceRankFilter(int sourceRankFilter) {
		this.sourceRankFilter = sourceRankFilter;
	}




	/*
	 * 
	 * other search apis:
	 * 
	 * http://search.yahooapis.com/NewsSearchService/V1/newsSearch?appid=YahooDemo&query=swine+flu&results=25&language=en
	 * 
	 * http://developer.nytimes.com/docs/article_search_api/
	 * 
	 * 
	 */
	public final static SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //2009-03-13 11:03:57
	
	public final static String BASE_QUERY = " AND \"New York\"";
	public final static String BASE_QUERY_SENATE = "(Senate OR Senator OR Sen OR Sen.) AND ";
	
	public final static int SEARCH_DELAY = 150;
	
	public final static int MAX_CONTENT_LENGTH = 1000;
	
	public final static String LYNX_CMD = "/usr/bin/lynx -dump ";
	
	public final static String SOURCE_FILTER_ID = "0gqJ7mBcQE3xN";
	
	String accesskey = null;
	String sharedsecret = null; 
	String server = "freeapi.daylife.com";
	String version = "4.2";
	
	String sort = "relevance";
	
	String max = "25";
	
	int sectionmax = 25;
	int anchorLength = 15;
	
	int sourceTypeFilter = -1;
	int sourceRankFilter = -1;
	boolean limitSources = true;
	
	public final static String FULL_STORIES_DIVIDER = "<hr/><div><h2>Full Stories</h2></div>";
	public final static String EXCERPT_STORIES_DIVIDER = "<hr/><div><h2>Story Excerpts</h2></div>";
	
	public ClipsClient ()
	{
		
		
	}
	
	public ClipsClient (String cmd)
	{
		File file = null;
		
		if (cmd.length() == 0)
		{
			
		}
		
		/*
		if (file == null)
		{
			file = new File("clips" + endTime.getTime() + ".html");
		}
		
		FileOutputStream fos = new FileOutputStream(file);
		
		fos.write(out.toString().getBytes());
		
		fos.flush();
		
		runCommandLine("open " + file.getAbsolutePath());
		*/
		
		if (cmd.toLowerCase().equals("senate"))
		{
			doSenateClips (null, null, false);
		}
		else if (cmd.toLowerCase().equals("topics"))
		{
			String topics = "State Senate";
			doTopicClips (topics, null, null, false);
		}
		else
			doKeywordClips (cmd, null, null, false);
		
		if (file != null)
		{
			
			
		}
		
		System.exit(0);
	}
	
	Date startTime;
	Date endTime;
	boolean fullArticles;
	
	public void doSenateClips (Date startTime, Date endTime, boolean fullArticles)
	{
		this.startTime= startTime;
		this.endTime = endTime;
		this.fullArticles = fullArticles;
		
		processing = true;
		
		output = null;
		
		if (startTime == null)
			{
			startTime = new Date();
			startTime.setHours(0);
			startTime.setMinutes(1);
			startTime.setSeconds(0);
			startTime.setDate(startTime.getDate()-1);
			}

		if (endTime == null)
		{
			endTime = new Date();
			endTime.setHours(endTime.getHours()+5);
		}

		StringBuffer out = new StringBuffer();
		
		
		StringBuffer fullPageText = new StringBuffer();
		
		//initialize the daypi client
		DayPIClient client = new DayPIClient(accesskey, sharedsecret, server, version);
		
		LinkedHashMap<String, Clip> fullpages = new LinkedHashMap<String, Clip>();
		
		LinkedHashMap<String, Integer> resultCount = new LinkedHashMap<String, Integer>();
		
		//ArrayList<Clip> fullresults = new ArrayList<Clip>();
	
		LinkedHashMap<String, Clip> sresults = null;
		String countKey = null;
		
		for (int i = 0; i < SENATOR_TOPIC_IDS.length; i++)
		{

			//create the input params
			Map input = new HashMap();
			String cmd = null;
			
			
				
			if (SENATOR_TOPIC_IDS[i][0].length() > 0)
			{
				
				
				String topicId = SENATOR_TOPIC_IDS[i][1];
				input.put("topic_id", topicId);
				cmd = "topic_getRelatedArticles";
				
			}
			else
			{
				
				input.put("query", BASE_QUERY_SENATE + "\"" + SENATOR_TOPIC_IDS[i][1] + "\"");
				cmd = "search_getRelatedArticles";
				
			}
			
			input.put("limit", max);
			input.put("sort", sort);
			input.put("start_time", DATEFORMAT.format(startTime.getTime()));
			input.put("end_time", DATEFORMAT.format(endTime));
			
			if (limitSources)
				input.put("source_filter_id",SOURCE_FILTER_ID);
			/*
			StringBuffer sourceWhitelist = new StringBuffer();
			sourceWhitelist.append("0a0F0VifAodow");
			
			input.put("source_whitelist",sourceWhitelist.toString());
			*/
			
			//make the API call
			Document doc = client.call(cmd, input);
		
			if (SENATOR_TOPIC_IDS[i][0].length()>0)
			{
				if (sresults!=null)
				{
					resultCount.put(countKey, new Integer(sresults.size()));
				
					if (sresults.size()>0)
					{
						String anchor = URLEncoder.encode(countKey);
						if (anchor.length()>anchorLength)
							anchor = anchor.substring(0,anchorLength);
						out.append("<h2><a name=\"" + anchor + "\">" + countKey + "</a></h2>");
						out.append(renderHTMLHeadlines(sresults.values()));
					}
				}
				
				sresults = new LinkedHashMap<String, Clip>();
				countKey = SENATOR_TOPIC_IDS[i][0];
			}
			
			
			if (SENATOR_TOPIC_IDS[i][1].equals("LABEL"))
			{
				out.append("<h1>" + SENATOR_TOPIC_IDS[i][0] + "</h1>");
				
				continue;
				
			}
			
			buildClips(doc,"article", sresults, (String)input.get("query"), fullArticles);
			
			//sresults = filterResults (sresults);
			
			log.info("topic-id: " + SENATOR_TOPIC_IDS[i][0] + "(" + SENATOR_TOPIC_IDS[i][1] + ")" + " = " + sresults.size());
			
			if (SENATOR_TOPIC_IDS[i][0].length()>0)
			{
				resultCount.put(countKey, new Integer(sresults.size()));
			}
			
			if (sresults.size()> 0)
			{
				
				Iterator<Clip> itclips = sresults.values().iterator();
				Clip clip = null;
				
				while(itclips.hasNext())
				{
					clip = itclips.next();
					fullpages.put(clip.getHeadline(), clip);
				}
				
			}
			
			try { Thread.sleep(SEARCH_DELAY); } catch (Exception e){}
		}
		
		if (fullArticles)
			out.append(FULL_STORIES_DIVIDER);
		else
			out.append(EXCERPT_STORIES_DIVIDER);
		
		String pageText = renderHTMLPages(fullpages.values(), fullArticles);
		fullPageText.append(pageText);
		
		pageText = highlightSenatorNames(fullPageText.toString());
		
		out.append(pageText);
		
		pageText = out.toString();
		
		out = new StringBuffer();
		
	//	out.append("<h1>Senate Names News Clips</h1>");
	//	out.append("Timespan: " + startTime.toLocaleString() + " to " + endTime.toLocaleString());
	//	out.append("<hr/>");
		
		Iterator<Entry<String,Integer>> itResultCount = resultCount.entrySet().iterator();
		
		Entry<String,Integer> entry = null;
		String idx = "";
		out.append("Clips Found: ");
		int clipsCount = 0;
		
		while (itResultCount.hasNext())
		{
			entry = itResultCount.next();
			
			String anchor = URLEncoder.encode(entry.getKey());
			if (anchor.length()>anchorLength)
				anchor = anchor.substring(0,anchorLength);
			
			clipsCount = entry.getValue().intValue();
			
			if (clipsCount > 0)
			{
			out.append("<a href=\"#" + anchor + "\">" + entry.getKey() + "</a>(" + entry.getValue() + ")&nbsp;&nbsp;");
			}
		}
		
		/*
		for (int i = 0; i < SENATOR_TOPIC_IDS.length; i++)
		{
			if (SENATOR_TOPIC_IDS[i][1].equals("LABEL"))
			{
				out.append("<br/><br/><b>" + SENATOR_TOPIC_IDS[i][0] + ":</b> ");
			}
			else if (SENATOR_TOPIC_IDS[i][0].length()>0 )
			{
				out.append("<a href=\"#" + i + "\">" + SENATOR_TOPIC_IDS[i][0] + "</a> ");
			}
		}*/
		
		out.append(pageText);
		
		try
		{
			output = out.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		processing = false;
		
	}
	
	public void doTopicClips (String topics, Date startTime, Date endTime, boolean fullArticles)
	{
		output = null;
		processing = true;
		String line = null;
		
		if (topics == null)
		{
			topics = "";
			
			try {
				File file = new File("../webapps/nysclips/WEB-INF/classes/topics.txt");
				System.out.println("file:" + file.getAbsolutePath());
				BufferedReader reader = new BufferedReader(new FileReader(file));
				while ((line = reader.readLine())!=null)
				{
					topics += line + "\r\n";
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//initialize the daypi client
		DayPIClient client = new DayPIClient(accesskey, sharedsecret, server, version);
		
		LinkedHashMap<String, Clip> fullpages = new LinkedHashMap<String, Clip>();
		
		if (startTime == null)
		{
			startTime = new Date();
			startTime.setHours(0);
			startTime.setMinutes(1);
			startTime.setSeconds(0);
			startTime.setDate(startTime.getDate()-1);
		}

		if (endTime == null)
		{
			endTime = new Date();
			endTime.setHours(endTime.getHours()+5);	
		}

		StringBuffer out = new StringBuffer();
		
		StringBuffer fullPageText = new StringBuffer();
		
		
		StringTokenizer st = new StringTokenizer (topics,"\r\n");
		StringTokenizer stLine = null;
		LinkedHashMap<String,Clip> sresults = null;
		Collection<Clip> sectionClips = null;
		Clip clip = null;
		
		String parentQuery = null;
		String query = null;
		
		String token = null;
		String sectionTitle = null;
		
		int thisDepth = -1;
		int lastDepth = -1;
		
		while (st.hasMoreTokens())
		{
			line = st.nextToken();
			stLine  = new StringTokenizer (line,".");
			thisDepth = stLine.countTokens() ;
			
			if (thisDepth == 3)
			{
				
				if (sresults != null)
				{
					sectionClips = sresults.values();
					
					if (sectionClips.size() > 0)
					{
						
						log.info("***rendering previous section headlines: " + sectionClips.size() + " found");
						//out.append("<h3>" + sectionTitle + "</h3>");
						//render previous main section
						out.append(renderHTMLHeadlines(sectionClips));
						fullpages.putAll(sresults);
					}
					
				}
				
				log.info("***new header line: " + line);
				
				sresults = new LinkedHashMap<String,Clip>();
				
				stLine.nextToken();
				stLine.nextToken();
				sectionTitle = stLine.nextToken();
				out.append("<h3>" + sectionTitle + "</h3>");
				
				
				parentQuery = "";
				query = sectionTitle;
				lastDepth = 3;
				
			}
			
			if (thisDepth == 2)
			{
				
				if (sresults != null)
				{
					sectionClips = sresults.values();	
					
					if (sectionClips.size() > 0)
					{
						log.info("***rendering section headlines: " + sectionClips.size() + " found");
						//out.append("<h3>" + sectionTitle + "</h3>");
						
						//render previous main section
						out.append(renderHTMLHeadlines(sectionClips));
						fullpages.putAll(sresults);
					}
				}
				
				log.info("***new subheader line: " + line);
				
				sresults = new LinkedHashMap<String,Clip>();
				
				stLine.nextToken();
				
				sectionTitle = stLine.nextToken().trim();
				out.append("<h2>" + sectionTitle + "</h2>");
				
				parentQuery = sectionTitle;
				query = null;
				lastDepth = 4;
				continue;
			}
			
			
			while (stLine.hasMoreTokens())
			{
				
				token = stLine.nextToken();
				
				if (!stLine.hasMoreTokens())
				{
					query = token;
				}
			}
			
			
			String thisQuery = query + ' ' + parentQuery;
			thisQuery = thisQuery.replace(" and ", " ");
			thisQuery = thisQuery.replace(" or ", " ");
			thisQuery = thisQuery.replace("&", " ");
			thisQuery = thisQuery.trim();
			
			//create the input params
			Map<String,String> input = new HashMap<String,String>();
			input.put("query", thisQuery);
			input.put("limit", max);
			input.put("sort",sort);
			input.put("start_time", DATEFORMAT.format(startTime.getTime()));
			input.put("end_time", DATEFORMAT.format(endTime));
			
			if (limitSources)
				input.put("source_filter_id",SOURCE_FILTER_ID);
			
			//make the API call
			Document doc = client.call("search_getRelatedArticles", input);
			
			log.info("query: " + thisQuery);
			
			buildClips(doc,"article", sresults, thisQuery, fullArticles);
			
			try { Thread.sleep(SEARCH_DELAY); } catch (Exception e){}
			
			if (query != null)
			{
				if (thisDepth > lastDepth)
				{
					parentQuery = query + ' ' + parentQuery;
					log.info("growing parent query: " + parentQuery);
				}
				else if (thisDepth < lastDepth)
				{
					parentQuery = sectionTitle;
					log.info("shrinking parent query: " + parentQuery);
				}
			}
			
			lastDepth = thisDepth;
			
		}
		
	/*
		if (sectionClips.size() > 0)
		{
			out.append("<h3>" + sectionTitle + "</h3>");
			//render previous main section
			out.append(renderHTMLHeadlines(sectionClips));
			fullpages.putAll(sresults);
		}*/
		
		//Render final headlines
		sectionClips = sresults.values();
		log.info("***rendering previous section headlines: " + sectionClips.size() + " found");
		//render previous main section
		out.append(renderHTMLHeadlines(sectionClips));
		fullpages.putAll(sresults);
		
		if (fullArticles)
			out.append(FULL_STORIES_DIVIDER);
		else
			out.append(EXCERPT_STORIES_DIVIDER);
		
		String pageText = renderHTMLPages(fullpages.values(), fullArticles);
		fullPageText.append(pageText);
		
		pageText = highlightSenatorNames(fullPageText.toString());
		
		out.append(pageText);
		
		
		try
		{
			
			output = out.toString();
			processing = false;
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void doKeywordClips (String term, Date startTime, Date endTime, boolean fullArticles)
	{
		
		output = null;
		processing = true;
		
		if (startTime == null)
		{
		startTime = new Date();
		startTime.setHours(0);
		startTime.setMinutes(1);
		startTime.setSeconds(0);
		startTime.setDate(startTime.getDate()-1);
		
		
		}

		if (endTime == null)
		{
		endTime = new Date();
		endTime.setHours(endTime.getHours()+5);
		}

		StringBuffer out = new StringBuffer();
		
		
		
		StringBuffer fullPageText = new StringBuffer();
		
		
	
		//initialize the daypi client
		DayPIClient client = new DayPIClient(accesskey, sharedsecret, server, version);
		
		LinkedHashMap<String, Clip> fullpages = new LinkedHashMap<String, Clip>();
		
		
		ArrayList<Clip> fullresults = new ArrayList<Clip>();
		
		int idx = 1;
		
		LinkedHashMap<String, Clip> sresults = new LinkedHashMap<String, Clip>();
		
		/*
		for (int s = 0; s < SOURCE_WHITELIST.length; s++)
		{*/
		
			//create the input params
			Map input = new HashMap();
			input.put("query", term);
			input.put("limit", max);
			input.put("sort",sort);
			
			input.put("start_time", DATEFORMAT.format(startTime.getTime()));
			input.put("end_time", DATEFORMAT.format(endTime));
			
			if (limitSources)
				input.put("source_filter_id",SOURCE_FILTER_ID);
			
			//input.put("source_whitelist",SOURCE_WHITELIST[s]);
			
			//make the API call
			Document doc = client.call("search_getRelatedArticles", input);
			
			buildClips(doc,"article", sresults, term, fullArticles);
			
			log.info("query: " + term + " = " + sresults.size());
			
			if (sresults.size()> 0)
			{
				
				
				out.append("<h2>TOPIC: " + term  + "</h2>");
			
				out.append(renderHTMLHeadlines(sresults.values()));
				
				Iterator<Clip> itclips = sresults.values().iterator();
				Clip clip = null;
				
				while(itclips.hasNext())
				{
					clip = itclips.next();
					fullpages.put(clip.getArticleId(), clip);
				}
				
			}
			
			fullresults.addAll(sresults.values());
			
			try { Thread.sleep(SEARCH_DELAY); } catch (Exception e){}
		
		//}
		
		if (fullArticles)
			out.append(FULL_STORIES_DIVIDER);
		else
			out.append(EXCERPT_STORIES_DIVIDER);
			
		String pageText = renderHTMLPages(fullpages.values(), fullArticles);
		fullPageText.append(pageText);
		
		pageText = highlightSenatorNames (fullPageText.toString());
		
		out.append(pageText);
		
		
		try
		{
			output = out.toString();
			processing = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//log.info("source: " + clip.getSourceName() + " rank=" + clip.getSourceRank() + " type=" + clip.getSourceType());
	
	public LinkedHashMap<String, Clip> filterResults (LinkedHashMap<String, Clip> results)
	{
		
		Iterator<String> it = results.keySet().iterator();
		Clip clip = null;
		String key = null;
		
		while (it.hasNext())
		{
			key = it.next();
			clip = results.get(key);
			
			if (clip.getSourceType() != 1)
			{
				results.remove(key);
			}
			
			if (clip.getSourceRank() > 4)
			{
				results.remove(key);
			}
		}
		
		return results;
	}
	
	
	public LinkedHashMap<String, Clip> buildClips (Document doc, String type, LinkedHashMap<String, Clip> results, String queryTerm, boolean fullArticles)
	{
		int uniqHits = 0;
		
		//parse and use the xml dom returned
		NodeList nodes = doc.getElementsByTagName(type);
		
		int hits = -1;
		
		
		Clip clip = null;
		
		for (int i = 0; i < nodes.getLength(); i++)
		{
			//log.info(nodes.item(i).getTextContent());
			NodeList storyNodes = nodes.item(i).getChildNodes();
			
			hits = storyNodes.getLength();
			
			clip = new Clip();
			
			for (int n = 0; n < storyNodes.getLength(); n++)
			{
				String nodeName = storyNodes.item(n).getNodeName();
				String content = storyNodes.item(n).getFirstChild().getNodeValue(); //1.5 change
				
				//headline
				if (nodeName.equals("headline"))
				{
					clip.setHeadline(content);
				}
				
				//timestamp
				else if (nodeName.equals("timestamp"))
				{
					try
					{
					//2009-03-13 11:03:57
					clip.setTimestamp(DATEFORMAT.parse(content));
					}
					catch (Exception e)
					{}
				}
				
				//excerpt
				else if (nodeName.equals("excerpt"))
				{
					clip.setExcerpt(content);
				}
				//source: name
				//source: rank
				//source: type
				else if (nodeName.equals("source"))
				{
					NodeList sourceNodes = storyNodes.item(n).getChildNodes();
					
					/*
					clip.setSourceName(sourceNodes.item(0).getTextContent());
					clip.setSourceRank(Integer.parseInt(sourceNodes.item(3).getTextContent()));
					clip.setSourceType(Integer.parseInt(sourceNodes.item(5).getTextContent()));
					*/
					
					clip.setSourceName(sourceNodes.item(0).getFirstChild().getNodeValue());
					clip.setSourceRank(Integer.parseInt(sourceNodes.item(3).getFirstChild().getNodeValue()));
					clip.setSourceType(Integer.parseInt(sourceNodes.item(5).getFirstChild().getNodeValue()));
					
				}
				
				//url
				else if (nodeName.equals("url"))
				{
					clip.setUrl(content);
					
					
				}
				
				//topic_story_relevance
				else if (nodeName.equals("topic_story_relevance"))
				{
					clip.setRelevance(Integer.parseInt(content));
				}
				
				//article_id
				else if (nodeName.equals("article_id"))
				{
					clip.setArticleId(content);
				}
				
				
			}
			
			if (clip.getHeadline()!=null)	
			{
				
				if (clip.getHeadline().toLowerCase().indexOf("letters to the editor")!=-1
						|| clip.getHeadline().toLowerCase().indexOf("letter to the editor")!=-1)
				{
					continue;
				}
				
			//	log.info("headline: " + clip.getHeadline());
				
				Clip storedClip = results.get(clip.getHeadline());
				
				if (storedClip==null)
				{
						clip.setMatchingTerms(queryTerm);
					
						results.put(clip.getHeadline(), clip);
						uniqHits++;
				}
				else
				{
					if (storedClip.getSourceName().indexOf(clip.getSourceName())==-1)
					{
						String sourceName = storedClip.getSourceName() + "/" + clip.getSourceName();
						storedClip.setSourceName(sourceName);
					
					}
					
					if (queryTerm != null)
					{
						if (storedClip.getMatchingTerms() == null)
							storedClip.setMatchingTerms(queryTerm);
						else if (storedClip.getMatchingTerms().indexOf(queryTerm)==-1)
							storedClip.setMatchingTerms(storedClip.getMatchingTerms() + ", " + queryTerm);
					}
					
					
					
					results.put(storedClip.getHeadline(), storedClip);
				}
				
				
				if (sourceTypeFilter != -1 && clip.getSourceType() != sourceTypeFilter)
				{
					results.remove(clip.getHeadline());
				}
				
				
				if (sourceRankFilter != -1 && clip.getSourceRank() > sourceRankFilter)
				{
					results.remove(clip.getHeadline());
				}
				
				if (fullArticles)
				{
					 clip.setFullStory(getFullPage(clip));
					
					if (clip.getFullStory().indexOf("Senate ") == -1 
							&& clip.getFullStory().indexOf("Sen ") == -1
							&& clip.getFullStory().indexOf("Sen.") == -1)
					{
						results.remove(clip.getHeadline());
					}
						
				}
				
						
				
			}
			
			
		}
		
		log.info("query= " + queryTerm + "; hits=" + hits + "; uniq hits=" + uniqHits);
		
		return results;
	}
	
	public LinkedHashMap<String, Clip> buildTestClips (Document doc, String type, LinkedHashMap<String, Clip> results, String queryTerm)
	{
		
		if (results == null)
		{
			log.info ("results null for: " + queryTerm);
			results = new LinkedHashMap<String, Clip>();
		}
		
		int uniqHits = 0;
		
		//parse and use the xml dom returned
		
		int hits = 10;
		
		int max = 10;
		
		Clip clip = null;
		
		for (int i = 0; i < max; i++)
		{
			
			clip = new Clip();
			clip.setHeadline("Breaking News: This is very existing");
			clip.setTimestamp(new Date());
			clip.setExcerpt("This is the excerpt");
			clip.setSourceName("New York Post");
			clip.setUrl("http://nypost.com");
			clip.setArticleId("a" + clip.getTimestamp().getTime());
			results.put(clip.getArticleId(), clip);
			
		}
		
		
		return results;
	}
	
	public String renderHTMLHeadlines (Collection<Clip> results)
	{
		
		Clip[] resultList = results.toArray(new Clip[1]);
		
		Arrays.sort(resultList);
		
		
		Clip clip = null;
		
	//	Iterator<Clip> it = results.iterator();
		
		StringBuffer out = new StringBuffer();
		
	//	it = results.iterator();
	
		//out.append("<ul>");
		
		for (int i = 0; i < resultList.length; i++)
		{
			clip = resultList[i];
			
			if (clip == null || clip.getHeadline() == null)
				break;
			
			String headline = processHeadline(clip.getHeadline());
			
			String anchor = URLEncoder.encode(clip.getHeadline());
			if (anchor.length()>anchorLength)
				anchor = anchor.substring(0,anchorLength);
			out.append("<li><a href=\"#" + anchor + "\">" + headline + "</a>, ");
			out.append(clip.getSourceName());
			out.append("\r\n");
			
			/*
			if (clip.getMatchingTerms()!=null)
			{
				out.append (" (matching terms=");
				out.append (clip.getMatchingTerms());
				out.append( ")");
			}*/
			
			out.append("</li>");
		//	out.append(", ");
		//	out.append(clip.getTimestamp()+"<br/><br/>");
			
			/*
			String excerpt = clip.getExcerpt();
			
			excerpt = excerpt.replace(term, "<b>" + term + "</b>");
			
			out.append("Excerpt: <em>" + excerpt + "</em>");
			
			out.append("<hr/>");
			*/
			
		}
		
		//out.append("</ul>");
		
		return out.toString();
	}
	
	
	public String renderHTMLPages (Collection<Clip> results, boolean fullArticles)
	{
		
		Clip clip = null;
		
		Iterator<Clip> it = results.iterator();
		
		StringBuffer out = new StringBuffer();
		
		it = results.iterator();
		
		DateFormat sdf = SimpleDateFormat.getDateTimeInstance();
		
		while (it.hasNext())
		{
			clip = it.next();
			
			try
			{
			
			String anchor = URLEncoder.encode(clip.getHeadline());
			if (anchor.length()>anchorLength)
				anchor = anchor.substring(0,anchorLength);
			out.append("<a name=\"" + anchor  + "\"></a>");
			
			out.append("<div class=\"headline\"><h4><a href=\"" + clip.getUrl() + "\" target=\"_new\">" + processHeadline(clip.getHeadline()) + "</a>, ");
			out.append(clip.getSourceName());
			
			out.append("</h4></div>");
			out.append(sdf.format(clip.getTimestamp()));
			out.append(" ");
			out.append("(<a href=\"" + clip.getUrl() + "\" target=\"_new\">link</a>)");
			out.append("<br/>");
			
			if (fullArticles)
			{
				if (clip.getFullStory() == null)
				{
					String page = getFullPage(clip);
					clip.setFullStory(page);
				}
				
				out.append(clip.getFullStory());
			}
			else
			{
				out.append(clip.getExcerpt());
			}
			
			out.append("<br/><br/><a href=\"#top\">back to top</a>");
			out.append("\r\n");
			out.append("<hr/>");
			out.append("\r\n");
			
			}
			catch (Exception e)
			{
				System.out.println("clip error: " + clip.getArticleId() + "=" + clip.getHeadline());
			}
			
		}
		
		
		return out.toString();
	}
	
	
	
	public static String getFullPage (Clip clip)
	{
		StringBuffer out = new StringBuffer();
		
		log.info("fetching full story (rel=" + clip.getRelevance() + ") from " + clip.getSourceName() + "(rank=" + clip.getSourceRank() + "): " + clip.getUrl());
		
		try
		{
			
			String page = (runCommandLine(LYNX_CMD + clip.getUrl()));
			
			page = filterPage (page, clip);
			
			
			StringTokenizer st = new StringTokenizer (page," ");
			String word = null;
			
			while (st.hasMoreElements())
			{
				word = st.nextToken();
				
				if (word.indexOf('[')==0)
				{
					int idx = word.indexOf(']');
					
					if (idx != -1)
					{
						word = word.substring(idx+1);
					}
				}
				
				out.append(word);
				out.append(' ');
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		return out.toString();
	}
	
	public static String processHeadline (String headline)
	{

		String[] list = headline.split("[A-Z]");
		double perc = ((double)list.length)/((double)headline.length());
		if (perc>.4)
		{
			headline = headline.toLowerCase();
			
			//headline = headline.substring(0,1).toUpperCase() + headline.substring(1);
			
			StringTokenizer st = new StringTokenizer(headline, " ");
			
			headline = "";
			String token = null;
			
			while(st.hasMoreTokens())
			{
				token = st.nextToken();
				headline += token.substring(0,1).toUpperCase() + token.substring(1) + " ";
			}
		}
		
		return headline.trim();
	}
	
	
	/*
	 * 
	 * 
	 */
	
	public static String filterPage (String page, Clip clip)
	{
		
		int hidx = page.indexOf(clip.getHeadline());
		
		if (hidx != -1)
			page = page.substring(hidx);
		
		for (int i = 0; i < PAGE_FILTER_KEYS.length; i++)
		{
			hidx = page.indexOf(PAGE_FILTER_KEYS[i]);
			if (hidx !=-1)
				page =page.substring(0, hidx);
	
		}
		return page;
	}
	
	public static String getFlag (Clip clip)
	{
		String flag = "";
		
		if (clip.getHeadline().indexOf("Sen")==-1&&clip.getExcerpt().indexOf("Sen")==-1)
			flag = "FLAGGED: ";
		
		return flag;
	}
	
	public String highlightSenatorNames (String pageText)
	{

		for (int i = 0; i < SENATOR_TOPIC_IDS.length; i++)
		{
			if (SENATOR_TOPIC_IDS[i][0].length()>0)
			{
				pageText = pageText.replace(SENATOR_TOPIC_IDS[i][0], "<b>" + SENATOR_TOPIC_IDS[i][0] + "</b>");
			}
			else if (SENATOR_TOPIC_IDS[i][1].length()>0)
			{
				pageText = pageText.replace(SENATOR_TOPIC_IDS[i][1], "<b>" + SENATOR_TOPIC_IDS[i][1] + "</b>");
			}
		}
		
		return pageText;
	}
	 private static String runCommandLine (String cmd) throws Exception
	    {
		 	
	    //	s_logger.info(cmd);
	    	Process process = Runtime.getRuntime().exec(cmd);
	                	    	
	    	//StreamInputReader sir = new StreamInputReader(process);
	    	//StreamErrorReader ser = new StreamErrorReader(process);  
	       
	    	StringBuffer out = new StringBuffer();
	    	
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    	
	    	String line =null;
	    	
	    	boolean prevLineBlank = false;
	    	
	    	while ((line = reader.readLine())!=null)
	    	{
	    		line = line.trim();
	    		
	    		if (line.startsWith("*") || line.startsWith("[") || line.startsWith("_")
	    				|| line.startsWith("+") || line.startsWith("o") || line.startsWith("#") || line.startsWith("|"))
	    			continue;
	    		
	    		out.append(line);
	    		
	    		if (line.length()<5 && (!prevLineBlank))
	    		{
	    			out.append("<br/><br/>");
	    			prevLineBlank = true;
	    		}
	    		else
	    		{
	    			out.append(" ");
	    			prevLineBlank = false;
	    		}
	    		
	    	}
	    	
	      // int exitVal = process.waitFor();     
	       
	       
	       return out.toString();
	       
	    }

	 
	 
	 
	 public static final String[] SOURCE_WHITELIST = {"","The New York Times","New York Post"};
	 
	 public static final String[] PAGE_FILTER_KEYS = {"Post new comment","Reader Comments","Reader's Comments","Leave a Reply"
			,"Submit your comment","ADVERTISEMENT","Recent Comments","Post a comment","post a comment"
			,"Email Story","Visible links","NEXT PAGE","Next Article","MOST POPULAR"
			,"COMMENTS","Ads by Google","Add your comment","Related Content","Related Stories"
			,"Leave a comment","Leave a Comment","Loading commenting interface...","Tags:","tags:","Article Rating","comments:","Comments:","Today's Top Picks","Your thoughts"};//,"THIS WEEK'S HOT TOPICS","Text Size: Normal | Large | Larger"};
	


public final static String[][] SENATOR_TOPIC_IDS = {

{"Senate Majority Conference","LABEL"},
{"Majority Leader Malcolm A. Smith","00ozdZEerbfq9"},
{"","Malcolm A. Smith"},
{"","Malcolm Smith"},
{"Senator Eric Adams","06iB8Jj7h2e8p"},
{"","Eric Adams"},
{"Senator Joseph P Addabbo Jr","02VH4lA1wg9Er"},
{"","Joseph P. Addabbo"},
{"","Joseph Addabbo"},
{"Senator Darrel J. Aubertine","02uj8GSgrP4zg"},
{"", "Darrel J. Aubertine"},
{"", "Darrel Aubertine"},
{"Senator Neil Breslin","0fVBfwR8FT1xp"},
{"","Neil Breslin"},
{"Senator Ruben Diaz","0aFa3wy63g7rC"},
{"","Ruben Diaz"},
{"Senator Martin Malave Dilan","08z704341SdQr"},
{"","Martin Malave Dilan"},
{"","Martin Dilan"},
{"Senator Thomas Duane","0dHvdDtbw06iX"},
{"","Thomas Duane"},
{"","Tom Duane"},
{"Senator Pedro Espada Jr.","0cgQ2KtfxQ3J9"},
{"","Pedro Espada"},
{"Senator Brian X. Foley","089ubxt5216Gn"},
{"","Brian X. Foley"},
{"","Brian Foley"},
{"Senator Ruth Hassell-Thompson","0bq2gjE1hSfWw"},
{"","Ruth Hassell-Thompson"},
{"Senator Shirley L. Huntley","01GVgJzgFI8Gb"},
{"","Shirley L. Huntley"},
{"","Shirley Huntley"},
{"Senator Craig M. Johnson","0dCgdjO0n9eeH"},
{"","Craig M. Johnson"},
{"","Craig Johnson"},
{"Senator Jeffrey Klein","0cXr31t3OP2FY"},
{"","Jeffrey Klein"},
{"","Jeff Klein"},
{"Senator Liz Krueger","0aqDf072jWgk1"},
{"","Liz Krueger"},
{"Senator Carl Kruger","09q76Vm4Kc5QU"},
{"","Carl Kruger"},
{"Senator Hiram Monserrate","02xz9dAesZ1Gj"},
{"","Hiram Monserrate"},
{"Senator Velmanette Montgomery","01GmeM31kb380"},
{"","Velmanette Montgomery"},
{"","Velma Montgomery"},
{"Senator George Onorato","0f23a4n6QP9KF"},
{"","George Onorato"},
{"Senator Suzi Oppenheimer","08gn7oibLt4qe"},
{"","Suzi Oppenheimer"},
{"Senator Kevin S. Parker","0g24dGY1freHQ"},
{"","Kevin S. Parker"},
{"","Kevin Parker"},
{"Senator Bill Perkins","0gN75cE12Xba6"},
{"","Bill Perkins"},
{"Senator John L. Sampson","07Ev3ef6qS6Os"},
{"","John L. Sampson"},
{"","John Sampson"},
{"Senator Diane Savino","0cs74rHc3T5Ez"},
{"","Diane Savino"},
{"Senator Eric T. Schneiderman","0g152F3bGT2l6"},
{"","Eric T. Schneiderman"},
{"","Eric Schneiderman"},
{"Senator Jose Serrano","02JbaQMe7ybvt"},
{"","Jose Serrano"},
{"Senator Daniel L. Squadron","08Esa0A9Ghc8z"},
{"","Daniel L. Squadron"},
{"","Daniel Squadron"},
{"Senator William T. Stachowski","04IG85I0x7dQE"},
{"","William T. Stachowski"},
{"","William Stachowski"},
{"Senator Toby Ann Stavisky","095yboE30Q2rI"},
{"","Toby Ann Stavisky"},
{"","Toby Stavisky"},
{"Senator Andrea Stewart-Cousins","0e8Q6Nr0jOc57"},
{"","Andrea Stewart-Cousins"},
{"Senator Antoine M. Thompson","05sp1FCgOecKL"},
{"","Antoine Thompson"},
{"Senator David J. Valesky","0e0w6e23sCbyR"},
{"","David J. Valesky"},
{"","David Valesky"},

{"Senate Minority Conference","LABEL"},
{"Minority Leader Dean Skelos","05JOgrG3hN7P6"},
{"","Dean Skelos"},
{"Senator James Alesi","0gGSeKN2oN1LQ"},
{"","James Alesi"},
{"","Jim Alesi"},
{"Senator John Bonacic","09dPgIz3YE5YZ"},
{"","John Bonacic"},
{"Senator John DeFrancisco","08Ng5ua2sm6zm"},
{"","John DeFrancisco"},
{"Senator Hugh Farley","06wVccL8Kk3Kq"},
{"","Hugh Farley"},
{"Senator John Flanagan","02ttcmW9Mj1mV"},
{"","John Flanagan"},
{"Senator Charles Fuschillo","0duxec66VcbgC"},
{"","Charles Fuschillo"},
{"","Chuck Fuschillo"},
{"Senator Martin Golden","0bM60c735EgmG"},
{"","Martin Golden"},
{"Senator Joseph A. Griffo","03N08grddRdOd"},
{"","Joseph A. Griffo"},
{"","Joseph Griffo"},
{"Senator Kemp Hannon","0cLBaEBejCawi"},
{"","Kemp Hannon"},
{"Senator Owen Johnson","06xp0AFeAD8qp"},
{"","Owen Johnson"},
{"Senator Andrew Lanza","0c4V20rdLiar2"},
{"","Andrew Lanza"},
{"Senator Bill Larkin","04EX4OD0JS4CM"},
{"","Bill Larkin"},
{"Senator Kenneth LaValle","0d1vfGV31V0SQ"},
{"","Kenneth LaValle"},
{"","Ken LaValle"},
{"Senator Vincent L. Leibell III","0c6o1eV8Oa451"},
{"","Vincent L. Leibell III"},
{"","Vincent Leibell"},
{"","Vince Leibell"},
{"Senator Thomas W. Libous","0gtKduU3Cf1Pn"},
{"","Thomas W. Libous"},
{"","Thomas Libous"},
{"","Tom Libous"},
{"Senator Elizabeth O'C. Little","0eWE7gH9jIeik"},
{"","Elizabeth O'C. Little"},
{"","Elizabeth Little"},
{"Senator Carl Marcellino","05fn2yKcnG6ta"},
{"","Carl Marcellino"},
{"Senator George D Maziarz","0a9Xgs2e7U5oK"},
{"","George D. Maziarz"},
{"","George Maziarz"},
{"Senator Roy McDonald","0d5G7Wwfza1fb"},
{"","Roy McDonald"},
{"Senator Thomas Morahan","00mjfaPaBeehV"},
{"","Thomas Morahan"},
{"","Tom Morahan"},
{"Senator Michael F. Nozzolio","05epeVufPd9eq"},
{"","Michael F. Nozzolio"},
{"","Michael Nozzolio"},
{"","Mike Nozzolio"},
{"Senator Frank Padavan","03cweGPd0Hfi0"},
{"","Frank Padavan"},
{"Senator Michael H. Ranzenhofer","00Tjg1E1AR3Pz"},
{"","Michael H. Ranzenhofer"},
{"","Michael Ranzenhofer"},
{"","Mike Ranzenhofer"},
{"Senator Joseph Robach","02Aw62d0XSbAh"},
{"","Joseph Robach"},
{"","Joe Robach"},
{"Senator Stephen Saland","020bcHPaaI6Nk"},
{"","Stephen Saland"},
{"Senator James L. Seward","0fw702N8Elgvc"},
{"","James L. Seward"},
{"","James Seward"},
{"","Jim Seward"},
{"Senator Dale Volker","0bNngHT8QybZw"},
{"","Dale Volker"},
{"Senator George H. Winner Jr.","0g6z64R5qq9Hi"},
{"","George H. Winner Jr."},
{"","George Winner"},
{"Senator Catharine M. Young","0gQG7IBftDcLc"},
{"","Catharine M. Young"},
{"","Catharine Young"},

{"State Offices","LABEL"},
{"Governor David A. Paterson","xxx"},
{"","Governer Paterson"},
{"","David A. Paterson"},
{"","David Paterson"},
{"Attorney General Andrew M. Cuomo","xxx"},
{"","Andrew M. Cuomo"},
{"","Andrew Cuomo"},
{"State Comptroller Thomas P. DiNapoli","xxx"},
{"","Thomas P. DiNapoli"},
{"","Thomas DiNapoli"},
{"","Tom DiNapoli"},
{"Assembly Speaker Sheldon Silver","xxx"},
{"","Sheldon Silver"},
{"","Assemblyman Silver"},
{"","Shelly Silver"}

};

}


/*
 * Smith, Malcolm A.	Majority
Adams, Eric	Majority
Addabbo, Joseph P, Jr	Majority
Aubertine, Darrel J.	Majority
Breslin, Neil D.	Majority
Diaz, Ruben , Sr.	Majority
Dilan, Martin Malave	Majority
Duane, Thomas K.	Majority
Espada, Pedro , Jr.	Majority
Foley, Brian X.	Majority
Hassell-Thompson, Ruth	Majority
Huntley, Shirley L.	Majority
Johnson, Craig M.	Majority
Klein, Jeffrey D.	Majority
Krueger, Liz	Majority
Kruger, Carl	Majority
Monserrate, Hiram	Majority
Montgomery, Velmanette	Majority
Onorato, George	Majority
Oppenheimer, Suzi	Majority
Parker, Kevin S.	Majority
Perkins, Bill	Majority
Sampson, John L.	Majority
Savino, Diane J.	Majority
Schneiderman, Eric T.	Majority
Serrano, José M.	Majority
Squadron, Daniel L.	Majority
Stachowski, William T.	Majority
Stavisky, Toby Ann	Majority
Stewart-Cousins, Andrea	Majority
Thompson, Antoine M.	Majority
Valesky, David J.	Majority


Skelos, Dean G.	Minority
Alesi, James S.	Minority
Bonacic, John J.	Minority
DeFrancisco, John A.	Minority
Farley, Hugh T.	Minority
Flanagan, John J.	Minority
Fuschillo, Charles J., Jr.	Minority
Golden, Martin J.	Minority
Griffo, Joseph A.	Minority
Hannon, Kemp	Minority
Johnson, Owen H.	Minority
Lanza, Andrew J.	Minority
Larkin, William J., Jr.	Minority
LaValle, Kenneth P.	Minority
Leibell, Vincent L., III	Minority
Libous, Thomas W.	Minority
Little, Elizabeth O'C.	Minority
Marcellino, Carl L.	Minority
Maziarz, George D.	Minority
McDonald, Roy J.	Minority
Morahan, Thomas P.	Minority
Nozzolio, Michael F.	Minority
Padavan, Frank	Minority
Ranzenhofer, Michael H.	Minority
Robach, Joseph E.	Minority
Saland, Stephen M.	Minority
Seward, James L.	Minority
Volker, Dale M.	Minority
Winner, George H., Jr.	Minority
Young, Catharine M. 	Minority

*/