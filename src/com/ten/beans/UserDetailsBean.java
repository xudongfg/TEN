package com.ten.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import model.Permission;

import com.ten.struts2.actions.ActionConstants;
/**
 * @author Nita Karande
 * This is the bean used data transfer between jsp and action classes
 * This class will fields for dublin core annotations
 */
public class UserDetailsBean {
	String user_name;
    String firstName;
    String lastName;
    String middleName;
    String emailId;
    String role;
    

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    int id;
    int roleId;

	ArrayList<String> roles;
	boolean userIntaker;
	boolean userAnnotator;
	boolean userCreator;
	boolean userStudent;
	boolean userMentor;
	
	public UserDetailsBean(){
		roles = new ArrayList<String>();
		user_name = "";
		userIntaker = false;
		userAnnotator = false;
		userCreator = false;
		userStudent = false;
		userMentor = false;
	}
	
	public int getId() {
		return id;
	}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setId(int id) {
		this.id = id;
	}

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

   public void addRole( String role ){
        if( !this.roles.contains(role) ){
            this.roles.add(role);

            if(ActionConstants.ROLE_INTAKER.equals(role)){
                userIntaker = true;
            }

            if(ActionConstants.ROLE_ANNOTATOR.equals(role)){
                userAnnotator = true;
            }

            if( ActionConstants.ROLE_CREATOR.equals(role)){
                userCreator = true;
            }

            if(ActionConstants.ROLE_MENTOR.equals(role)){
                userMentor = true;
            }

            if(ActionConstants.ROLE_STUDENT.equals(role)){
                userStudent = true;
            }
        }
    }
   
    
    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
        for(String role:roles){
            if(role.equals(ActionConstants.ROLE_INTAKER)){
                userIntaker = true;
            }
            
            if(role.equals(ActionConstants.ROLE_ANNOTATOR)){
                userAnnotator = true;
            }
            
            if(role.equals(ActionConstants.ROLE_CREATOR)){
                userCreator = true;
            }
            
            if(role.equals(ActionConstants.ROLE_MENTOR)){
                userMentor = true;
            }
            
            if(role.equals(ActionConstants.ROLE_STUDENT)){
                userStudent = true;
            }
        }
    }   

	
	public boolean isUserIntaker() {
		return userIntaker;
	}

	public boolean isUserAnnotator() {
		return userAnnotator;
	}

	public boolean isUserCreator() {
		return userCreator;
	}

	public boolean isUserStudent() {
		return userStudent;
	}

	public boolean isUserMentor() {
		return userMentor;
	}

	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public ArrayList<String> getRoles() {
		return roles;
	}	
	
   public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
