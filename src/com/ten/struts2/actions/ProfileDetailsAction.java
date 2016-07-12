package com.ten.struts2.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.StudentAnnotationsBean;
import com.ten.triplestore.dao.implementation.VirtuosoAccessDaoImpl;
import com.ten.triplestore.dao.interfaces.TriplestoreAccessDaoInterface;
import com.ten.utils.Utils;

public class ProfileDetailsAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(ProfileDetailsAction.class);
	
	private StudentAnnotationsBean studentAnnotationsBean;

    public StudentAnnotationsBean getStudentAnnotationsBean() {
		return studentAnnotationsBean;
	}

	public void setStudentAnnotationsBean(
			StudentAnnotationsBean studentAnnotationsBean) {
		this.studentAnnotationsBean = studentAnnotationsBean;
	}

	@Override
    public String execute() throws Exception {
         HttpServletRequest request = ServletActionContext.getRequest();
         HttpSession session =  request.getSession(); 
         String user_name = request.getUserPrincipal().getName();
         
         String method = request.getMethod();
         String result = ActionConstants.FORWARD_SHOWJSP;
         
         TriplestoreAccessDaoInterface tdbAccessDaoInterface = new VirtuosoAccessDaoImpl();
      	 if(ActionConstants.METHOD_GET.equalsIgnoreCase(method)){
     		//check if user is logged in
     		if(!Utils.isEmptyOrNull(user_name)){
     			//get user details from session
     			      	
     			this.studentAnnotationsBean = (StudentAnnotationsBean)session.getAttribute(ActionConstants.KEY_STUDENT_DETAILS);
         	
     			//student annotations not present in session so update it
     			if(this.studentAnnotationsBean == null){
		        	//get student details annotations	        		
	        		this.studentAnnotationsBean  = tdbAccessDaoInterface.getStudentAnnotations(user_name);
					session.setAttribute(ActionConstants.KEY_STUDENT_DETAILS, this.studentAnnotationsBean );					
     			}        	
     			result = ActionConstants.FORWARD_SUCCESS;
	         }else{
	        	 Exception ex = new Exception();
	        	 throw ex;
	         }
     	}else{
     		try{
	     		//post action update preferences
	     		tdbAccessDaoInterface.updateStudentAnnotations(user_name,this.studentAnnotationsBean);
	     		session.setAttribute(ActionConstants.KEY_STUDENT_DETAILS, this.studentAnnotationsBean );	
	     		
	     		addActionMessage(ActionConstants.PROFILE_UPDATE_SUCCESS_MSG);
     		}catch(Exception ex){
     			log.error(ex);
     			addActionError(ActionConstants.PROFILE_UPDATE_ERROR_MSG);
				result = ActionConstants.FORWARD_INPUT;
     		}    
     	}
        return result;
    }
}
