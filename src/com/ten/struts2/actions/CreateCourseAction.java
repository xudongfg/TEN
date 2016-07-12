package com.ten.struts2.actions;

import javax.servlet.http.HttpServletRequest;

import com.ten.beans.CourseBean;
import com.ten.beans.CourseObjectiveBean;
import com.ten.beans.UserDetailsBean;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.CourseAnnotationsBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;
import com.ten.triplestore.dao.implementation.VirtuosoAccessDaoImpl;
import com.ten.triplestore.dao.interfaces.TriplestoreAccessDaoInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * @author Nita Karande
 * This action is invoked by create_course.jsp
 * It creates a course and stores it in the database and stores annotations in the triplestore
 */
public class CreateCourseAction extends ActionSupport implements SessionAware {

	static Logger log = Logger.getLogger(CreateCourseAction.class);
	
	private static final long serialVersionUID = 1L;
	CourseAnnotationsBean courseAnnotationsBean;

	String courseName;
    String courseDescription;
    String coursePrerequisites;
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
    String courseObjectives[];

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public String getCoursePrerequisites() {
		return coursePrerequisites;
	}

	public void setCoursePrerequisites(String coursePrerequisites) {
		this.coursePrerequisites = coursePrerequisites;
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

    public String[] getCourseObjectives() {
        return courseObjectives;
    }

    public void setCourseObjectives(String[] courseObjectives) {
        this.courseObjectives = courseObjectives;
    }

    public CourseAnnotationsBean getCourseAnnotationsBean() {
        return courseAnnotationsBean;
    }

    public void setCourseAnnotationsBean(CourseAnnotationsBean courseAnnotationsBean) {
        this.courseAnnotationsBean = courseAnnotationsBean;
    }
    
    private Map<String, Object> session ;
    
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    /**
	 * This method is configured to be invoked in struts.xml, for creating courses and storing course related annotations in triple store.
	 * It makes calls to mysql dao implementation to store the course in database
	 * It also stores annotations for course in triple store 
	 */
	public String execute() throws Exception {
		//Get request method invoked
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String method = request.getMethod();
		String result = ActionConstants.FORWARD_SHOWJSP;
		
		if (ActionConstants.METHOD_GET.equalsIgnoreCase(method)) {
            reset();
            result = ActionConstants.FORWARD_SHOWJSP;;
		} else if (ActionConstants.METHOD_POST.equalsIgnoreCase(method)) {
			try {
				//Insert course into Mysql database
				DbAccessDaoInterface dbAccessDaoInterface = new DbAccessDaoImpl();
                UserDetailsBean user  = dbAccessDaoInterface.getUserDetails(request.getUserPrincipal().getName());
                CourseBean course = buildCourse(user.getId());
                int courseId = dbAccessDaoInterface.addCourse(course);

                //Insert course objectives into DB, and add the objective beans to the course bean
                ArrayList<CourseObjectiveBean> objectives = new ArrayList<CourseObjectiveBean>();
                for (String objective : getCourseObjectives())
                {
                    if (objective == null || objective.isEmpty())
                        continue;
                    CourseObjectiveBean courseObjectiveBean = new CourseObjectiveBean();
                    courseObjectiveBean.setDescription(objective);
                    objectives.add(courseObjectiveBean);
                }
                dbAccessDaoInterface.addCourseObjectives(course, objectives);

                //Insert annotations in Triplestore
				TriplestoreAccessDaoInterface tdbAccessDaoInterface = new VirtuosoAccessDaoImpl();
				tdbAccessDaoInterface.insertCourseAnnotations(this.courseAnnotationsBean, courseId);
                
                session.put("course", course);
				
				//course created successfully
				addActionMessage(ActionConstants.CREATE_COURSE_SUCCESS_MSG);
				result = ActionConstants.FORWARD_SUCCESS;
			} catch(Exception ex) {
				log.error(ex);
				reset();
				addActionError(ActionConstants.CREATE_COURSE_ERROR_MSG);
				result = ActionConstants.FORWARD_INPUT;
			}	
		}
		return result;
	}

    private CourseBean buildCourse( int ownerId ){
        CourseBean course = new CourseBean();
        course.setOwnerId( ownerId );
        course.setName(this.courseName);
        course.setDescription(this.courseDescription);
        course.setPrerequisites(this.coursePrerequisites);
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

        course.setAnnotations(courseAnnotationsBean);
        
        return course;
    }
	
	public void reset(){
		this.courseName = "";
		this.courseAnnotationsBean = new CourseAnnotationsBean();
	}
}
