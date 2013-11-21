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
boolean limitSources = false;

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


<div style="font-size:14pt;margin:10px;background:white;padding:3px;">
Welcome to the New York State Senate Newsclips service, developed by Senate
Technology Services and powered by our partner, DayLife.
This service allows you to search and browse online news from the last 24 hours utilizing both local, New York-area news, as well as
 sources from around the globe.
 <br/><br/>
 - <a href="names.jsp">Senator Names</a>: these stories are updated every 3 hours with the latest articles matching members of the Senate and prominent State offices, as well.
<br/><br/>
 - <a href="topics.jsp">Daily Topics</a>: these stories are also updated every 3 hours, built from an extensive outline of queries on topics ranging from the economy to the environment.
 <br/><br/>
 
 - <a href="search.jsp?category=keyword&term=Brooklyn&limit=on">Keyword Search</a>: you can run (and bookmark!) your own custom searches any time you'd like on proper names, bills, hot topics and more.
 <hr/>

 <em>* News Clips is also available from your Blackberry, iPhone or other web-enabled mobile device.</em>
 </div>
   
 <jsp:include page="footer.jsp"/>
   
    
