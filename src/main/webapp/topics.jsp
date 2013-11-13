<%@ page language="java" import="java.util.*, java.io.*, gov.nysenate.newsclips.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache" %>
<%

Date start = null;
Date end = null;

String term = request.getParameter("term");
String category = "topics";
String topicString = request.getParameter("topics");

if (category == null)
	category = "keyword";
	
int SOURCE_TYPE_FILTER = -1;
int SOURCE_RANK_FILTER = -1;
   
boolean fullArticles = false;
boolean limitSources = true;


if (request.getParameter("limit")!=null)
{
	limitSources = true;
}

if (request.getParameter("excerpt")!=null)
{
	fullArticles = true;
}



if (term == null) term = "";

int cacheTime = 60*60*24;
%>
<jsp:include page="header.jsp"/>
   
      <cache:cache time="<%=cacheTime%>"  scope="application">
   
  
   <%
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

	  	client.setSourceRankFilter(SOURCE_RANK_FILTER);
	   client.setSourceTypeFilter(SOURCE_TYPE_FILTER);
	   
	   client.doTopicClips(topicString, start,end, fullArticles);
	 
		output = client.getOutput();
   
    %>
   
	<%=output%>
   <%} %>
   </cache:cache>
   
 <jsp:include page="footer.jsp"/>
   
    
