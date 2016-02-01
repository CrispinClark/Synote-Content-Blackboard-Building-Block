package gdp18.synote.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import blackboard.platform.context.Context;
import gdp18.synote.model.SynoteCourseFolderData;
import gdp18.synote.model.SynoteFolder;

public class SynoteCourseFolderGetter extends SynoteGetter{

	private SynoteCourseFolderData data = new SynoteCourseFolderData();
	
	public SynoteCourseFolderGetter(Context ctx) {
		super(ctx);
	}

	public SynoteCourseFolderGetter(String course, String user){
		super(course, user);
	}

	public boolean getMappedFoldersFromServer() throws Exception{
		String json = requestMappedFoldersJsonFromServer();
		try{
    		data.parseMappedFoldersJson(json);
    	}
    	catch(JSONException ex){
    		throw new JSONException("Error in JSON received from server.");
    	}
		
		return isCourseConfigured();
	}
	
    private String requestMappedFoldersJsonFromServer() throws Exception{
    	String courseId = bbCourse.getBatchUid();
    	String jwt = getJWT();
    	String url = Utils.pluginSettings.getSynoteURL() 
    			+ Utils.getCourseMappingsURL(courseId)
    			+ "?token="
    			+ jwt;
    	
    	return sendGetRequest(url);
    }
    
	public void getSuggestedFoldersFromServer() throws Exception{
		String json = requestSuggestedFoldersJsonFromServer();
		try{
    		data.parseSuggestedFoldersJson(json);
    	}
    	catch(JSONException ex){
    		throw new JSONException("Error in JSON received from server.");
    	}
	}
	
    public String requestSuggestedFoldersJsonFromServer() throws Exception{
    	String courseId = bbCourse.getBatchUid();
    	String jwt = getJWT();
    	String url = Utils.pluginSettings.getSynoteURL() 
    			+ Utils.getSuggestedFoldersURL(courseId)
    			+ "?token="
    			+ jwt;
    	
    	return sendGetRequest(url);
    }
    
    public ArrayList<SynoteFolder> getMappedFolders(){
    	return data.getMappedFolders();
    }
    
    public ArrayList<SynoteFolder> getSuggestedFolders(){
    	return data.getSuggestedFolders();
    }
    
    public ArrayList<SynoteFolder> getOtherFolders(){
    	return data.getOtherFolders();
    }
    
    public ArrayList<SynoteFolder> getAllUnmappedFolders(){
    	return data.getAllUnmappedFolders();
    }
    
    public boolean isCourseConfigured(){
    	return data.isCourseConfigured();
    }
}
