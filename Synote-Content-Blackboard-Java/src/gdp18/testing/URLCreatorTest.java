package gdp18.testing;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gdp18.synote.control.Utils;

public class URLCreatorTest {

	String userId = "dummyUser";
	String courseId = "dummyCourse";
	String synoteURL = "svm-tk1g11-gdp18.ecs.soton.ac.uk:3005";
	
	@Test
	public void testConfigureCourseURL() {
		String jwt = Utils.generateRequestJWT(userId, courseId, "creator");
		String url = Utils.pluginSettings.getSynoteURL() + Utils.getConfigureCourseURL(courseId) + "?jwt=" + jwt;
		
		assertEquals(url, "svm-tk1g11-gdp18.ecs.soton.ac.uk:3005/connect/course?jwt=" +jwt);
	}
	
	@Test
	public void testCourseContentURL() {
		String jwt = Utils.generateRequestJWT(userId, courseId, "creator");
		String url = Utils.pluginSettings.getSynoteURL() + Utils.getCourseContentURL(courseId) + "?jwt=" + jwt;
		
		assertEquals(url, "svm-tk1g11-gdp18.ecs.soton.ac.uk:3005/connect/course/"+courseId+"?jwt=" +jwt);
	}
	
	@Test
	public void testCourseMappingsURL() {
		String jwt = Utils.generateRequestJWT(userId, courseId, "creator");
		String url = Utils.pluginSettings.getSynoteURL() + Utils.getCourseMappingsURL(courseId) + "?jwt=" + jwt;
		
		assertEquals(url, "svm-tk1g11-gdp18.ecs.soton.ac.uk:3005/connect/course/"+courseId+"/mappings?jwt=" +jwt);
	}
	
	@Test
	public void testSuggestedFoldersURL() {
		String jwt = Utils.generateRequestJWT(userId, courseId, "creator");
		String url = Utils.pluginSettings.getSynoteURL() + Utils.getSuggestedFoldersURL(courseId) + "?jwt=" + jwt;
		
		assertEquals(url, "svm-tk1g11-gdp18.ecs.soton.ac.uk:3005/connect/course/"+courseId+"/suggest?jwt=" +jwt);
	}
}
