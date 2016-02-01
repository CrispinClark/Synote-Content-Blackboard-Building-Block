<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ page import="java.util.*" %>

<%@ page import="gdp18.synote.control.SynoteCourseFolderGetter"%>
<%@ page import="gdp18.synote.model.SynoteFolder"%>
<%@ page import="gdp18.synote.control.Utils"%>

<%@ taglib prefix="bbNG" uri="/bbNG"%>

<bbNG:learningSystemPage ctxId="ctx" >
<%
	final String page_title = "Add Synote Folders to " + ctx.getCourse().getDisplayTitle();
		
	//Passed from Blackboard.
	String course_id = request.getParameter("course_id");

	SynoteCourseFolderGetter courseFolderGetter = new SynoteCourseFolderGetter(ctx);
	String username = courseFolderGetter.getBbUserName();
	String courseCode = courseFolderGetter.getCourseCode();
	
	String mapFoldersURL = Utils.mapFoldersURL + "?course_id=" + courseCode;
	
	List<SynoteFolder> availableFolders;

	courseFolderGetter.getSuggestedFoldersFromServer();
	availableFolders = courseFolderGetter.getAllUnmappedFolders();
	
	Comparator<SynoteFolder> folderSortByName = new Comparator<SynoteFolder>(){
		public int compare(SynoteFolder o1, SynoteFolder o2){
			String s1 = o1.getName();
			String s2 = o2.getName();
			
			return s1.toLowerCase().compareTo(s2.toLowerCase());
		}
	};
	
	Comparator<SynoteFolder> folderSortBySuggested = new Comparator<SynoteFolder>(){
		public int compare(SynoteFolder o1, SynoteFolder o2){
			boolean b1 = o1.isSuggested();
			boolean b2 = o2.isSuggested();
			
			return Boolean.compare(b1, b2);
		}
	};
	%>
	<bbNG:pageHeader instructions="Select folders to link to this course. Synote recordings will be automatically generated from videos in these folders.">
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb><%=page_title%></bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar ><%=page_title%></bbNG:pageTitleBar>
	</bbNG:pageHeader>
	<%
	// First check if the caller is allowed to be here
	if (!courseFolderGetter.userMayConfig()){
		%>
		You do not have access to configure this course.
		<%
	} 
	else if (availableFolders == null){
		%>
		Error retrieving folders.
		<%
	}
	else if (availableFolders.size() == 0){
		%>
		No available Panopto folders were found.
		<%
	}
	else{
		if (availableFolders.size() > 0){
		%>
		<bbNG:form name="mapForm" id="mapForm" action="<%= mapFoldersURL %>">
			<input type="hidden" id="mapValue" name="mapValue" value="POST">
			<input type="hidden" id="username" name="username" value="<%=username %>">
			<input type="hidden" id="course_id" name="course_id" value="<%= courseCode %>">
			<bbNG:inventoryList
				description="Available folders"
				collection="<%= availableFolders %>"
				objectVar="availableList"
				className="SynoteFolder"
				initialSortCol="Suggested"
				initialSortBy="DESCENDING"
				>
				<bbNG:listActionBar>
					<bbNG:listActionItem 
						id="mapAction" 
						url="javascript:document.mapForm.submit()" 
						title="Map"/>
				</bbNG:listActionBar>
				<bbNG:listCheckboxElement name="ckbox" id="ckbox" value="<%= availableList.getPanoptoFolderId()%>"/>
				<bbNG:listElement name="listHeader" isRowHeader="true">
				</bbNG:listElement>
				<bbNG:listElement
		 			label="Name"
		 			name="Name"
		 			comparator="<%= folderSortByName %>">
		 			<%= availableList.getName()%>
		 		</bbNG:listElement>
		 		<bbNG:listElement
					 label="Description"
					 name="Description">
					 <%= availableList.getDescription() %>
				</bbNG:listElement>
				<bbNG:listElement
					 label="Suggested?"
					 name="Suggested"
					 comparator="<%= folderSortBySuggested %>">
					 <%= availableList.isSuggested() ? "yes" : "no" %>
				</bbNG:listElement>
			</bbNG:inventoryList>
		</bbNG:form>
		<%
		} 
	}
	%>
	<bbNG:okButton/>
</bbNG:learningSystemPage>