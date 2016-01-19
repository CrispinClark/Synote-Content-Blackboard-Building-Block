package gdp18.synote;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import blackboard.platform.context.Context;

public class SynoteContentData extends SynoteData{

	private List<SynoteContentItem> contentItems;
	
	public SynoteContentData(Context ctx) {
		super(ctx);
		contentItems = new ArrayList<>();
	}

    public boolean getContentItemsFromServer() throws Exception{
    	String json = requestContentJsonFromServer();
    	if (!isCourseConfigured(json)){
			return false;
		}
    	
		contentItems = parseContentJson(json);
		return true;
    }

    public String requestContentJsonFromServer() throws Exception{
    	String courseId = bbCourse.getBatchUid();
    	String jwt = getJWT();
    	String url = Utils.pluginSettings.getSynoteURL() 
    			+ Utils.getCourseContentURL(courseId)
    			+ "?token="
    			+ jwt;
    	
    	return sendGetRequest(url);
    }
    
    private List<SynoteContentItem> parseContentJson(String json) throws Exception{
    	List<SynoteContentItem> newList = new ArrayList<>();
    	
    	JSONObject obj = new JSONObject(json);
    	
    	if (obj.optJSONArray("recordings") != null){
    		JSONArray recordings = obj.getJSONArray("recordings");
    		for (int i = 0; i < recordings.length(); i++){
	    		JSONObject itemJSON = recordings.getJSONObject(i);
	    		SynoteContentItem item = new SynoteContentItem();
	    		
	    		item.setId(itemJSON.getInt("id"));
	    		
	    		if (itemJSON.optString("inputId") != null){
	    			item.setInputId(itemJSON.getString("inputId"));
	    		}
	    		if (itemJSON.optString("name") != null){
	    			item.setName(itemJSON.getString("name"));
	    		}
	    		if (itemJSON.optString("description") != null){
	    			item.setDescription(itemJSON.getString("description"));
	    		}
	    		if (itemJSON.optString("creatorId") != null){
	    			item.setCreatorId(itemJSON.getString("creatorId"));
	    		}
	    		if (itemJSON.optString("mp3") != null){
	    			item.setMp3URL(itemJSON.getString("mp3"));
	    		}
	    		if (itemJSON.optString("mp4") != null){
	    			item.setMp4URL(itemJSON.getString("mp4"));
	    		}
	    		if (itemJSON.optString("startTime") != null){
	    			item.setStartTime(itemJSON.getString("startTime"));
	    		}
	    		if (itemJSON.optString("duration") != null){
	    			item.setDuration(itemJSON.getString("duration"));
	    		}
	    		if (itemJSON.optString("thumbUrl") != null){
	    			item.setThumbUrl(itemJSON.getString("thumbUrl"));
	    		}
	    		if (itemJSON.optString("synoteURL") != null){
	    			item.setSynoteURL(itemJSON.getString("synoteURL"));
	    		}
	    		item.setComplete(itemJSON.getBoolean("complete"));
	    		
	    		newList.add(item);
	    	}
    	}
    	
    	return newList;
    }
    
    public List<SynoteContentItem> getItems(){
    	return this.contentItems;
    }
}
