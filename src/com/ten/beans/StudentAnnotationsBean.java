package com.ten.beans;

public class StudentAnnotationsBean {
	String tribe;
	String preferredLearningObjectType;
	String preferredTextContent; //definition, Explanation, description
	String preferredImageContent; //photo, illustration, graph
	String preferredLanguage;

	public String getPreferredLearningObjectType() {
		return preferredLearningObjectType;
	}

	public void setPreferredLearningObjectType(String preferredLearningObjectType) {
		this.preferredLearningObjectType = preferredLearningObjectType;
	}

	public StudentAnnotationsBean(){
		super();
		tribe="";
		preferredLearningObjectType = "";
		preferredTextContent = "";
		preferredImageContent = "";
		preferredLanguage = "";
	}

	public String getPreferredTextContent() {
		return preferredTextContent;
	}

	public void setPreferredTextContent(String preferredTextContent) {
		this.preferredTextContent = preferredTextContent;
	}

	public String getPreferredImageContent() {
		return preferredImageContent;
	}

	public void setPreferredImageContent(String preferredImageContent) {
		this.preferredImageContent = preferredImageContent;
	}

	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public String getTribe() {
		return tribe;
	}

	public void setTribe(String tribe) {
		this.tribe = tribe;
	}
}
