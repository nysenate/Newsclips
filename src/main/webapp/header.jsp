<%@ page language="java" import="java.util.*, java.io.*, gov.nysenate.newsclips.*" pageEncoding="UTF-8"%>

<%
String term = request.getParameter("term");
if (term == null) term = "";

String appPath = request.getContextPath();

String title = "NY Senate Open News";
 %>
 

<html>
  <head>
  
<title><%=title%></title>
<link rel="shortcut icon" href="<%=appPath%>/img/nys_favicon_0.ico" type="image/x-icon" />
 
<link rel="stylesheet" type="text/css" media="screen" href="<%=appPath%>/style.css"/> 

<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; minimum-scale=1.0; user-scalable=0;" />
 <meta name="apple-mobile-web-app-capable" content="YES">
 <script type="text/javascript" src="<%=appPath%>/js/jquery-1.3.2.min.js"></script>
 <script type="text/javascript" src="<%=appPath%>/js/jquery.highlight.js"></script>
 
 <link rel="alternate" type="application/rss+xml" title="RSS 2.0" href="<%=appPath%>/feed" />
  </head>
<body>  
    <div id="header-home" style="margin-top:0px">
     <div style="float:left;">
     <a href="http://nysenate.gov">NYSenate.gov</a>:
    <a href="http://nysenate.gov/senators">Senators</a>&nbsp;|&nbsp;
    <a href="http://nysenate.gov/committees">Committees</a>&nbsp;|&nbsp;
    <a href="http://nysenate.gov/issues-initiatives">Issues &amp; Initiatives</a>&nbsp;|&nbsp;
    <!--
    <a href="http://nysenate.gov/legislation-open-senate">Open Senate</a>&nbsp;|&nbsp;
    
    <a href="http://nysenate.gov/about-us">About</a>&nbsp;|&nbsp;
    <a href="http://nysenate.gov/media">Photos &amp; Videos</a>&nbsp;|&nbsp;
     -->
    <a href="http://nysenate.gov/newsroom">Newsroom</a>
    &nbsp;|&nbsp;
      <a href="http://open.nysenate.gov/legislation">Open Legislation</a>
    </div>
    <!-- 
      <div style="float:right;">

<a href="<%=appPath%>/beta"><b>BETA FEEDBACK</b></a> &nbsp;|&nbsp; 

<a href="<%=appPath%>/mobile/info">Mobile Access</a> &nbsp;|&nbsp; 

<a href="http://wiki.github.com/nysenatecio/OpenLeg">Developers</a>
   </div>
    -->
   </div>
<div class="notice">
Open Newsclips is a beta service available on a trial basis; please send your feedback to ciodesk@senate.state.ny.us to help us improve the service.
</div>

    <div id="header">
    
    <div id="logobox" style="width:134px;height:32px;padding-top:3px;"><a href="<%=appPath%>"><img src="<%=appPath%>/img/openwordlogo.gif" /></a>
   
    </div>
  
   
<div style="font-size:9pt;line-height:16px;">
<form method="get" action="<%=appPath%>/search.jsp" style="padding-bottom:6px">
		
Search recent news from New York sources:  
		
		<input type="text" id="txtSearchBox" name="term" value="<%=term%>">	
<input type="submit" value="go"/>

</form>
   </div>
  
   </div>
   
