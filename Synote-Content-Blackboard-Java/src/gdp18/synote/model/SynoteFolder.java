package gdp18.synote.model;

public class SynoteFolder {
	
	private final String name;
	private final String description;
	private final String panoptoFolderId;
	private boolean isMapped;
	private boolean isSuggested;
	private boolean isOther;
	
	public SynoteFolder(String name, String description,
			String panoptoFolderId){
		this.name = name;
		this.description = description;
		this.panoptoFolderId = panoptoFolderId;
	}
	
	public SynoteFolder(String name, String description, 
			String panoptoFolderId, boolean isMapped){
		this(name, description, panoptoFolderId);
		this.isMapped = isMapped;
	}

	public String getName() {
		return name;
	}

	public String getPanoptoFolderId() {
		return panoptoFolderId;
	}

	public String getDescription() {
		return description;
	}
	
	public boolean isMapped() {
		return isMapped;
	}

	public void setMapped(boolean isMapped) {
		this.isMapped = isMapped;
	}

	public boolean isSuggested() {
		return isSuggested;
	}

	public void setSuggested(boolean isSuggested) {
		this.isSuggested = isSuggested;
	}

	public boolean isOther() {
		return isOther;
	}

	public void setOther(boolean isOther) {
		this.isOther = isOther;
	}
	
}
