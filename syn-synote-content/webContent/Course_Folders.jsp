<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ page import="java.util.*" %>

<%@ page import="gdp18.synote.SynoteCourseFolderData"%>
<%@ page import="gdp18.synote.SynoteFolder"%>
<%@ page import="gdp18.synote.Utils"%>

<%@ taglib prefix="bbNG" uri="/bbNG"%>

<bbNG:learningSystemPage ctxId="ctx" >
<%
	final String page_title = "Current Synote Folders for " + ctx.getCourse().getDisplayTitle();
%>
	<bbNG:pageHeader instructions="The Panopto folders linked to this course. Synote recordings will be automatically generated from videos in these folders.">
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb><%=page_title%></bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar ><%=page_title%></bbNG:pageTitleBar>
	</bbNG:pageHeader>

<%
	//Passed from Blackboard.
	String course_id = request.getParameter("course_id");

	SynoteCourseFolderData courseFolderData = new SynoteCourseFolderData(ctx);
	String username = courseFolderData.getBbUserName();
	String courseCode = courseFolderData.getCourseCode();
	String courseTitle = courseFolderData.getCourseName();
	String courseDescription = courseFolderData.getCourseDescription();
	
	String parentPageTitle = "Synote Content";
	String addFoldersURL = Utils.addCourseFoldersScriptURL + "?course_id=" + course_id;
	String mapFoldersURL = Utils.mapFoldersURL + "?course_id=" + courseCode;
	String configureCourseURL = Utils.configureCourseScriptURL;
	
	boolean configured = courseFolderData.getMappedFoldersFromServer();
	List<SynoteFolder> mappedFolders;
	
	Comparator<SynoteFolder> folderSortByName = new Comparator<SynoteFolder>(){
		public int compare(SynoteFolder o1, SynoteFolder o2){
			String s1 = o1.getName();
			String s2 = o2.getName();
			
			return s1.toLowerCase().compareTo(s2.toLowerCase());
		}
	};
	
	// First check if the caller is allowed to be here
	if (!courseFolderData.userMayConfig()){
		%>
		You do not have access to configure this course.
		<%
	}
	else if (!configured){
		%>
		This Blackboard course is not configured for use with Synote. Would you like to configure it now?
		<bbNG:form name="configForm" id="configForm" action="<%= configureCourseURL %>"> 
			<input type="hidden" id="config_course_username" name="config_course_username" value="<%= username %>">
			<input type="hidden" id="config_course_id" name="config_course_id" value="<%= courseCode %>">
			<input type="hidden" id="config_course_name" name="config_course_name" value="<%= courseTitle %>">
			<input type="hidden" id="config_course_desc" name="config_course_desc" value="<%= courseDescription %>">
			<div>
				<bbNG:button id="Config Button" label="Configure Course" url="javascript:document.configForm.submit()" />
			</div>
		</bbNG:form>
		<%
	}
	else{
		mappedFolders = courseFolderData.getMappedFolders();
		
		if (mappedFolders == null || mappedFolders.size() == 0){
		%>
		There are no folders currently mapped to this course.
		<%
		}
		
		else if (mappedFolders.size() > 0){
		%>
		<bbNG:form name="mapForm" id="mapForm" action="<%= mapFoldersURL %>">
			<input type="hidden" id="mapValue" name="mapValue" value="DELETE">
			<input type="hidden" id="username" name="username" value="<%=username %>">
			<input type="hidden" id="course_id" name="course_id" value="<%= courseCode %>">
			<bbNG:inventoryList
				description="Mapped folders"
				collection="<%= mappedFolders %>"
				objectVar="mappedList"
				className="SynoteFolder"
				>
				<bbNG:listActionBar>
					<bbNG:listActionItem 
						id="unmapAction" 
						url="javascript:document.mapForm.submit()" 
						title="Unmap" />
				</bbNG:listActionBar>
				<bbNG:listCheckboxElement name="ckbox" id="ckbox" value="<%= mappedList.getPanoptoFolderId()%>"/>
				<bbNG:listElement name="listHeader" isRowHeader="true">
				</bbNG:listElement>
				<bbNG:listElement
		 			label="Name"
		 			name="Name"
		 			comparator="<%= folderSortByName %>">
		 			<%= mappedList.getName()%>
		 		</bbNG:listElement>
		 		<bbNG:listElement
					 label="Description"
					 name="Description">
					 <%= mappedList.getDescription() %>
				</bbNG:listElement>
			</bbNG:inventoryList>
		</bbNG:form>
		<%}%>
		<div>
			<bbNG:button id="Add folders Button" label="Add Folders" url="<%= addFoldersURL %>" />
	 	</div>
	 	<%
 	}
	%>
 	<bbNG:okButton/>
 </bbNG:learningSystemPage>