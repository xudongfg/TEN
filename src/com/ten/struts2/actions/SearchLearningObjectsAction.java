package com.ten.struts2.actions;

import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.CourseBean;
import com.ten.beans.CourseLearningObjectLinkBean;
import com.ten.beans.CourseObjectiveBean;
import com.ten.beans.CourseObjectiveLearningObjectLinkBean;
import com.ten.beans.LearningObjectDetailsBean;
import com.ten.beans.TenLearningObjectAnnotationsBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;
import com.ten.triplestore.dao.implementation.VirtuosoAccessDaoImpl;
import com.ten.triplestore.dao.interfaces.TriplestoreAccessDaoInterface;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

/**
 * 
 * @author Nita Karande
 * This action is invoked by add_learning_objects.jsp 
 * It allows searching for learning objects and linking them to a course
 */
public class SearchLearningObjectsAction extends ActionSupport implements SessionAware {

	static Logger log = Logger.getLogger(SearchLearningObjectsAction.class);
    
    protected TriplestoreAccessDaoInterface tdbAccessDaoInterface;
    protected DbAccessDaoInterface dbAccessDaoInterface;
	
	private static final long serialVersionUID = 1L;
	
	protected String keywords;
	protected String typeOfLearningObject;
	protected String[] learningObjectIds;
    protected String[] courseContentNames;

    protected int courseContentId;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
	
	public String getTypeOfLearningObject() {
		return typeOfLearningObject;
	}

	public void setTypeOfLearningObject(String typeOfLearningObject) {
		this.typeOfLearningObject = typeOfLearningObject;
	}

    public void setLearningObjectIds(String[] learningObjectIds) {
        this.learningObjectIds = learningObjectIds;
    }
    
    public String[] getLearningObjectIds() {
        return learningObjectIds;
    }

    public void setCourseContentNames(String[] courseContentNames) {
        this.courseContentNames = courseContentNames;
    }
    
    public String[] getCourseContentNames() {
        return courseContentNames;
    }
    
    public int getCourseContentId() {
        return courseContentId;
    }

    public void setCourseContentId(int courseContentId) {
        this.courseContentId = courseContentId;
    }
    
    protected Map<Integer, LearningObjectDetailsBean> learningObjectsSearchResults;

    public Map<Integer, LearningObjectDetailsBean> getLearningObjectsSearchResults() {
        return learningObjectsSearchResults;
    }

	public void setLearningObjectsSearchResults(Map<Integer, LearningObjectDetailsBean> learningObjectsSearchResults) {
		this.learningObjectsSearchResults = learningObjectsSearchResults;
	}
    
    protected Map<String, Object> session ;
    
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
	
    /**
	 * This method is configured to be invoked in struts.xml, for searching and linking learning objects
	 */
	@SuppressWarnings("unchecked")
    public String execute() throws Exception {
        //Get request method invoked
		HttpServletRequest request = ServletActionContext.getRequest();
		
        String result = ActionConstants.FORWARD_SHOWJSP;

        String method = request.getMethod();
        CourseBean course = (CourseBean)session.get(ActionConstants.SESSION_COURSE_KEY);

        dbAccessDaoInterface = new DbAccessDaoImpl();
        
        try {
            if (ActionConstants.METHOD_GET.equalsIgnoreCase(method)) { // GET path is for getting search results
                
                learningObjectsSearchResults = searchLearningObjects(keywords, typeOfLearningObject, course);
                // keep map of learning object details in session for linking purposes
                session.put(
                        ActionConstants.SESSION_LEARNING_OBJECTS_SEARCH_RESULTS_KEY,
                        learningObjectsSearchResults);
                
                result = ActionConstants.FORWARD_SHOWJSP;
                
            } else if (ActionConstants.METHOD_POST.equalsIgnoreCase(method)) { // POST path is for linking learning objects
                
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
        } catch (Exception ex) {
            log.error(ex);
            reset();                
            addActionError(ActionConstants.SEARCH_LEARNING_OBJECTS_ERROR_MSG);
            result = ActionConstants.FORWARD_INPUT;
        }
		return result;
	}

    public void reset(){
		this.learningObjectsSearchResults = null;
	}

    
    
    protected Map<Integer, LearningObjectDetailsBean> searchLearningObjects(String keywords, String typeOfLearningObject, CourseBean course) throws Exception {
        if (keywords == null || keywords.isEmpty() || typeOfLearningObject == null || typeOfLearningObject.isEmpty()) {
            return new HashMap<Integer, LearningObjectDetailsBean>();
        }

        
        // search learning objects in triple store
        TriplestoreAccessDaoInterface tdbAccessDaoInterface = new VirtuosoAccessDaoImpl();
        
        HashMap<String,TenLearningObjectAnnotationsBean> mapLearningObjects = tdbAccessDaoInterface.searchLearningObjects(typeOfLearningObject, keywords, "");
        
        // get learning object details
        Map<String, LearningObjectDetailsBean> mapLearningObjectDetails = dbAccessDaoInterface.getMapOfLearningObjects(mapLearningObjects.keySet()); 
        
        Map<Integer, LearningObjectDetailsBean> learningObjectsSearchResults = new HashMap<Integer, LearningObjectDetailsBean>(); // clear past results
        
        HashSet<LearningObjectDetailsBean> combinedLearningObjectsSearchResults = new HashSet<LearningObjectDetailsBean>();
        if (mapLearningObjectDetails.values() != null){
        	combinedLearningObjectsSearchResults.addAll(mapLearningObjectDetails.values());
        }
        
        System.out.println("Type of Learning Object is: " + typeOfLearningObject);
        
		//      Full text search text learning objects
		if (typeOfLearningObject.equals("Text")){
			ArrayList<String> semanticRelatedKeywords = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(keywords, ",");
			while (st.hasMoreTokens()) {
				String nextKeyword = st.nextToken().trim();
				semanticRelatedKeywords.addAll(tdbAccessDaoInterface.getSemanticRelatedKeywords(nextKeyword)) ;
			}
	
			HashSet<LearningObjectDetailsBean> fullTextLearningObjectsSearchResults = dbAccessDaoInterface.getLearningObjectsByFullTextSearch(semanticRelatedKeywords);
			if (fullTextLearningObjectsSearchResults != null){
				combinedLearningObjectsSearchResults.addAll(fullTextLearningObjectsSearchResults);
			}
		}

        // make search results map with key as learning object id, this is used by the view
        for (LearningObjectDetailsBean learningObject : combinedLearningObjectsSearchResults) {
            // don't show already linked objects in search results
            if (!course.isLearningObjectLinked(learningObject.getId())) {
                learningObjectsSearchResults.put(learningObject.getId(), learningObject);
            }
        }
        
        return learningObjectsSearchResults;
    }
    
    protected void parseSelectionAndLinkLearningObjectsToCourse(CourseBean course) throws Exception {
        
        HashMap<Integer, ArrayList<String>> mapOfLearningObjectIdAndCourseContentName = new HashMap<Integer, ArrayList<String>>(); 
        HashMap<Integer, ArrayList<Integer>> mapOfLearningObjectIdAndCourseObjectiveId = new HashMap<Integer, ArrayList<Integer>>();
        
        // Create map from check boxes
        // For each check box the value is the learning object Id
        // This means there is a link between the learning object and the course 
        if (learningObjectIds != null && learningObjectIds.length > 0) {
            for (String learningObjectIdString : learningObjectIds)
            {
                Integer learningObjectId = Integer.parseInt(learningObjectIdString);
                if (!mapOfLearningObjectIdAndCourseContentName.containsKey(learningObjectId)) {
                    mapOfLearningObjectIdAndCourseContentName.put(learningObjectId, new ArrayList<String>());
                    // Learning object linked to course (not a course field name), so add a ""
                    mapOfLearningObjectIdAndCourseContentName.get(learningObjectId).add("");
                }
            }
        }

        // Create map from drop down selections
        // For each drop down selection the value is the learning object Id and course content name
        // This means there is a link between the learning object and the specific course content 
        if (courseContentNames != null && courseContentNames.length > 0) {
            for (String contentName : courseContentNames)
            {
                // If selected item is not an objective then value format is {courseId}-{courseContentName}
                // Else if it is an objective then value format is {courseId}-Objective-{courseObjectiveId}
                String[] parts = contentName.split("-");
                Integer learningObjectId = Integer.parseInt(parts[0]);
                String courseContentName = parts[1];
                if (!mapOfLearningObjectIdAndCourseContentName.containsKey(learningObjectId)) {
                    continue;
                }
                if ("Objective".equals(courseContentName)) {
                    Integer courseObjectiveId = Integer.parseInt(parts[2]);
                    if (!mapOfLearningObjectIdAndCourseObjectiveId.containsKey(learningObjectId)) {
                        mapOfLearningObjectIdAndCourseObjectiveId.put(learningObjectId, new ArrayList<Integer>());
                    }
                    mapOfLearningObjectIdAndCourseObjectiveId.get(learningObjectId).add(courseObjectiveId);
                } else {
                    mapOfLearningObjectIdAndCourseContentName.get(learningObjectId).add(courseContentName);
                }
            }
        }

        // First remove existing links between learning object and course
        // The user unchecked the checkbox, so remove all existing links to the learning object
        TreeSet<Integer> learningObjectIds = new TreeSet<Integer>(course.getLearningObjectLinks().keySet());
        for (Integer learningObjectId : learningObjectIds) {
            if (!mapOfLearningObjectIdAndCourseContentName.containsKey(learningObjectId)) {
                // if map doesn't contain the learning object id, user unchecked the checkbox
                // so unlink learning object from all course content it is linked to
                dbAccessDaoInterface.unlinkLearningObjectToCourse(course.getId(), learningObjectId);
                course.removeLearningObjectLinks(learningObjectId);
                // unlink learning object from all objectives it is linked to
                for (CourseObjectiveBean objective : course.getObjectives().values()) {
                    if (objective.isLearningObjectLinked(learningObjectId)) {
                        dbAccessDaoInterface.unlinkLearningObjectToCourseObjective(objective.getId(), learningObjectId);
                        objective.removeLearningObjectLinks(learningObjectId);
                    }
                }
            }
        }
        
        // Save new links in course cultural content links table
        for (Map.Entry<Integer, ArrayList<String>> entry : mapOfLearningObjectIdAndCourseContentName.entrySet()) {
            Integer learningObjectId = entry.getKey();
            for (String courseContentName : entry.getValue()) {
                if (course.isLearningObjectLinked(learningObjectId, courseContentName)) {
                    continue; // already linked, skip
                }
                int id = dbAccessDaoInterface.linkLearningObjectToCourse(course.getId(), learningObjectId, courseContentName);
                CourseLearningObjectLinkBean link = new CourseLearningObjectLinkBean();
                link.setId(id);
                link.setLearningObject(this.learningObjectsSearchResults.get(learningObjectId));
                link.setCourseContentName(courseContentName);
                course.addLearningObjectLink(link);
            }
        }
        
        // Save new links in course objective cultural content links table
        for (Map.Entry<Integer, ArrayList<Integer>> entry : mapOfLearningObjectIdAndCourseObjectiveId.entrySet()) {
            Integer learningObjectId = entry.getKey();
            for (Integer courseObjectiveId : entry.getValue()) {
                CourseObjectiveBean courseObjective = course.getObjectives().get(courseObjectiveId);
                if (courseObjective.isLearningObjectLinked(learningObjectId)) {
                    continue; // already linked, skip
                }
                int id = dbAccessDaoInterface.linkLearningObjectToCourseObjective(courseObjectiveId, learningObjectId);
                CourseObjectiveLearningObjectLinkBean link = new CourseObjectiveLearningObjectLinkBean();
                link.setId(id);
                link.setLearningObject(this.learningObjectsSearchResults.get(learningObjectId));
                courseObjective.addLearningObjectLink(link);
            }
        }
    }
}
