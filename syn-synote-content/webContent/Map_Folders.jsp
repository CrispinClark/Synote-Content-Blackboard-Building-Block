<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib uri="/bbNG" prefix="bbNG"%>

<%@ page import="gdp18.synote.Utils" %>

<%@ page import="java.io.OutputStreamWriter"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.net.URLEncoder"%>

<%@ page import="org.json.JSONObject"%> 
  
<bbNG:learningSystemPage ctxId="ctx">
<%	
	String username = request.getParameter("username");
	String course_id = request.getParameter("course_id");
	String[] checked = request.getParameterValues("ckbox");
	
	JSONObject json = new JSONObject();
	json.put("collection", checked);
	String valuesJSON = json.toString();
	
	if (request.getParameter("mapValue").equals("null")){
		%>
		The type of request was not set correctly.
		<bbNG:okButton/>
		<%
		return;
	}
	
	String requestType = request.getParameter("mapValue");
	Boolean isPost = requestType.equals("POST");
	
	String synoteURL = Utils.pluginSettings.getSynoteURL();
	String mapFoldersURL = Utils.getCourseMappingsURL(course_id);
	
	String url = synoteURL
			+ mapFoldersURL
			+ "?token="
			+ Utils.generateRequestJWT(username, course_id, "creator");
	
	URL obj = new URL(url);
	
	System.out.println(requestType);
	
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	con.setRequestMethod(requestType);
	con.setRequestProperty("Content-Type", "application/json");
	con.setRequestProperty("Accept", "application/json");
	con.setDoOutput(true);
	con.setDoInput(true);
	
	OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
	wr.write(valuesJSON);
	wr.flush();
	
	int x = con.getResponseCode();  
	
	if (x == 200){
	%>
		Folders successfully <%= isPost? "mapped" : "removed" %>.	
	<%
	}
	else{
	%>
		The was an error mapping the folders. The error code is <%=x%>.
	<%
	}
%>
	<bbNG:okButton/>
</bbNG:learningSystemPage>