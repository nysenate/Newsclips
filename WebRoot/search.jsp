<%@ page language="java" import="java.util.*, java.io.*, gov.nysenate.newsclips.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

Date start = null;
Date end = null;

String term = request.getParameter("term");
String category = request.getParameter("category");
String topicString = request.getParameter("topics");

if (category == null)
	category = "keyword";
	
int SOURCE_TYPE_FILTER = -1;
int SOURCE_RANK_FILTER = -1;
   
boolean useCache = false;
boolean fullArticles = false;
boolean limitSources = true;

if (request.getParameter("cache")!=null)
{
	useCache = false;
}

if (request.getParameter("limit")!=null)
{
	limitSources = true;
}

if (request.getParameter("excerpt")!=null)
{
	fullArticles = true;
}



if (term == null) term = "";


%>
<jsp:include page="header.jsp"/>


   
  
   <%
   String cacheKey = category + "-" + term;
   
    ClipsClient client = (ClipsClient) session.getAttribute("client");
   
  	 if (client == null)
  	 {
   	 	client = new ClipsClient ();
   	 	session.setAttribute("client", client);
   	 }
   	 
   	 
   	client.setLimitSources(limitSources);
   	
   	String output = null;
   	
   	if (client.isProcessing())
   	{
   		output = "Processing request...";
   	}
   	
    if (category != null && category.length()>0){

	  		output = null;//client.getOutput();//(String)application.getAttribute(cacheKey);

		   if ((term != null && term.length() > 0))
		   {	
		   
			   client.setSourceRankFilter(SOURCE_RANK_FILTER);
			   client.setSourceTypeFilter(SOURCE_TYPE_FILTER);
			   
			   if (category.equals("senate"))
			   	client.doSenateClips(start, end, fullArticles);
			   
			   
			   else if (category.equals("topics"))
			   	client.doTopicClips(topicString, start,end, fullArticles);
			   else
			   	client.doKeywordClips(term, start,end, fullArticles);
			   
			   // output = baos.toString(); 
			//	application.setAttribute(cacheKey,output);
			
				output = client.getOutput();
		
		  } 
		  else if (output == null)
		  	output = "";
   
    %>
   
	<%=output%>
   <%} %>
   

   
 <jsp:include page="footer.jsp"/>
   
    