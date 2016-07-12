package com.ten.struts2.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.StudentAnnotationsBean;
import com.ten.beans.UserDetailsBean;
import com.ten.triplestore.dao.implementation.VirtuosoAccessDaoImpl;
import com.ten.triplestore.dao.interfaces.TriplestoreAccessDaoInterface;
import com.ten.utils.Utils;

public class MainAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

    @Override
    public String execute() throws Exception {
         HttpServletRequest request = ServletActionContext.getRequest();
         String result = ActionConstants.FORWARD_ERROR;
         String user_name = request.getUserPrincipal().getName();
         
         //check if user is logged in
         if(!Utils.isEmptyOrNull(user_name)){
        	 //create user details object and store in session
        	HttpSession session =  request.getSession();        	
        	UserDetailsBean userDetailsBean = null;
        	
        	Object object = session.getAttribute(ActionConstants.KEY_USER_DETAILS);
        	//check if user details not already present
        	if((object == null)){
                DbAccessDaoInterface db = new DbAccessDaoImpl();
        		userDetailsBean = db.getUserDetails( user_name );
	        	session.setAttribute(ActionConstants.KEY_USER_DETAILS, userDetailsBean);

	        	//get student details annotations
	        	if(request.isUserInRole(ActionConstants.ROLE_STUDENT)){
	        		TriplestoreAccessDaoInterface tdbAccessDaoInterface = new VirtuosoAccessDaoImpl();
					StudentAnnotationsBean studentAnnotationsBean = tdbAccessDaoInterface.getStudentAnnotations(user_name);
					session.setAttribute(ActionConstants.KEY_STUDENT_DETAILS, studentAnnotationsBean);
	        	}
        	}
        	
        	result = ActionConstants.FORWARD_SUCCESS;
         }else{
        	 Exception ex = new Exception();
        	 throw ex;
         }
         return result;
    }
}
