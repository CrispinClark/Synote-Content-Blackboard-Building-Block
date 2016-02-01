package gdp18.testing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestStringBuilder{
	
	public void generateStrings(){		
		try {
			
			generateUnconfiguredCourseJSON();
			generateConfiguredCourseWith10JSON();
			generateMalformedContent();
			
			generateMappingsForCourse();
			generateUncofiguredMappingsForCourse();
			generateMalformedMappingsJSON();
			
			generateRangeOfFoldersForCourseJSON(); 
			generateUniqueRangeOfFoldersForCourseJSON();
			generateMalformedSuggestedJSON();			
			
			System.out.println("Complete");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void generateUnconfiguredCourseJSON() throws IOException{
		JSONObject json = new JSONObject();
		json.put("configured", false);
		json.put("recordings", (JSONArray) null);
		
		save(TestStringConstants.unconfiguredCourse, json.toString());
	}
	
	private static void generateConfiguredCourseWith10JSON() throws IOException{
		JSONObject json = new JSONObject();
		json.put("configured", true);
		
		ArrayList<JSONObject> recordings = new ArrayList<JSONObject>();
		for (int i = 0; i < 10; i++){
			JSONObject obj = new JSONObject();
			obj.put("id", i);
			obj.put("inputId", "inputID"+i);
			obj.put("name", "name"+i);
			obj.put("description", "description"+i);
			obj.put("mp3", "mp3"+i);
			obj.put("mp4", "mp4"+i);
			obj.put("startTime", "2015-12-29T22:14:46.9411984");
			obj.put("duration", "duration"+i);
			obj.put("thumbUrl", "thumbURL"+i);
			obj.put("complete", true);
			
			JSONObject collection = new JSONObject();
			collection.put("id", "id"+i);
			collection.put("inputId", "inputId"+i);
			collection.put("name", "name"+i);
			obj.put("collection", collection);
			obj.put("synoteURL", "synoteURL"+i);
			
			recordings.add(obj);
		}
		json.put("recordings", recordings);
		
		save(TestStringConstants.configuredCourseWith10, json.toString());
	}
	
	private static void generateMalformedContent() throws IOException{
		JSONObject json = new JSONObject();
		json.put("configured", true);
		
		ArrayList<JSONObject> recordings = new ArrayList<JSONObject>();
		for (int i = 0; i < 10; i++){
			JSONObject obj = new JSONObject();
			obj.put("id", i);
			obj.put("inputId", "inputID"+i);
			obj.put("name", "name"+i);
			obj.put("description", "description"+i);
			obj.put("mp3", "mp3"+i);
			obj.put("mp4", "mp4"+i);
			obj.put("startTime", "2015-12-29T22:14:46.9411984");
			obj.put("duration", "duration"+i);
			obj.put("thumbUrl", "thumbURL"+i);
			
			JSONObject collection = new JSONObject();
			collection.put("id", "id"+i);
			collection.put("inputId", "inputId"+i);
			collection.put("name", "name"+i);
			obj.put("collection", collection);
			obj.put("synoteURL", "synoteURL"+i);
			
			recordings.add(obj);
		}
		json.put("recordings", recordings);
		
		save(TestStringConstants.malformedContent, json.toString());
	}
	
	private static void generateMappingsForCourse() throws IOException{
		JSONArray array = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject obj = new JSONObject();
			obj.put("id", i);
			obj.put("inputId", "inputID"+i);
			obj.put("name", "name"+i);
			obj.put("description", "description"+i);
			array.put(obj);
		}
		
		JSONObject obj = new JSONObject();
		obj.put("configured", true);
		obj.put("mappings", array);
		
		save(TestStringConstants.courseMappings, obj.toString());
	}
	
	private static void generateUncofiguredMappingsForCourse() throws IOException{
		JSONObject obj = new JSONObject();
		obj.put("configured", false);
		obj.put("mappings", (JSONArray) null);
		
		save(TestStringConstants.unconfiguredMappings, obj.toString());	
	}
	
	private static void generateRangeOfFoldersForCourseJSON() throws IOException{
		JSONObject obj = new JSONObject();
		obj.put("configured", true);
		
		JSONArray collections = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject folder = new JSONObject();
			folder.put("inputId", "folder"+i);
			folder.put("name", "name"+i);
			folder.put("description", "desc"+i);
			
			collections.put(folder);
		}
		obj.put("collections", collections);
		
		JSONArray current = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject folder = new JSONObject();
			folder.put("inputId", "folder"+i);
			folder.put("name", "name"+i);
			folder.put("description", "desc"+i);			
		
			current.put(folder);
		}
		obj.put("current", current);
		
		JSONArray suggested = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject folder = new JSONObject();
			folder.put("inputId", "folder"+i);
			folder.put("name", "name"+i);
			folder.put("description", "desc"+i);			
		
			suggested.put(folder);
		}
		obj.put("suggested", suggested);
		
		JSONArray other = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject folder = new JSONObject();
			folder.put("inputId", "folder"+i);
			folder.put("name", "name"+i);
			folder.put("description", "desc"+i);		
			
			other.put(folder);
		}
		obj.put("other", other);
		
		save(TestStringConstants.foldersVariety, obj.toString());
	} 
	
	private static void generateUniqueRangeOfFoldersForCourseJSON() throws IOException{
		JSONObject obj = new JSONObject();
		obj.put("configured", true);
		
		JSONArray collections = new JSONArray();
				
		JSONArray current = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject folder = new JSONObject();
			folder.put("inputId", "currentFolder"+i);
			folder.put("name", "currentName"+i);
			folder.put("description", "currentDesc"+i);			
		
			collections.put(folder);
			current.put(folder);
		}
		
		JSONArray suggested = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject folder = new JSONObject();
			folder.put("inputId", "suggestedFolder"+i);
			folder.put("name", "suggestedName"+i);
			folder.put("description", "suggestedDesc"+i);			
		
			collections.put(folder);
			suggested.put(folder);
		}
		
		JSONArray other = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject folder = new JSONObject();
			folder.put("inputId", "otherFolder"+i);
			folder.put("name", "otherName"+i);
			folder.put("description", "otherDesc"+i);		
			
			collections.put(folder);
			other.put(folder);
		}
		
		obj.put("collections", collections);
		obj.put("current", current);
		obj.put("suggested", suggested);
		obj.put("other", other);
		
		save(TestStringConstants.uniqueFoldersVariety, obj.toString());
	}

	private static void generateMalformedSuggestedJSON() throws IOException {
		JSONObject obj = new JSONObject();
		obj.put("configured", true);
		
		JSONArray collections = new JSONArray();
				
		JSONArray current = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject folder = new JSONObject();
			folder.put("inputId", "currentFolder"+i);
			folder.put("name", "currentName"+i);
			folder.put("description", "currentDesc"+i);			
		
			collections.put(folder);
			current.put(folder);
		}
		
		JSONArray suggested = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject folder = new JSONObject();
			folder.put("inputId", "suggestedFolder"+i);
			folder.put("name", "suggestedName"+i);		
		
			collections.put(folder);
			suggested.put(folder);
		}
		
		JSONArray other = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject folder = new JSONObject();
			folder.put("inputId", "otherFolder"+i);
			folder.put("name", "otherName"+i);
			folder.put("description", "otherDesc"+i);		
			
			collections.put(folder);
			other.put(folder);
		}
		
		obj.put("collections", collections);
		obj.put("current", current);
		obj.put("suggested", suggested);
		obj.put("other", other);
		
		save(TestStringConstants.malformedSuggested, obj.toString());
	}
	
	private void generateMalformedMappingsJSON() throws IOException {
		JSONArray array = new JSONArray();
		for (int i = 0; i < 3; i++){
			JSONObject obj = new JSONObject();
			obj.put("inputId", "inputID"+i);
			obj.put("description", "description"+i);
			array.put(obj);
		}
		
		JSONObject obj = new JSONObject();
		obj.put("configured", true);
		obj.put("mappings", array);
		
		save(TestStringConstants.malformedMappings, obj.toString());
	}
	
	private static void save(String filename, String json) throws IOException{
		File file = new File("./resources/"+filename);
		FileWriter writer = new FileWriter(file, false);
		writer.write(json);
		writer.flush();
		writer.close();
		
		System.out.println("Saved string to " + filename);
	}
}
