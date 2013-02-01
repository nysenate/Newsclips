<%@ page language="java" import="java.util.*, java.io.*, gov.nysenate.newsclips.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache" %>

<%
if (request.getParameter("download")!=null)
{
response.setContentType("text/html");

String filename = "nysenatenews";
	
filename += "-" + new Date().getTime();

response.setHeader("Content-Disposition","attachment; filename=clips-" + filename + ".html");
}

%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

Date start = null;
Date end = null;
	
int SOURCE_TYPE_FILTER = -1;
int SOURCE_RANK_FILTER = -1;
   
boolean fullArticles = false;
boolean limitSources = false;


if (request.getParameter("limit")!=null)
{
	limitSources = true;
}

if (request.getParameter("excerpt")!=null)
{
	fullArticles = false;
}

int cacheTime = 60*60*12;
%>
<jsp:include page="header.jsp"/>
   
   <cache:cache time="<%=cacheTime%>"  scope="application">
   <%
   String category = "names";
   String term = "";
   
    ClipsClient client = (ClipsClient) session.getAttribute("client");
   
  	 if (client == null)
  	 {
   	 	client = new ClipsClient ();
   	 	session.setAttribute("client", client);
   	 }
   	 
   	 
   	client.setLimitSources(limitSources);
   	
   	String output = null;
  	client.setSourceRankFilter(SOURCE_RANK_FILTER);
		   client.setSourceTypeFilter(SOURCE_TYPE_FILTER);
		   
		   	client.doSenateClips(start, end, fullArticles);
		   
		   
		   output = client.getOutput();
		   
       Date genDate = new Date();
   
   if (output == null || output.trim().length() == 0)
   {
   output = "<em>No results found</em>";
   }
    %>
  
   <em>Timestamp: <%=genDate.toLocaleString()%></em><%if (request.getParameter("download")==null){%> | <a href="?download=true">save to disk</a><%}%><hr/>
   
   
	<%=output%>
   
  </cache:cache> 
  
   <jsp:include page="footer.jsp"/>

