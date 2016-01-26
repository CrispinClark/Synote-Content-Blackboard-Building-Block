<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="bbNG" uri="/bbNG"%>

<%@ page import="gdp18.synote.SynoteContentData"%>
<%@ page import="gdp18.synote.Utils"%>
<%@ page import="gdp18.synote.SynoteContentItem" %>
<%@ page import="java.util.*" %>

<%@ taglib prefix="bbNG" uri="/bbNG"%>


<bbNG:learningSystemPage ctxId="ctx" >

<%
	final String page_title = "Synote Content for " + ctx.getCourse().getDisplayTitle();
%>	
	<bbNG:pageHeader instructions="The Synote content available for this course.">
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb><%= page_title %></bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar ><%= page_title %></bbNG:pageTitleBar>
	</bbNG:pageHeader>
<% 
	//Passed from Blackboard.
	String course_id = request.getParameter("course_id");
	String courseFoldersURL = Utils.courseFoldersScriptURL + "?course_id=" + course_id;
	String configureCourseURL = Utils.configureCourseScriptURL;	
	
	SynoteContentData synoteContentData = new SynoteContentData(ctx);
	boolean userMayConfig = synoteContentData.userMayConfig();
	boolean configured = synoteContentData.getContentItemsFromServer();
	List<SynoteContentItem> contentItems;	
	
	String username = synoteContentData.getBbUserName();
	String courseCode = synoteContentData.getCourseCode();
	String courseTitle = synoteContentData.getCourseName();
	String courseDescription = synoteContentData.getCourseDescription();
	
	Comparator<SynoteContentItem> itemSortByName = new Comparator<SynoteContentItem>(){
		public int compare(SynoteContentItem o1, SynoteContentItem o2){
			String s1 = o1.getName();
			String s2 = o2.getName();
			
			return s1.toLowerCase().compareTo(s2.toLowerCase());
		}
	};
	
	Comparator<SynoteContentItem> itemSortByDate = new Comparator<SynoteContentItem>(){
		public int compare(SynoteContentItem o1, SynoteContentItem o2){
			Date d1 = o1.getStartTime();
			Date d2 = o2.getStartTime();
			
			return d1.compareTo(d2);
		}
	};
	
	if (!configured){
		%>
		<div>
			This course is not currently configured for use with Synote.
		</div>
		<%
		if (userMayConfig){
			%>
			<div>
				<bbNG:form name="configForm" id="configForm" action="<%= configureCourseURL %>"> 
					<input type="hidden" id="config_course_username" name="config_course_username" value="<%= username %>">
					<input type="hidden" id="config_course_id" name="config_course_id" value="<%= courseCode %>">
					<input type="hidden" id="config_course_name" name="config_course_name" value="<%= courseTitle %>">
					<input type="hidden" id="config_course_desc" name="config_course_desc" value="<%= courseDescription %>">
					<bbNG:button id="Config Button" label="Configure Course" url="javascript:document.configForm.submit()" />
				</bbNG:form>
			</div>
			<% 
		}
	}
	else{
		contentItems = synoteContentData.getItems();
		%>
		<bbNG:inventoryList
			description="<%= page_title %>"
			collection="<%= contentItems %>"
			objectVar="itemList"
			className="SynoteContentItem"
			initialSortBy="ASCENDING"
			initialSortCol="Date"
			>
			<bbNG:listElement name="listHeader" isRowHeader="true"/>
			<bbNG:listElement 
				name="Thumbnail">
				<img src="<%= itemList.getThumbUrl() %>"
					 height="100"
					 width="100"><br>
			</bbNG:listElement>
			<bbNG:listElement
	 			label="Name"
	 			name="Name"
	 			comparator="<%= itemSortByName %>">
	 			<div id="item">
	 				<h2><a href="<%= itemList.getSynoteURL() %>">
	 					<%= itemList.getName() %>
	 				</a></h2>
	 				<p>
	 					<%= itemList.getDescription().equals(null) ? "No description available" : itemList.getDescription() %>
	 				</p><br>
	 				<div>
	 					Created by <%= itemList.getCreatorUsername() %>
	 				</div>
	 			</div>
	 		</bbNG:listElement>
	 		<bbNG:listElement
	 			name="Date"
	 			label="Date"
	 			comparator="<%= itemSortByDate %>">
	 			<%= itemList.getStartTimeString() %>
	 		</bbNG:listElement>
		</bbNG:inventoryList>
		<%
		if(userMayConfig){ 
			%>
	       	<div id="confButtons" class="configureButtonBox">
	         	<div class="configureText">
	         		You can update the folders that are mapped to this course.
	         	</div>
	         	<div class="configureButtons">
	            	<bbNG:button id="EditFolders" label="Edit Folders" url="<%=courseFoldersURL%>"/>
	         	</div>
	         </div>
		   	<% 	
	   	}
	}
	%>     		
</bbNG:learningSystemPage>

