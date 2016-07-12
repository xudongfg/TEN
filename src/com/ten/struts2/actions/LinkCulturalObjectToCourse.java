package com.ten.struts2.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.CourseLearningObjectLinkBean;
import com.ten.beans.UserDetailsBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

/**
 * This action facilitates in linking a cultural object to a course.
 */
public class LinkCulturalObjectToCourse extends ActionSupport{

	static Logger log = Logger.getLogger(LinkCulturalObjectToCourse.class);
	
	private static final long serialVersionUID = 1L;

    int courseId;
    int culturalContentId;
    String courseContentFieldNames; 
    int courseContentId;



	public int getCourseContentId() {
        return courseContentId;
    }

    public void setCourseContentId(int courseContentId) {
        this.courseContentId = courseContentId;
    }

    public String getCourseContentFieldNames() {
		return courseContentFieldNames;
	}

	public void setCourseContentFieldNames(String courseContentName) {
		this.courseContentFieldNames = courseContentName;
	}

	public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCulturalContentId() {
        return culturalContentId;
    }

    public void setCulturalContentId(int culturalContentId) {
        this.culturalContentId = culturalContentId;
    }
	
	public String execute() throws Exception {
        //Get request method invoked
        HttpServletRequest request = ServletActionContext.getRequest();

        String method = request.getMethod();
        String result = ActionConstants.FORWARD_SHOWJSP;

        if (ActionConstants.METHOD_POST.equalsIgnoreCase(method)) {
            try {
                DbAccessDaoInterface dbAccessDaoInterface = new DbAccessDaoImpl();
                if (courseContentFieldNames != null && !courseContentFieldNames.isEmpty()) {
                    dbAccessDaoInterface.linkLearningObjectToCourse(this.courseId, this.culturalContentId, "");
                }
                else {
                    String[] courseFieldNames = this.courseContentFieldNames.split(",");
                    for (String fieldName : courseFieldNames) {
                        dbAccessDaoInterface.linkLearningObjectToCourse(this.courseId, this.culturalContentId, fieldName);
                    }
                }
                addActionMessage(ActionConstants.CULTURAL_CONTENT_LINKED_SUCCESS_MSG);
                result = ActionConstants.FORWARD_SUCCESS;
            } catch (Exception ex) {
                log.error(ex);
                addActionError(ActionConstants.CULTURAL_CONTENT_LINKED_ERROR_MSG);
                result = ActionConstants.FORWARD_INPUT;
            }
        } else if (ActionConstants.METHOD_GET.equalsIgnoreCase(method)) {
            result = ActionConstants.FORWARD_SHOWJSP;
        }
        return result;
    }

}
