package gdp18.synote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class SynoteContentItem {
	private int id;
	private String inputId;
	private String name;
	private String description;
	private String creatorId;
	private String creatorUsername;
	private String mp3URL;
	private String mp4URL;
	private Date startTime;
	private String duration;
	private String thumbUrl;
	private boolean complete;
	private String synoteURL;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInputId() {
		return inputId;
	}
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreatorUsername() {
		return creatorUsername;
	}
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}
	public String getMp3URL() {
		return mp3URL;
	}
	public void setMp3URL(String mp3url) {
		mp3URL = mp3url;
	}
	public String getMp4URL() {
		return mp4URL;
	}
	public void setMp4URL(String mp4url) {
		mp4URL = mp4url;
	}
	public Date getStartTime(){
		return this.startTime;
	}
	public String getStartTimeString() {
		SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd, yyyy 'at' hh:mm:ss a");
		return format.format(startTime).toString();
	}
	public void setStartTime(String startString) throws ParseException {
		String formattedString = startString.split("Z")[0];
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		this.startTime = format.parse(formattedString);
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	public String getSynoteURL() {
		return synoteURL;
	}
	public void setSynoteURL(String synoteURL) {
		this.synoteURL = synoteURL;
	}
}
