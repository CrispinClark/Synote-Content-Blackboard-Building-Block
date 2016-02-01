package gdp18.synote.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SynoteContentData {
	private List<SynoteContentItem> contentItems;
	private boolean isConfigured;
	
	public SynoteContentData(){
		this.contentItems = new ArrayList<>();
	}
	
	public void parseContentJson(String json) throws JSONException, ParseException{
    	List<SynoteContentItem> newList = new ArrayList<>();
    	
    	JSONObject obj = new JSONObject(json);
    	isConfigured = obj.getBoolean("configured");
    	
    	if (!isConfigured){
    		return;
    	}
    	
    	if (obj.optJSONArray("recordings") != null){
    		JSONArray recordings = obj.getJSONArray("recordings");
    		for (int i = 0; i < recordings.length(); i++){
	    		JSONObject itemJSON = recordings.getJSONObject(i);
	    		SynoteContentItem item = new SynoteContentItem();
	    		
	    		item.setId(itemJSON.getInt("id"));
	    		
	    		item.setInputId(itemJSON.getString("inputId"));
	    		item.setName(itemJSON.getString("name"));
	    		item.setDescription(itemJSON.getString("description"));
	    		item.setMp3URL(itemJSON.getString("mp3"));
	    		item.setMp4URL(itemJSON.getString("mp4"));
	    		item.setStartTime(itemJSON.getString("startTime"));
	    		item.setDuration(itemJSON.getString("duration"));
	    		item.setThumbUrl(itemJSON.getString("thumbUrl"));
	    		item.setSynoteURL(itemJSON.getString("synoteURL"));
	    		item.setComplete(itemJSON.getBoolean("complete"));
	    		
	    		newList.add(item);
	    	}
    	}
    	
    	this.contentItems = newList;
    }
    
	public List<SynoteContentItem> getItems(){
    	return this.contentItems;
    }
	
	public boolean isCourseConfigured(){
		return this.isConfigured;
	}
}
