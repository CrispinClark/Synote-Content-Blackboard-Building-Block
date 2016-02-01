package gdp18.synote.control;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import blackboard.platform.context.Context;
import gdp18.synote.model.SynoteContentData;
import gdp18.synote.model.SynoteContentItem;

public class SynoteContentGetter extends SynoteGetter{

	private SynoteContentData data = new SynoteContentData();
	
	public SynoteContentGetter(Context ctx) {
		super(ctx);
	}

    public boolean getContentItemsFromServer() throws Exception{
    	String json = requestContentJsonFromServer();
    	try{
    		data.parseContentJson(json);
    	}
    	catch(JSONException ex){
    		throw new JSONException("Error in JSON received from server.");
    	}
    	
		return isCourseConfigured();
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
    
    public List<SynoteContentItem> getItems(){
    	return data.getItems();
    }
    
    public boolean isCourseConfigured(){
    	return data.isCourseConfigured();
    }
}
