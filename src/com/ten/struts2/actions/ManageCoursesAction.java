package com.ten.struts2.actions;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.ten.beans.LearningObjectDetailsBean;
import com.ten.beans.UserDetailsBean;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.CourseBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;

/**
 * This action is invoked by manage_courses_main.jsp
 * It gets a list of courses from the database to show to the user
 */
public class ManageCoursesAction extends ActionSupport{

    static Logger log = Logger.getLogger(ManageCoursesAction.class);

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

        if (ActionConstants.METHOD_GET.equalsIgnoreCase(method)) {
            try{
                //Get courses from database for this user
                String user_name = request.getUserPrincipal().getName();

                DbAccessDaoInterface db = new DbAccessDaoImpl();
                UserDetailsBean userDetailsBean = db.getUserDetails( user_name );
                this.listCourses = db.getCoursesForUser( userDetailsBean );

                //show view courses page
                result = ActionConstants.FORWARD_SUCCESS;
            } catch(Exception ex) {
                log.error(ex);
                addActionError(ActionConstants.VIEW_COURSE_ERROR_MSG);
                result = ActionConstants.FORWARD_INPUT;
            }
        }
        return result;
    }
}
