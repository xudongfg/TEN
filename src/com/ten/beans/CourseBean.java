package com.ten.beans;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Nita Karande
 * This is the bean used data transfer between jsp and action classes
 * This class will fields for dublin core annotations
 */
public class CourseBean {
    int id;
    int ownerId;
    String name;
    String description;
    String prerequisites;
    String topics;
    String overview;
    String timeline;
    String individualAssignments;
    String groupAssignments;
    String onsiteAssignments;
    String exams;
    String quizzes;
    String knowledgeCheckpoints;
    String gradingRubric;
    String technologyRequirements;
    String supportServices;

    File documentTemplates; // course document templates

    String evaluationFormat; // list or set of questions
    File evaluationAttachments; // course evaluation attachments

    String progressMeterBadges;

    @Deprecated
    int courseId;

    @Deprecated
    String courseName;
	
	Map<Integer, CourseObjectiveBean> objectives; // key is objective id
    
    CourseAnnotationsBean annotations;

    // key is learning object id
    Map<Integer, CourseLearningObjectLinkBean> learningObjectLinks;

    // key is learning object id + "-" + course content name
    Map<String, CourseLearningObjectLinkBean> courseContentLearningObjectLinks;
    
    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
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

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public String getIndividualAssignments() {
        return individualAssignments;
    }

    public void setIndividualAssignments(String individualAssignments) {
        this.individualAssignments = individualAssignments;
    }

    public String getGroupAssignments() {
        return groupAssignments;
    }

    public void setGroupAssignments(String groupAssignments) {
        this.groupAssignments = groupAssignments;
    }

    public String getOnsiteAssignments() {
        return onsiteAssignments;
    }

    public void setOnsiteAssignments(String onsiteAssignments) {
        this.onsiteAssignments = onsiteAssignments;
    }

    public String getExams() {
        return exams;
    }

    public void setExams(String exams) {
        this.exams = exams;
    }

    public String getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(String quizzes) {
        this.quizzes = quizzes;
    }

    public String getKnowledgeCheckpoints() {
        return knowledgeCheckpoints;
    }

    public void setKnowledgeCheckpoints(String knowledgeCheckpoints) {
        this.knowledgeCheckpoints = knowledgeCheckpoints;
    }

    public String getGradingRubric() {
        return gradingRubric;
    }

    public void setGradingRubric(String gradingRubric) {
        this.gradingRubric = gradingRubric;
    }

    public String getTechnologyRequirements() {
        return technologyRequirements;
    }

    public void setTechnologyRequirements(String technologyRequirements) {
        this.technologyRequirements = technologyRequirements;
    }

    public String getSupportServices() {
        return supportServices;
    }

    public void setSupportServices(String supportServices) {
        this.supportServices = supportServices;
    }

    public File getDocumentTemplates() {
        return documentTemplates;
    }

    public void setDocumentTemplates(File documentTemplates) {
        this.documentTemplates = documentTemplates;
    }

    public String getEvaluationFormat() {
        return evaluationFormat;
    }

    public void setEvaluationFormat(String evaluationFormat) {
        this.evaluationFormat = evaluationFormat;
    }

    public File getEvaluationAttachments() {
        return evaluationAttachments;
    }

    public void setEvaluationAttachments(File evaluationAttachments) {
        this.evaluationAttachments = evaluationAttachments;
    }

    public String getProgressMeterBadges() {
        return progressMeterBadges;
    }

    public void setProgressMeterBadges(String progressMeterBadges) {
        this.progressMeterBadges = progressMeterBadges;
    }

    public Map<Integer,CourseObjectiveBean> getObjectives() {
        return objectives;
    }

    public void setObjectives(Map<Integer,CourseObjectiveBean> objectives) {
        this.objectives = objectives;
    }

    public void addObjectives(Collection<CourseObjectiveBean> objectives) {
        for (CourseObjectiveBean objective : objectives) {
            this.objectives.put(objective.getId(), objective);
        }
    }

    public CourseAnnotationsBean getAnnotations() {
        return annotations;
    }

    public void setAnnotations(CourseAnnotationsBean annotations) {
        this.annotations = annotations;
    }

    public Map<Integer, CourseLearningObjectLinkBean> getLearningObjectLinks() {
        return learningObjectLinks;
    }

    public Map<String, CourseLearningObjectLinkBean> getCourseContentLearningObjectLinks() {
        return courseContentLearningObjectLinks;
    }
    
    public void addLearningObjectLink(CourseLearningObjectLinkBean link) {
        if (link.getCourseContentName() == null || link.getCourseContentName().isEmpty()) {
            learningObjectLinks.put(link.getLearningObject().getId(), link);
            courseContentLearningObjectLinks.put(Integer.toString(link.getLearningObject().getId()), link);
        } else {
            courseContentLearningObjectLinks.put(link.getLearningObject().getId() + "-" + link.getCourseContentName(), link);
        }
        link.setCourse(this);
    }

    public void addLearningObjectLinks(Collection<CourseLearningObjectLinkBean> links) {
        for (CourseLearningObjectLinkBean link : links) {
            addLearningObjectLink(link);
        }
    }
    
    public boolean isLearningObjectLinked(int learningObjectId) {
        return learningObjectLinks.containsKey(learningObjectId);
    }

    public boolean isLearningObjectLinked(int learningObjectId, String courseContentName) {
        if (courseContentName == null || courseContentName.isEmpty()) {
            return isLearningObjectLinked(learningObjectId);
        }
        return courseContentLearningObjectLinks.containsKey(Integer.toString(learningObjectId) + "-" + courseContentName);
    }
    
    public void removeLearningObjectLinks(int learningObjectId) {
        Iterator<Map.Entry<String, CourseLearningObjectLinkBean>> it = courseContentLearningObjectLinks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, CourseLearningObjectLinkBean> entry = it.next();
            if (entry.getValue().getLearningObject().getId() == learningObjectId) {
                it.remove();
            }
        }
        learningObjectLinks.remove(learningObjectId);
    }
    
    public CourseBean() {
		this.courseName ="";
		this.courseId = 0;
        this.objectives = new HashMap<Integer,CourseObjectiveBean>();
        this.learningObjectLinks = new HashMap<Integer,CourseLearningObjectLinkBean>();
        this.courseContentLearningObjectLinks = new HashMap<String,CourseLearningObjectLinkBean>();
	}
	

    @Deprecated
	public int getCourseId() {
		return courseId;
	}

    @Deprecated
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

    @Deprecated
	public String getCourseName() {
		return courseName;
	}

    @Deprecated
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	
}
