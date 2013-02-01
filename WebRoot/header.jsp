<%@ page language="java" import="java.util.*, java.io.*, gov.nysenate.newsclips.*" pageEncoding="UTF-8"%>

<%
String term = request.getParameter("term");
if (term == null) term = "";
 %>
<html>
  <head>
  
    <title>The NY Senate News Clips</title>

  <%if (request.getParameter("download")==null){%> 

<link rel="stylesheet" type="text/css" media="screen" href="style.css"/> 
<%}else{ %>
<style>
<jsp:include page="style.css"/>
</style>
<%} %>
<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; minimum-scale=1.0; user-scalable=0;" />
 <meta name="apple-mobile-web-app-capable" content="YES">
  </head>
  
  <body>
   <%if (request.getParameter("download")==null){%>
   
   
    <div id="searchbar">
    <div id="logobox"><img src="img/logo75.png" /></div>
    
   <h2>The NY Senate News Clips <em style="font-size:12px;font-weight:normal;">Online news from the last twenty-four hours...</em></h2> 
  <form class="searchForm" action="search.jsp" method="GET">
   <div style="float:left;">
 <a href="index.jsp">Home</a> | <a href="names.jsp">Senator Names</a> | <a href="topics.jsp">Daily Topics</a>
 
 or
 </div>
 <div style="float:left;margin-left:10px;">
   <input type="hidden" name="category" value="keyword"/>
   <b>search any keyword </b> <input type="text" name="term" value='<%=term %>' />
  
&nbsp;
<input type="submit" value="GO"/>
&nbsp;
<!-- 
<br/>
<input type="checkbox" name="excerpt" <%if (request.getParameter("excerpt")!=null){%>CHECKED="CHECKED"<%}%> /> Full Articles (Slower)
<input type="checkbox" name="limit"  <%if (request.getParameter("limit")!=null){%>CHECKED="CHECKED"<%}%> /> NY News Sources Only
 -->
</div>
   </form>


  
   </div><br style="clear:both;"/>
   <%}%>