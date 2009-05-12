<%@ page language="java" import="java.util.*, java.io.*, org.nysenatecio.clips.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

Date start = null;
Date end = null;

String term = request.getParameter("term");
String category = request.getParameter("category");
String topicString = request.getParameter("topics");

  
   
boolean useCache = true;
boolean fullArticles = true;

if (request.getParameter("cache")!=null)
{
	useCache = false;
}

if (request.getParameter("excerpt")!=null)
{
	fullArticles = false;
}

if (request.getParameter("download")!=null)
{
response.setContentType("text/html");

String filename = category;

if (term != null)
	filename += "-" + term.replace(' ','_');
	
filename += "-" + new Date().getTime();

response.setHeader("Content-Disposition","attachment; filename=clips-" + filename + ".html");
}



if (term == null) term = "";


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  
    
    <title>NY Senate News Clips</title>

<script>
function updateSearch() 
{
	var searchType = document.getElementById("selCategory").value;
	
	if (searchType == "keyword")
	{
		document.getElementById("optKeyword").style.display="inline";
	}
	else
	{
		document.getElementById("optKeyword").style.display="none";
	}
	
	if (searchType == "topics")
	{
		document.getElementById("optTopics").style.display="inline";
	}
	else
	{
		document.getElementById("optTopics").style.display="none";
	}
}
</script>
  <%if (request.getParameter("download")==null){%> 

<link rel="stylesheet" type="text/css" media="screen" href="style.css"/> 
<%} %>
  </head>
  
  <body>
    <div id="searchbar">
  <%if (request.getParameter("download")==null){%> 
   <form method="post">
   
   Show me: <select id="selCategory" name="category" onchange="updateSearch()" >
<option value="senate" <%if (category!=null && category.equals("senate")){%>SELECTED<%} %>>Senator Name Search</option>
<option value="topics" <%if (category!=null && category.equals("topics")){%>SELECTED<%} %>>Senate Topics Search</option>
<option value="keyword" <%if (category!=null && category.equals("keyword")){%>SELECTED<%} %>>Keyword Search</option>

</select> <input type="submit" value="GO"/>
&nbsp;

<input type="checkbox" name="excerpt" <%if (request.getParameter("excerpt")!=null){%>CHECKED="CHECKED"<%}%> /> Excerpts Only
<input type="checkbox" name="cache"  <%if (request.getParameter("cache")!=null){%>CHECKED="CHECKED"<%}%>/> Refresh Cache
<input type="checkbox" name="download"   <%if (request.getParameter("download")!=null){%>CHECKED="CHECKED"<%}%>/> Download File

<br/><br/>
<div id="optKeyword" style="<%if (category==null || (!category.equals("keyword"))){%>display:none;<%} %>background:#eee;padding:3px;margin:3px;"><b>Enter keyword:</b> <input type="text" name="term" value="<%=term %>"/></div> 
<div id="optTopics" style="<%if (category==null || (!category.equals("topics"))){%>display:none;<%} %>background:#eee;padding:3px;margin:3px;">
<b>Topic List</b><br/>
<textarea name="topics" rows="20" cols="80">
<%if (topicString!=null){ %><%=topicString %><%}else{ %>
<jsp:include page="defaultTopics.jsp"/>
<%} %>
</textarea>
</div>
   </form>
   </div>
   
<%}%>   
   <%
   String cacheKey = category + "-" + term;
   
	if (category != null && category.length()>0){
   String output = (String)application.getAttribute(cacheKey);

   if (output==null || (!useCache))
   {	
   ClipsClient client = new ClipsClient();
   
   ByteArrayOutputStream baos = new ByteArrayOutputStream();
   
   if (category.equals("senate"))
   	client.doSenateClips(baos,start,end, fullArticles);
   
   else if (category.equals("topics"))
   	client.doTopicClips(topicString, baos,start,end, fullArticles);
   else
   client.doKeywordClips(term, baos,start,end, fullArticles);
   
    output = baos.toString(); 
	application.setAttribute(cacheKey,output);
  } 
   
    %>
   
	<%=output%>
   <%} %>
   
   
  </body>
</html>
<!-- 
New York Senate: Pass Bills, Introduce Bills, Announce Plans, Senate Rules, Rules Reform, Legislative Pay, Ethics

New York Assembly: Pass Bills, Introduce Bills, Announce Plans, Senate Rule Rules Reform, Legislative Pay, Ethics

New York Politics: New York Elections, Electoral Campaigns, Electoral Politics, Polls, Congressional Races and Elections, Campaign Finance, Redistricting, Reapportionment, Debates, Democratic Party Nominations, Republican Party Nominations, Working Families Party Nominations and Officers, Conservative Party Nominations, County Elections, City Elections, Election Law, Voting Machinges, Voting Rights, Board of Elections, Judicial Elections

New York Issues: Budgets, State Governments, Local Governments, State Budget, County Budgets, City Budgets, County Legislature, Census, New York State population, Population shifts,Civil Rights,Gay, Lesbian, Bisexual, Transgender, Gay Marriage, Bullying, Discrimination, Minority Owned Businesses, Immigrant Issues, Day labor centers, Immigration, Language issues, Womens Issues, Reproductive Rights, Police Abuses, Disabilities, Native American, Native Tribe, First Amendment, Legal Issues, Consumer Issues, New York State Department of Consumer Affairs, New York City Department of Consumer Affairs, Consumer frauds, Food recalls, Product recalls, Crime and Public Protection, Fire Department layoffs, Police corruption, Police hires, Police layoffs, Police Shooting, Drugs, Gangs, State or Local Criminal Statistics, Prisons, Domestic Violence, Terrorism, Economy, Economic Development, Empire State Development Corporation, Employment and Unemployment statistics, Factory Openings and Closings, Federal Stimulus, Job Layoffs, Job Hires, New York State Agriculture Department, Wall Street Jobs, Upstate Econony, Education, Class Overcrowding, Charter Schools, Class Size, Failing Schools, New York State Education Department, New Curricula, No Child Left Behind, Teacher Unions, United Federation of Teachers, Randi Weingarten, National Education Association, Testing scores, Referendum to approve budgets, Universities, CUNY, SUNY, Community Colleges, Libraries, Energy and Telecommunications, Cable Rates, Cable Service, Electricity Costs, NYSERDA, Public Service Commission, Power Authorities, Power Plants, Phone Rates, Environment, Conservation, Dumping, New York Department of Environmental Conservation, Pollution, Water  and Sewer , Parks, Nuclear, Indian Point, Government Reform, Bribery, Corruption, Consolidation, Ethics, Health Care and Medicaid, New York State Department of Health, Office of Medicaid Inspector General, Medicaid Fraud Control Unit, Medicaid Fraud, Health Insurance, HMOs, Medicaid Services, Medical Malpractice, Tort Reform, Hospitals and Nursing Homes, Mental Health, Food Poisoning, Food safety, Drug Prices, Housing, Mortgage Foreclosures, House Flipping, Predatory Lending, Rent Control, Public Housing, Homeless, Judiciary, Criminal and Civil Justice, Judiciary, Courts, Judicial Pay Raise, Chief Judget Jonathon Lippman, Labor, New York State Department of Labor, Contract Announcements, Civil Service, Pensions, Negotiations, Strikes, Labor Unions, Labor Union Governance, Union elections, Wage and Hour Issues, Minimum Wage, Workplace Safety, Racing, Gaming and Wagering, NYRA, Off Track Betting, Proposed Casinos, Religion, Church Governance, Controversies, Prayer in Schools, Transportaion, Bridges, Infrastructure, Local Public Transportation, New Projects, New York State Department of Transportation, MTA, Straphangars, Roads, Transit Authorities, Tunnels, Social Services, Children and Family Services, Discrimination, Welfare, Welfare to Work, Veterans, Homeland Security, Military Affairs, New York State Department of Veterans Affairs, New Yorkers in Iraq, Afghanistan, West Point, New York Reserves, New York Air Force, New York Naval Bases

National and International: President Obama,United States Congress,Passes Bill,Passes Budget,United States Senate,Chuck Schumer,Kirsten Gillibrand,Congressional Action, National Economy, Iraq, Israel, Afghanistan

 -->
