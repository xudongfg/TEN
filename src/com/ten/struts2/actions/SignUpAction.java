package com.ten.struts2.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.UserDetailsBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;

/**
 * 
 * @author Salma Bashar
 * This action is invoked by signupscreen.jsp
 * It creates a new user and stores it in the database
 */
public class SignUpAction extends ActionSupport{

    static Logger log = Logger.getLogger(SignUpAction.class);
    
    private static final long serialVersionUID = 1L;
    
    private UserDetailsBean userDetailsBean;
    
    public UserDetailsBean getUserDetailsBean() {
        return userDetailsBean;
    }

    public void setUserDetailsBean(UserDetailsBean userDetailsBean) {
        this.userDetailsBean = userDetailsBean;
    }

    String userName;
    String firstName;
    String middleName;
    String lastName;
    String emailId;
    String userPassword;
    String rePassword;
    String role;
    int roleId;


    boolean userIntaker;
    boolean userAnnotator;
    boolean userCreator;
    boolean userStudent;
    
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    
    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }   

    /**
     * This method is configured to be invoked in struts.xml, for creating a new user account
     * It makes calls to mysql dao implementation to store the new user in the database
     */
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        
        String method = request.getMethod();
        String result = ActionConstants.FORWARD_SHOWJSP;
        
        if(ActionConstants.METHOD_POST.equalsIgnoreCase(method)){
            try {
                //Insert new user into Mysql database
                DbAccessDaoInterface dbAccessDaoInterface = new DbAccessDaoImpl();
                dbAccessDaoInterface.insertUser(this.userName, this.firstName, this.middleName, this.lastName, this.emailId, this.userPassword, this.role);
                // this.getUserDetailsBean().addRole(this.role);         
                //user created successfully
                addActionMessage(ActionConstants.CREATE_ACCOUNT_SUCCESS_MSG);
                result = ActionConstants.FORWARD_SUCCESS;
            } catch(Exception ex) {
                log.error(ex);
                reset();
                addActionError(ActionConstants.CREATE_ACCOUNT_ERROR_MSG);
                result = ActionConstants.FORWARD_INPUT;
            }           
        } else if(ActionConstants.METHOD_GET.equalsIgnoreCase(method)) {  
            reset();
            result = ActionConstants.FORWARD_SHOWJSP;;
        }
        return result;
    }   
    
    public void reset(){
        this.userName = "";
        this.firstName = "";
        this.middleName = "";
        this.lastName = "";
        this.emailId = "";
        this.userPassword = "";
        this.role = "";
    }
}
