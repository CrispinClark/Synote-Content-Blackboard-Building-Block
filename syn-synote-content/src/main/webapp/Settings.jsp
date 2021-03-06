<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@page import="gdp18.synote.control.Utils" %>

<%@ taglib prefix="bbNG" uri="/bbNG"%>

<bbNG:genericPage ctxId="ctx" >

	<%
		String page_title = "Synote Content Settings";
	
		String synoteURL = request.getParameter("synoteURL");
		String sharedKey = request.getParameter("sharedKey");
		String expirySeconds = request.getParameter("expirySeconds");

		if (synoteURL != null){
			while(synoteURL.endsWith("/")){
				synoteURL = synoteURL.substring(0, synoteURL.length()-1);
			}
			if(!synoteURL.trim().equals("") && Utils.isValidXML(synoteURL.trim())){
		        Utils.pluginSettings.setSynoteURL(synoteURL.trim());
		    }
		}
		
		if (sharedKey != null){
			if(!sharedKey.trim().equals("") && Utils.isValidXML(sharedKey.trim())){
		        Utils.pluginSettings.setSharedKey(sharedKey.trim());
		    }
		}
		
		if (expirySeconds != null){
			if (Utils.isInteger(expirySeconds)){
				int seconds = Integer.parseInt(expirySeconds);
				Utils.pluginSettings.setJWTExpirySeconds(seconds);
			}
		}
		
		synoteURL = Utils.pluginSettings.getSynoteURL();
		sharedKey = Utils.pluginSettings.getSharedKey();
		expirySeconds = String.valueOf(Utils.pluginSettings.getJWTExpirySeconds());
	%>
	<bbNG:pageHeader instructions="The settings for communication with the Synote server">
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb><%= page_title %></bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar ><%= page_title %></bbNG:pageTitleBar>
	</bbNG:pageHeader>
	
	<bbNG:form action="Settings.jsp" method="post" onSubmit="return validateForm();">	
		<bbNG:dataCollection>
			<bbNG:step title="Settings">
				<bbNG:dataElement label="Synote URL" isRequired="true" labelFor="Synote URL">
					<bbNG:textElement name="synoteURL" value="<%= synoteURL %>" helpText="The URL for your Synote server"/>
				</bbNG:dataElement>
				<bbNG:dataElement label="Shared Key" isRequired="true" labelFor="Shared Key">
					<bbNG:textElement name="sharedKey" value="<%= sharedKey %>" helpText="The shared key to use for authenticating requests to and from the Synote server. "/>
				</bbNG:dataElement>
				<bbNG:dataElement label="JWT expiry seconds" isRequired="true" labelFor="JWT Expiry Seconds">
					<bbNG:textElement name="expirySeconds" value="<%= expirySeconds %>" helpText="The number of seconds a JWT generated by this building block is valid for. "/>
				</bbNG:dataElement>
			</bbNG:step>
			<bbNG:stepSubmit/>
		</bbNG:dataCollection>
	</bbNG:form>
</bbNG:genericPage>