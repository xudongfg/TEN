package com.ten.beans;

public class CourseAnnotationsBean extends TenCommonAnnoationsBean{
	String creator;
	String description;
	String keywords;

	public CourseAnnotationsBean(){
		super();
		creator = "";
		description = "";
		keywords = "";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
}
