package gdp18.testing;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gdp18.synote.model.SynoteContentData;
import gdp18.synote.model.SynoteCourseFolderData;

public class ParsingJSONTest {

	private SynoteContentData contentData;
	private SynoteCourseFolderData folderData;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		TestStringBuilder builder = new TestStringBuilder();
		builder.generateStrings();
	}
	
	@Before
	public void setUp() throws Exception {
		contentData = new SynoteContentData();
		folderData = new SynoteCourseFolderData();
	}
	
	private String readJSONFromFile(String filename) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("./resources/"+filename));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	
	@Test
	public void testUnconfiguredCourse() throws Exception {
		String json = readJSONFromFile(TestStringConstants.unconfiguredCourse);
		contentData.parseContentJson(json);
		
		assertFalse(contentData.isCourseConfigured());
	}

	@Test
	public void testConfiguredCourse() throws Exception {
		String json = readJSONFromFile(TestStringConstants.configuredCourseWith10);
		contentData.parseContentJson(json);
		
		assertTrue(contentData.isCourseConfigured());
		assertTrue(contentData.getItems().size() == 10);
	}
	
	@Test(expected=JSONException.class)
	public void testMalformedJSONForContent() throws Exception{
		String json = readJSONFromFile(TestStringConstants.malformedContent);
		contentData.parseContentJson(json);	
	}
	
	@Test
	public void testCourseMappings() throws Exception {
		String json = readJSONFromFile(TestStringConstants.courseMappings);
		folderData.parseMappedFoldersJson(json);
		
		assertTrue(folderData.isCourseConfigured());
		assertEquals(folderData.getMappedFolders().size(), 3);
	}
	
	@Test
	public void testUnconfiguredCourseMappings() throws Exception {
		String json = readJSONFromFile(TestStringConstants.unconfiguredMappings);
		folderData.parseMappedFoldersJson(json);
		
		assertFalse(folderData.isCourseConfigured());
		assertTrue(folderData.getMappedFolders().size() == 0);
	}
	
	@Test
	public void testFoldersVariety() throws Exception {
		String json = readJSONFromFile(TestStringConstants.foldersVariety);
		folderData.parseSuggestedFoldersJson(json);
		
		assertTrue(folderData.isCourseConfigured());
		assertEquals(folderData.getSuggestedFolders().size(), 3);
		assertEquals(folderData.getMappedFolders().size(), 3);
		assertEquals(folderData.getOtherFolders().size(), 3);
		assertEquals(folderData.getAllUnmappedFolders().size(), 3);
	}
	
	@Test
	public void testUniqueFoldersVariety() throws Exception {
		String json = readJSONFromFile(TestStringConstants.uniqueFoldersVariety);
		folderData.parseSuggestedFoldersJson(json);
		
		assertTrue(folderData.isCourseConfigured());
		assertEquals(folderData.getSuggestedFolders().size(), 3);
		assertEquals(folderData.getMappedFolders().size(), 3);
		assertEquals(folderData.getOtherFolders().size(), 3);
		assertEquals(folderData.getAllUnmappedFolders().size(), 6);
	}
	
	@Test(expected=JSONException.class)
	public void testMalformedSuggested() throws Exception {
		String json = readJSONFromFile(TestStringConstants.malformedSuggested);
		folderData.parseSuggestedFoldersJson(json);
	}
	
	@Test(expected=JSONException.class)
	public void testMalformedMappings() throws Exception {
		String json = readJSONFromFile(TestStringConstants.malformedMappings);
		folderData.parseMappedFoldersJson(json);
	}
}
