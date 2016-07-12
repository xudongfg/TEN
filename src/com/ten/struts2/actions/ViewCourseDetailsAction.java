package com.ten.struts2.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.CourseAnnotationsBean;
import com.ten.beans.LearningObjectDetailsBean;
import com.ten.beans.StudentAnnotationsBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;
import com.ten.triplestore.dao.implementation.VirtuosoAccessDaoImpl;
import com.ten.triplestore.dao.interfaces.TriplestoreAccessDaoInterface;

/**
 * 
 * @author Nita Karande
 * This action invoked by upload_image.jsp 
 * It invokes method to upload image to database and store annotations in triplestore
 */
public class ViewCourseDetailsAction extends ActionSupport{

	static Logger log = Logger.getLogger(ViewCourseDetailsAction.class);
	
	private static final long serialVersionUID = 1L;
	private String typeOfLearningObject;
	private String courseName;
	private String courseId;
	private String actionName;
	private CourseAnnotationsBean courseAnnotationsBean;
	private HashMap<String, HashMap<String, LearningObjectDetailsBean>> mapLearningObjects;
	private HashMap<String, HashMap<String, LearningObjectDetailsBean>> mapDefaultLearningObjects;
	int recommendedLearningObjects;
	int defaultLearningObjects;

	public HashMap<String, HashMap<String, LearningObjectDetailsBean>> getMapDefaultLearningObjects() {
		return mapDefaultLearningObjects;
	}

	public int getRecommendedLearningObjects() {
		return recommendedLearningObjects;
	}

	public void setRecommendedLearningObjects(int recommendedLearningObjects) {
		this.recommendedLearningObjects = recommendedLearningObjects;
	}

	public int getDefaultLearningObjects() {
		return defaultLearningObjects;
	}

	public void setDefaultLearningObjects(int defaultLearningObjects) {
		this.defaultLearningObjects = defaultLearningObjects;
	}

	public void setMapDefaultLearningObjects(
			HashMap<String, HashMap<String, LearningObjectDetailsBean>> mapDefaultLearningObjects) {
		this.mapDefaultLearningObjects = mapDefaultLearningObjects;
	}

	public String getTypeOfLearningObject() {
		return typeOfLearningObject;
	}

	public void setTypeOfLearningObject(String typeOfLearningObject) {
		this.typeOfLearningObject = typeOfLearningObject;
	}

	public String getCourseName() {
		return courseName;
	}

	public HashMap<String, HashMap<String, LearningObjectDetailsBean>> getMapLearningObjects() {
		return mapLearningObjects;
	}

	public void setMapLearningObjects(
			HashMap<String, HashMap<String, LearningObjectDetailsBean>> mapLearningObjects) {
		this.mapLearningObjects = mapLearningObjects;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public CourseAnnotationsBean getCourseAnnotationsBean() {
		return courseAnnotationsBean;
	}

	public void setCourseAnnotationsBean(CourseAnnotationsBean courseAnnotationsBean) {
		this.courseAnnotationsBean = courseAnnotationsBean;
	}

	/**
	 * This method is configured to be invoked in struts.xml, for image file uploading and annotations.
	 * It makes calls to mysql dao implementation to store the uploaded file to database
	 * It also stores annotations for image in triple store 
	 */
	public String execute() throws Exception {
		//Get request method invoked
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String method = request.getMethod();
		String result = ActionConstants.FORWARD_SHOWJSP;
		
		if(ActionConstants.METHOD_POST.equalsIgnoreCase(method)){
			try{
				TriplestoreAccessDaoInterface tdbAccessDaoInterface = new VirtuosoAccessDaoImpl();
				
				//get course annotations from triple store						
				this.courseAnnotationsBean = tdbAccessDaoInterface.getCourseAnnotations(Integer.parseInt(this.courseId));
					
				if("view".equals(this.actionName)){
									
					//get student annotation details from session
					StudentAnnotationsBean studentAnnotationsBean = (StudentAnnotationsBean)request.getSession().getAttribute(ActionConstants.KEY_STUDENT_DETAILS);
					
					//get recommended learning objects from triple stores
					HashMap<String,ArrayList<String>> mapLearningObjectsTemp = tdbAccessDaoInterface.queryRecommendedLearningObjects(studentAnnotationsBean, this.courseAnnotationsBean.getKeywords());
					
					//get recommended learning object contents
					DbAccessDaoInterface dbAccessDaoInterface = new DbAccessDaoImpl();
					this.mapLearningObjects = new HashMap<String, HashMap<String, LearningObjectDetailsBean>>();
					for(String learningObjectType:mapLearningObjectsTemp.keySet())
					{
						//for each learning object type
						HashMap<String, LearningObjectDetailsBean> map = new HashMap<String, LearningObjectDetailsBean>();
						ArrayList<String> learningObjectsUriList = mapLearningObjectsTemp.get(learningObjectType);
						if((learningObjectsUriList!=null) && (learningObjectsUriList.size()>0))
						{
							recommendedLearningObjects += learningObjectsUriList.size();
							//get details for each object
							map = dbAccessDaoInterface.getMapOfLearningObjects(learningObjectsUriList);
						}
						this.mapLearningObjects.put(learningObjectType, map);
					}
					
					//get default learning objects from triple stores
					mapLearningObjectsTemp = tdbAccessDaoInterface.queryDefaultLearningObjects(this.courseAnnotationsBean.getKeywords(), "");
					
					//get default learning object contents
					this.mapDefaultLearningObjects = new HashMap<String, HashMap<String, LearningObjectDetailsBean>>();
					for(String learningObjectType:mapLearningObjectsTemp.keySet())
					{
						//for each learning object type
						HashMap<String, LearningObjectDetailsBean> map = new HashMap<String, LearningObjectDetailsBean>();
						ArrayList<String> learningObjectsUriList = mapLearningObjectsTemp.get(learningObjectType);						
						if((learningObjectsUriList!=null) && (learningObjectsUriList.size()>0)){
							//if there are objects matching criteria
							HashSet<String> learningObjectsUriSet =  new HashSet<String>(learningObjectsUriList);
							
							//remove those already recommended
							learningObjectsUriSet.removeAll(mapLearningObjects.get(learningObjectType).keySet());
							ArrayList<String> learningObjectsUriNotInRecommended = new ArrayList<String>(learningObjectsUriSet);
							
							//get details for remaining
							if((learningObjectsUriNotInRecommended!=null) && (learningObjectsUriNotInRecommended.size()>0))
							{
								defaultLearningObjects += learningObjectsUriNotInRecommended.size();
								map = dbAccessDaoInterface.getMapOfLearningObjects(learningObjectsUriNotInRecommended);
							}
						}
						this.mapDefaultLearningObjects.put(learningObjectType, map);
					}					
				}
								
	            result = ActionConstants.FORWARD_SUCCESS;
			}catch(Exception ex){
				log.error(ex);
				reset();				
				addActionError(ActionConstants.SEARCH_LEARNING_OBJECTS_ERROR_MSG);
				result = ActionConstants.FORWARD_INPUT;
			}           
		}else if(ActionConstants.METHOD_GET.equalsIgnoreCase(method)){	
			reset();
			result = ActionConstants.FORWARD_SHOWJSP;;
		}
		return result;
	}	
	
	public void reset(){
		this.typeOfLearningObject = "";
		this.mapLearningObjects = null;
		this.mapDefaultLearningObjects = null;
		this.courseAnnotationsBean = null;
		this.defaultLearningObjects = 0;
		this.recommendedLearningObjects = 0;
	}
}
