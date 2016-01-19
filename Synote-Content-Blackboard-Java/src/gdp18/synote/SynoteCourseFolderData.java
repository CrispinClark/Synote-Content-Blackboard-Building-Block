package gdp18.synote;

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

public class SynoteCourseFolderData extends SynoteData{

	private HashMap<String, SynoteFolder> folders;
	
	public SynoteCourseFolderData(Context ctx) {
		super(ctx);
		folders = new HashMap<>();
	}

	public SynoteCourseFolderData(String course, String user){
		super(course, user);
		folders=new HashMap<>();
	}

    public String convertArrayToCollectionsJSON(String[] values) throws JSONException{
    	JSONObject json = new JSONObject();
		json.put("collections", values);
		return json.toString();
    }
    
	public boolean getMappedFoldersFromServer() throws Exception{
		String json = requestMappedFoldersJsonFromServer();
		if (!isCourseConfigured(json)){
			return false;
		}
		
		folders = parseMappedFoldersJson(json);
		return true;
	}
	
    public String requestMappedFoldersJsonFromServer() throws Exception{
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
		folders = parseSuggestedFoldersJson(json);
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
    
    private HashMap<String, SynoteFolder> parseMappedFoldersJson(String mappedJsonString) 
    		throws JSONException{
    	HashMap<String, SynoteFolder> newFolders = new HashMap<>();
    	JSONObject obj = new JSONObject(mappedJsonString);
    	if (obj.optJSONArray("mappings") != null){
    		JSONArray mappings = obj.getJSONArray("mappings");
    		for (int i = 0; i < mappings.length(); i++){
	    		JSONObject folderJson = mappings.getJSONObject(i);
	    		String inputId = folderJson.getString("inputId");
	    		String name = folderJson.getString("name");
	    		String description = folderJson.getString("description");
	    		
	    		newFolders.put(name, new SynoteFolder(name, description, inputId, true));
	    	}
    	}
    	return newFolders;
    }
    
    private HashMap<String, SynoteFolder> parseSuggestedFoldersJson(String suggestedJsonString) 
    		throws JSONException{
    	HashMap<String, SynoteFolder> newFolders = new HashMap<>();
    	
    	JSONObject obj = new JSONObject(suggestedJsonString);
    	
    	if (obj.optJSONArray("collections") != null){
    		JSONArray collections = obj.getJSONArray("collections");
    		for (int i = 0; i < collections.length(); i++){
	    		JSONObject folderJson = collections.getJSONObject(i);
	    		String inputId = folderJson.getString("inputId");
	    		String name = folderJson.getString("name");
	    		String description = folderJson.getString("description");
	    		
	    		newFolders.put(name, new SynoteFolder(name, description, inputId));
	    	}
    	}
    
    	if (obj.optJSONArray("suggested") != null){
    		JSONArray suggested = obj.getJSONArray("suggested");
    		for (int i = 0; i < suggested.length(); i++){
	    		JSONObject folderJson = suggested.getJSONObject(i);
	    		String name = folderJson.getString("name");
	    		if (!newFolders.containsKey(name)){
	    			String inputId = folderJson.getString("inputId");
	    			String description = folderJson.getString("description");
	    			
	    			newFolders.put(name, new SynoteFolder(name, description, inputId));
	    		}
	    		
	    		newFolders.get(name).setSuggested(true);
	    	}
    	}
    	
    	if (obj.optJSONArray("current") != null){
    		JSONArray current = obj.getJSONArray("current");
    		for (int i = 0; i < current.length(); i++){
	    		JSONObject folderJson = current.getJSONObject(i);
	    		String name = folderJson.getString("name");
	    		if (!newFolders.containsKey(name)){
	    			String inputId = folderJson.getString("inputId");
	    			String description = folderJson.getString("description");
	    			
	    			newFolders.put(name, new SynoteFolder(name, description, inputId));
	    		}
	    		
	    		newFolders.get(name).setMapped(true);
	    	}
    	}
    	
    	if (obj.optJSONArray("other") != null){
    		JSONArray other = obj.getJSONArray("other");
    		for (int i = 0; i < other.length(); i++){
	    		JSONObject folderJson = other.getJSONObject(i);
	    		String name = folderJson.getString("name");
	    		if (!newFolders.containsKey(name)){
	    			String inputId = folderJson.getString("inputId");
	    			String description = folderJson.getString("description");
	    			
	    			newFolders.put(name, new SynoteFolder(name, description, inputId));
	    		}
	    		
	    		newFolders.get(name).setOther(true);
	    	}
    	}    	
    	return newFolders;
    }

    public ArrayList<SynoteFolder> getMappedFolders(){
    	ArrayList<SynoteFolder> mappedFolders = new ArrayList<>();
    	for (SynoteFolder folder : folders.values()){
    		if (folder.isMapped()){
    			mappedFolders.add(folder);
    		}
    	}
    	return mappedFolders;
    }
    
    public ArrayList<SynoteFolder> getSuggestedFolders(){
    	ArrayList<SynoteFolder> suggestedFolders = new ArrayList<>();
    	for (SynoteFolder folder : folders.values()){
    		if (folder.isSuggested()){
    			suggestedFolders.add(folder);
    		}
    	}
    	return suggestedFolders;
    }
    
    public ArrayList<SynoteFolder> getOtherFolders(){
    	ArrayList<SynoteFolder> otherFolders = new ArrayList<>();
    	for (SynoteFolder folder : folders.values()){
    		if (folder.isOther()){
    			otherFolders.add(folder);
    		}
    	}
    	return otherFolders;
    }
    
    public ArrayList<SynoteFolder> getAllUnmappedFolders(){
    	ArrayList<SynoteFolder> allFolders = new ArrayList<>();
    	for (SynoteFolder folder : folders.values()){
    		if (folder.isSuggested() | folder.isOther()){
    			allFolders.add(folder);
    		}
    	}
    	return allFolders;
    }
}
