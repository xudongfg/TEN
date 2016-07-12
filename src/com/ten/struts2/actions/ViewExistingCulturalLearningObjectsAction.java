package com.ten.struts2.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.LearningObjectDetailsBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;

/**
 * 
 * @author Salma Bashar
 */
public class ViewExistingCulturalLearningObjectsAction extends ActionSupport {

	static Logger log = Logger.getLogger(ViewCoursesAction.class);
	
	private static final long serialVersionUID = 1L;

	private List<LearningObjectDetailsBean> learningObjects;
	
	public List<LearningObjectDetailsBean> getLearningObjects() {
		return learningObjects;
	}

	public void setLearningObjects(List<LearningObjectDetailsBean> learningObjects) {
		this.learningObjects = learningObjects;
	}

	public String execute() throws Exception {
		//Get request method invoked
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String method = request.getMethod();
		String result = ActionConstants.FORWARD_SHOWJSP;
		
		if (ActionConstants.METHOD_GET.equalsIgnoreCase(method)) {
			try {
				
				DbAccessDaoInterface dbAccessDaoInterface = new DbAccessDaoImpl();
				this.learningObjects = dbAccessDaoInterface.getAllLearningObjects();
				
			    //show view courses page
			    result = ActionConstants.FORWARD_SUCCESS;
			} catch(Exception ex) {
				log.error(ex);
				reset();				
				addActionError(ActionConstants.VIEW_COURSE_ERROR_MSG);
				result = ActionConstants.FORWARD_INPUT;
			}           
		}
		
		return result;
	}	
	
	public void reset(){
		this.learningObjects = new ArrayList<LearningObjectDetailsBean>();
	}
}
