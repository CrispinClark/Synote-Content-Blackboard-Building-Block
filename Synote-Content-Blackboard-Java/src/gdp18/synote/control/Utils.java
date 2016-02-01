package gdp18.synote.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedHashMap;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTSigner.Options;

import blackboard.platform.plugin.PlugInUtil;

public class Utils {
	
	public static final Settings pluginSettings = new Settings();
	
	public static final String vendorID = "syn";
    public static final String pluginHandle = "synote-content";
    public static final String logFilename = "log.txt";

    public static final String configureCourseScriptURL = "Configure_Course.jsp";
    public static final String courseFoldersScriptURL = "Course_Folders.jsp";
    public static final String addCourseFoldersScriptURL = "Add_Folders_To_Course.jsp";
    
    public static final String contentScriptURL = "Content.jsp";
    
    public static final String mapFoldersURL = "Map_Folders.jsp";

    public static String getConfigureCourseURL(String courseId){
    	return "/connect/course";
    }
    
    public static String getCourseContentURL(String courseId){
    	return "/connect/course/"+courseId;
    }

    public static String getCourseMappingsURL(String courseId){
    	return "/connect/course/"+courseId+"/mappings";
    }

    public static String getSuggestedFoldersURL(String courseId){
    	return "/connect/course/"+courseId+"/suggest";
    }
    
    public static String generateRequestJWT(String userId, String courseId, 
			String subjectRole){

		JWTSigner signer = new JWTSigner(pluginSettings.getSharedKey());
		
		LinkedHashMap<String, Object> requestClaims = new LinkedHashMap<String, Object>();
		requestClaims.put("user", userId);
		requestClaims.put("subjectCourse", courseId);
		requestClaims.put("subjectRole", subjectRole);
		
		Options options = new Options();
		options.setExpirySeconds(pluginSettings.getJWTExpirySeconds());
		
		return signer.sign(requestClaims, options);
	}

    public String generateRequestJWT(String sharedKey, int expirySeconds, String userId, 
    		String courseId, String subjectRole){
    	JWTSigner signer = new JWTSigner(sharedKey);
		
		LinkedHashMap<String, Object> requestClaims = new LinkedHashMap<String, Object>();
		requestClaims.put("user", userId);
		requestClaims.put("subjectCourse", courseId);
		requestClaims.put("subjectRole", subjectRole);
		
		Options options = new Options();
		options.setExpirySeconds(expirySeconds);
		
		return signer.sign(requestClaims, options);
    }
    
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isValidXML(String str){
    	return !(str.contains("<") | str.contains(">") | str.contains("&"));
    }
    
	public static void log(String logMessage){
        log(null, logMessage);
    }

    public static void log(Exception e, String logMessage) {
        try {
        	File configDir = PlugInUtil.getConfigDirectory(vendorID, pluginHandle);
        	File logFile = new File(configDir, logFilename);

            FileWriter fileStream = new FileWriter(logFile, true);
            BufferedWriter bufferedStream = new BufferedWriter(fileStream);
            PrintWriter output = new PrintWriter(bufferedStream);

            output.write(new Date().toString() + ": " + logMessage);

            if(!logMessage.endsWith(System.getProperty("line.separator")))
            {
                output.println();
            }

            if(e != null)
            {
                e.printStackTrace(output);
            }

            output.println("===================================================================================");

            output.flush();
            output.close();
        }
        catch(Exception ex) {
        }
    }
}
