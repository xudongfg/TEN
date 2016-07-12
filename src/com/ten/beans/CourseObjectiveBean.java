package com.ten.beans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CourseObjectiveBean {
	int id;
	String description;

    // key is learning object id
    Map<Integer, CourseObjectiveLearningObjectLinkBean> learningObjectLinks;
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

    public Map<Integer, CourseObjectiveLearningObjectLinkBean> getLearningObjectLinks() {
        return learningObjectLinks;
    }
    
    public void addLearningObjectLink(CourseObjectiveLearningObjectLinkBean link) {
        learningObjectLinks.put(link.getLearningObject().getId(), link);
    }

    public void addLearningObjectLinks(Collection<CourseObjectiveLearningObjectLinkBean> links) {
        for (CourseObjectiveLearningObjectLinkBean link : links) {
            addLearningObjectLink(link);
        }
    }

    public boolean isLearningObjectLinked(int learningObjectId) {
        return learningObjectLinks.containsKey(learningObjectId);
    }

    public void removeLearningObjectLinks(int learningObjectId) {
        learningObjectLinks.remove(learningObjectId);
    }
    
    public CourseObjectiveBean() {
        this.learningObjectLinks = new HashMap<Integer,CourseObjectiveLearningObjectLinkBean>();
    }
}
