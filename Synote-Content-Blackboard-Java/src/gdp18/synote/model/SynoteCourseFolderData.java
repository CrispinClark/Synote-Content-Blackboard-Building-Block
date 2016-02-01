package gdp18.synote.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SynoteCourseFolderData {
	private HashMap<String, SynoteFolder> folders;
	private boolean isConfigured;
	
	public SynoteCourseFolderData(){
		folders = new HashMap<>();
	}

    public void parseMappedFoldersJson(String mappedJsonString) 
    		throws JSONException{
    	HashMap<String, SynoteFolder> newFolders = new HashMap<>();
    	JSONObject obj = new JSONObject(mappedJsonString);
    	isConfigured = obj.getBoolean("configured");
    	if (!isConfigured){
    		return;
    	}
    	
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
    	this.folders = newFolders;
    }
    

    public void parseSuggestedFoldersJson(String suggestedJsonString) 
    		throws JSONException{
    	HashMap<String, SynoteFolder> newFolders = new HashMap<>();
    	
    	JSONObject obj = new JSONObject(suggestedJsonString);
    	isConfigured = obj.getBoolean("configured");
    	if (!isConfigured){
    		return;
    	}
    	
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
    	folders = newFolders;
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
    
    public boolean isCourseConfigured(){
    	return this.isConfigured;
    }
}
