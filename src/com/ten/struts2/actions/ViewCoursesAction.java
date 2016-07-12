package com.ten.struts2.actions;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.CourseBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;

/**
 * 
 * @author Nita Karande
 * This action invoked by create_course.jsp and main.jsp
 * It invokes method to create course and store it to database and store annotations in triplestore
 */
public class ViewCoursesAction extends ActionSupport{

	static Logger log = Logger.getLogger(ViewCoursesAction.class);
	
	private static final long serialVersionUID = 1L;
	ArrayList<CourseBean> listCourses;
    
	
	public ArrayList<CourseBean> getListCourses() {
		return listCourses;
	}

	public void setListCourses(ArrayList<CourseBean> listCourse) {
		this.listCourses = listCourse;
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
		
		if(ActionConstants.METHOD_GET.equalsIgnoreCase(method)){
			try{
				//Get courses from database
				DbAccessDaoInterface dbAccessDaoInterface = new DbAccessDaoImpl();
				this.listCourses = dbAccessDaoInterface.getAllCourses();
			   //show view courses page
			   result = ActionConstants.FORWARD_SUCCESS;
			}catch(Exception ex){
				log.error(ex);
				reset();				
				addActionError(ActionConstants.VIEW_COURSE_ERROR_MSG);
				result = ActionConstants.FORWARD_INPUT;
			}           
		}else if(ActionConstants.METHOD_GET.equalsIgnoreCase(method)){	
			reset();
			result = ActionConstants.FORWARD_SHOWJSP;;
		}
		return result;
	}	
	
	public void reset(){
		this.listCourses = new ArrayList<>();
	}
}
