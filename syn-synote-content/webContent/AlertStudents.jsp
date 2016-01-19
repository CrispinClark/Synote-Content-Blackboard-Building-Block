<%@page language="java" pageEncoding="ISO-8859-1" %>

<%@page import="gdp18.synote.Utils" %>
<%@taglib uri="/bbData" prefix="bbData"%>

<bbData:context id="ctx">
<% 
	response.setContentType("text/json");
	response.getWriter().write(Utils.getJsonFromHttpRequest(request));
%>
HELLO
</bbData:context>