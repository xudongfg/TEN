package com.ten.beans;

import java.util.Date;

public class TenLearningObjectAnnotationsBean extends TenCommonAnnoationsBean {
	String annotator;
	
	//Descriptive tags
	String title;
	String keywords;
    String description;
	String source;
	String language;
	String relation;
	String coverage;	
	String tribe;
	String rating;
	String relevantSubjects;
	


    //Structural Tags
	String date;
	String type;
	String format;
	String identifier;
	
	//for recommendation
	String textType;
	String imageType;
	
	public String getAnnotator() {
		return annotator;
	}

	public void setAnnotator(String annotator) {
		this.annotator = annotator;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    public String getRelevantSubjects() {
        return relevantSubjects;
    }

    public void setRelevantSubjects(String relevantSubjects) {
        this.relevantSubjects = relevantSubjects;
    }	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTribe() {
		return tribe;
	}

	public void setTribe(String tribe) {
		this.tribe = tribe;
	}
	
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getTextType() {
		return textType;
	}

	public void setTextType(String textType) {
		this.textType = textType;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public TenLearningObjectAnnotationsBean(){
		super();
		//descriptive
		title = "";
		keywords = "";
		description = "";
		source = "";
		language = "";
		relation = "";
		coverage = "";
		tribe = "";
		rating = "";
		relevantSubjects = "";
		
		//structural
		date = (new Date()).toString();
		type = "";
		format = "";
		identifier = "";
		
		textType = "";
		imageType = "";
	}
}
