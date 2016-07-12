package com.ten.struts2.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String user_name;

    public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	@Override
    public String execute() throws Exception {
         HttpServletRequest request = ServletActionContext.getRequest();
         String result = ActionConstants.FORWARD_ERROR;
         
         this.user_name = request.getUserPrincipal().getName();
         
         HttpSession session =  request.getSession();
         session.invalidate();
         
         result = ActionConstants.FORWARD_SUCCESS;
         return result;
    }
}
