<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib uri="/bbNG" prefix="bbNG"%>

<%@ page import="gdp18.synote.Utils" %>
<%@ page import="gdp18.synote.PostRequester" %>

<%@ page import="java.io.OutputStreamWriter"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.net.URLEncoder"%>

<%@ page import="org.json.JSONObject"%> 
  
<bbNG:learningSystemPage ctxId="ctx">
<%	
	String username = request.getParameter("config_course_username");	
	String course_id = request.getParameter("config_course_id");
	String course_name = request.getParameter("config_course_name");
	String course_desc = request.getParameter("config_course_desc");
	
	String synoteURL = Utils.pluginSettings.getSynoteURL();
	String configureCourseURL = Utils.getConfigureCourseURL(course_id);
	
	String url = synoteURL
			+ configureCourseURL
			+ "?token="
			+ Utils.generateRequestJWT(username, course_id, "creator");
	
	JSONObject json = new JSONObject();
	json.put("outputId", course_id);
	json.put("name", course_name);
	json.put("description", course_desc);
	String valuesJSON = json.toString();
	
	int x = PostRequester.sendRequest("POST", url, valuesJSON);  
	
	if (x == 200){
	%>
		Course successfully configured.	
	<%
	}
	else{
	%>
		There was an error configuring the course. The error code is <%=x%>. JSON = <%= valuesJSON %>. Target url = <%= url %> 
	<%
	}
%>
	<bbNG:okButton/>
</bbNG:learningSystemPage>