package com.ten.struts2.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.ten.beans.*;

import com.ten.triplestore.dao.implementation.VirtuosoAccessDaoImpl;
import com.ten.triplestore.dao.interfaces.TriplestoreAccessDaoInterface;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;


public class ManageCourseDetailsAction extends SearchLearningObjectsAction {

    static Logger log = Logger.getLogger(ManageCourseDetailsAction.class);

    private static final long serialVersionUID = 1L;

    private String actionName;

    private String id;
    private int courseId;

    private String name;
    private String description;
    private String prerequisites;
    private String topics;
    private String overview;
    private String timeline;
    private String individualAssignments;
    private String groupAssignments;
    private String onsiteAssignments;
    private String exams;
    private String quizzes;
    private String knowledgeCheckpoints;
    private String gradingRubric;
    private String technologyRequirements;
    private String supportServices;
    private File documentTemplates; // course document templates
    private String evaluationFormat; // list or set of questions
    private File evaluationAttachments; // course evaluation attachments
    private String progressMeterBadges;

    // course download zip
    private InputStream fileInputStream;
    private String fileName;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTypeOfLearningObject() {
        return typeOfLearningObject;
    }

    public void setTypeOfLearningObject(String typeOfLearningObject) {
        this.typeOfLearningObject = typeOfLearningObject;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    @SuppressWarnings("unchecked")
    public String execute() throws Exception {
        //Get request method invoked
        HttpServletRequest request = ServletActionContext.getRequest();

        String method = request.getMethod();
        String result = ActionConstants.FORWARD_SHOWJSP;

        dbAccessDaoInterface = new DbAccessDaoImpl();
        try {
            if (ActionConstants.METHOD_GET.equalsIgnoreCase(method)) {

                if (this.actionName == null || this.actionName.isEmpty()) {
                    // Default, get course content and linked learning objects
                    CourseBean course = dbAccessDaoInterface.getCourse(courseId);
                    course.addObjectives(dbAccessDaoInterface.getCourseObjectives(courseId));
                    Collection<CourseLearningObjectLinkBean> links = dbAccessDaoInterface.getCourseLearningObjectLinks(courseId);
                    Map<Integer, LearningObjectDetailsBean> learningObjects = new HashMap<Integer, LearningObjectDetailsBean>(); 
                    for (CourseLearningObjectLinkBean link : links) {
                        LearningObjectDetailsBean learningObject;
                        if (!learningObjects.containsKey(link.getLearningObject().getId())) {
                            learningObject = dbAccessDaoInterface.getLearningObject(link.getLearningObject().getId());
                            learningObjects.put(learningObject.getId(), learningObject);
                        }
                        else {
                            learningObject = learningObjects.get(link.getLearningObject().getId());
                        }
                        link.setLearningObject(learningObject);
                    }
                    course.addLearningObjectLinks(links);
        
                    populateAction(course);
                    
                    session.put(ActionConstants.SESSION_COURSE_KEY, course);
                }
                else if ("searchLearningObjects".equals(this.actionName)) {
                    CourseBean course = (CourseBean)session.get(ActionConstants.SESSION_COURSE_KEY);
                    learningObjectsSearchResults = searchLearningObjects(keywords, typeOfLearningObject, course);
                    // keep map of learning object details in session for linking purposes
                    session.put(
                        ActionConstants.SESSION_LEARNING_OBJECTS_SEARCH_RESULTS_KEY,
                        learningObjectsSearchResults);
                }

                result = ActionConstants.FORWARD_SHOWJSP;
            } else if (ActionConstants.METHOD_POST.equalsIgnoreCase(method)) {
                if ("edit".equals(this.actionName)) {
                	log.info("updating course: "+this.name);
                	dbAccessDaoInterface.updateCourse( buildCourse() );
                    CourseBean course = (CourseBean)session.get(ActionConstants.SESSION_COURSE_KEY);
                    updateCourse(course);
                    result = ActionConstants.FORWARD_SUCCESS;
                }
                else if ("delete".equals(this.actionName)) {
                    log.info("deleting course: "+this.name);
                    dbAccessDaoInterface.deleteCourse(courseId);
                    tdbAccessDaoInterface = new VirtuosoAccessDaoImpl();
                    tdbAccessDaoInterface.deleteCourseAnnotations(courseId);
                    result = ActionConstants.DELETE_COURSE;
                }
                else if ("linkLearningObjects".equals(this.actionName)) {
                    log.info("link learning objects");
                    
                    CourseBean course = (CourseBean)session.get(ActionConstants.SESSION_COURSE_KEY);
                    learningObjectsSearchResults = (Map<Integer, LearningObjectDetailsBean>)session.get("learningObjectsSearchResults");
                    parseSelectionAndLinkLearningObjectsToCourse(course);
                    // remove learning objects from the search results list which got linked,
                    // since these will be shown in the list of linked learning objects instead
                    Iterator<Map.Entry<Integer, LearningObjectDetailsBean>> it = learningObjectsSearchResults.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Integer, LearningObjectDetailsBean> entry = it.next();
                        if (course.isLearningObjectLinked(entry.getKey())) {
                            it.remove();
                        }
                    }
                    
                    result = ActionConstants.FORWARD_SUCCESS;
                }
                else if ("export".equals(this.actionName)) {
                	log.info("exporting course");
                    File file = dbAccessDaoInterface.getCourseContentForExport(courseId);
                    fileName = file.getName();
                    fileInputStream = new FileInputStream(file);
                    result = ActionConstants.DOWNLOAD_COURSE;
                }
            }
        } catch (Exception ex) {
            log.error(ex);
            result = ActionConstants.FORWARD_INPUT;
        }
        return result;
    }

    private void populateAction( CourseBean course ) {
        this.name = course.getName();
        this.description = course.getDescription();
        this.prerequisites = course.getPrerequisites();
        this.topics = course.getTopics();
        this.overview = course.getOverview();
        this.timeline = course.getTimeline();
        this.individualAssignments = course.getIndividualAssignments();
        this.groupAssignments = course.getGroupAssignments();
        this.onsiteAssignments = course.getOnsiteAssignments();
        this.exams = course.getExams();
        this.quizzes = course.getQuizzes();
        this.knowledgeCheckpoints = course.getKnowledgeCheckpoints();
        this.gradingRubric = course.getGradingRubric();
        this.technologyRequirements = course.getTechnologyRequirements();
        this.supportServices = course.getSupportServices();
        this.documentTemplates = course.getDocumentTemplates();
        this.evaluationFormat = course.getEvaluationFormat();
        this.evaluationAttachments = course.getEvaluationAttachments();
        this.progressMeterBadges = course.getProgressMeterBadges();
    }

    private CourseBean buildCourse(){
        CourseBean course = new CourseBean();
        updateCourse(course);
        return course;
    }

    private void updateCourse(CourseBean course){
        course.setId(Integer.parseInt(this.id));
        course.setName(this.name);
        course.setDescription(this.description);
        course.setPrerequisites(this.prerequisites);
        course.setTopics(this.topics);
        course.setOverview(this.overview);
        course.setTimeline(this.timeline);
        course.setIndividualAssignments(this.individualAssignments);
        course.setGroupAssignments(this.groupAssignments);
        course.setOnsiteAssignments(this.onsiteAssignments);
        course.setExams(this.exams);
        course.setQuizzes(this.quizzes);
        course.setKnowledgeCheckpoints(this.knowledgeCheckpoints);
        course.setGradingRubric(this.gradingRubric);
        course.setTechnologyRequirements(this.technologyRequirements);
        course.setSupportServices(this.supportServices);
        course.setDocumentTemplates(this.documentTemplates);
        course.setEvaluationFormat(this.evaluationFormat);
        course.setEvaluationAttachments(this.evaluationAttachments);
        course.setProgressMeterBadges(this.progressMeterBadges);
    }
}
