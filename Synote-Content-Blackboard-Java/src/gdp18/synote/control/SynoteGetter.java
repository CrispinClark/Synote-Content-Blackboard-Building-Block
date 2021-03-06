package gdp18.synote.control;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.data.course.CourseMembership.Role;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Id;
import blackboard.persist.PersistenceException;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.user.UserDbLoader;
import blackboard.platform.context.Context;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.platform.security.SecurityUtil;

public abstract class SynoteGetter {
	
	protected Course bbCourse;
	protected User bbUser;
	protected String bbUserName;
	protected boolean isInstructor;
	
	public SynoteGetter(Context ctx){
		initSynoteGetter(ctx.getCourse(), ctx.getUser());
    }
	
	public SynoteGetter(String courseId, String username){
		BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
		try {
			CourseDbLoader courseLoader = (CourseDbLoader) bbPm.getLoader(CourseDbLoader.TYPE);
			Course bbCourse = courseLoader.loadByCourseId(courseId);
			
			UserDbLoader userLoader = (UserDbLoader) bbPm.getLoader(UserDbLoader.TYPE);
			User bbUser = userLoader.loadByUserName(username);
			
			initSynoteGetter(bbCourse, bbUser);
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}
	
    protected void initSynoteGetter(Course bbCourse, User bbUser){
    	this.bbCourse = bbCourse;
    	this.bbUser = bbUser;
        this.bbUserName = bbUser.getUserName();
        this.isInstructor = isUserInstructor(this.bbCourse.getId(), this.bbUserName, false);
    }

	public String getCourseCode(){
		return bbCourse.getBatchUid();
	}

	public String getCourseName(){
		return bbCourse.getDisplayTitle();
	}
	
	public String getCourseDescription(){
		return bbCourse.getDescription();
	}
	
    public String sendGetRequest(String url) throws Exception{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
    
    public boolean userMayConfig(){
    	return this.isInstructor;
    }
    
    public static boolean isUserInstructor(Id bbCourseId, String bbUserName, boolean checkTACanCreateLinks){      
        
    	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
        
        try {
            //Get user's blackboard ID from their username
            UserDbLoader userLoader = (UserDbLoader)bbPm.getLoader(UserDbLoader.TYPE);
            User user = userLoader.loadByUserName(bbUserName);
            Id bbUserId = user.getId();
            
            //Load the user's membership in the current course to get their role
            CourseMembershipDbLoader membershipLoader = (CourseMembershipDbLoader)bbPm.getLoader(CourseMembershipDbLoader.TYPE);
            CourseMembership usersCourseMembership = membershipLoader.loadByCourseAndUserId(bbCourseId, bbUserId);
            Role userRole = usersCourseMembership.getRole();
            
            if (isInstructorRole(userRole))
            {
                return true;
            }
        }
        catch(Exception e) {
            Utils.log(e, String.format("Error getting user's course membership (course ID: %s, userName: %s).", bbCourseId, bbUserName));
        }

        //User is not an instructor, check if they are a system admin
        return userCanConfigureSystem();
    }

	public String getJWT(){
		String subjectRole = isInstructor ? "creator" : "viewer";
    	String courseId = bbCourse.getBatchUid();
    	
    	return Utils.generateRequestJWT(bbUserName, courseId, subjectRole);
	}
	
    /*Returns true if role should be treated as an Instructor. Instructors get creator access in Synote.*/
    private static boolean isInstructorRole(
            blackboard.data.course.CourseMembership.Role membershipRole) {
        //Role is instructor role if it is the 'Instructor' or 'Course Builder' built in blackboard role,
        //or if it is any role marked with the 'Act As Instructor' flag.
        return membershipRole.equals(CourseMembership.Role.INSTRUCTOR)
                || membershipRole.equals(CourseMembership.Role.COURSE_BUILDER)
                || membershipRole.getDbRole().isActAsInstructor();
    }
    
    // Check for system tools configuration entitlement
    public static boolean userCanConfigureSystem() {
        return SecurityUtil.userHasEntitlement("system.panopto.EXECUTE");
    }

	public String getBbUserName() {
		return bbUserName;
	}
	
	public abstract boolean isCourseConfigured();
}
