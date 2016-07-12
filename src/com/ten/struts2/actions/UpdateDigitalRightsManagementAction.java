package com.ten.struts2.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.DigitalRightsManagementBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;
import com.ten.triplestore.dao.implementation.VirtuosoAccessDaoImpl;
import com.ten.triplestore.dao.interfaces.TriplestoreAccessDaoInterface;

public class UpdateDigitalRightsManagementAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(ProfileDetailsAction.class);
	
	private DigitalRightsManagementBean digitalRightsManagementBean;
	
	int learningObjectId;
	String learningObjectContentType;
    private String cloReferenceId;
    
	public String getCloReferenceId() {
        return cloReferenceId;
    }

    public void setCloReferenceId(String cloReferenceId) {
        this.cloReferenceId = cloReferenceId;
    }

    public int getLearningObjectId() {
		return learningObjectId;
	}

	public void setLearningObjectId(int learningObjectId) {
		this.learningObjectId = learningObjectId;
	}
    
    public String getLearningObjectContentType() {
        return learningObjectContentType;
    }

    public void setLearningObjectContentType(String learningObjectContentType) {
        this.learningObjectContentType = learningObjectContentType;
    }

	public DigitalRightsManagementBean getDigitalRightsManagementBean() {
		return digitalRightsManagementBean;
	}

	public void setDigitalRightsManagementBean(DigitalRightsManagementBean digitalRightsManagementBean) {
		this.digitalRightsManagementBean = digitalRightsManagementBean;
	}

	@Override
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
         
        String method = request.getMethod();
        String result = ActionConstants.FORWARD_SHOWJSP;
        
        DbAccessDaoInterface dbAccessDaoInterface = new DbAccessDaoImpl();

        TriplestoreAccessDaoInterface tdbAccessDaoInterface = new VirtuosoAccessDaoImpl();
        
      	if (ActionConstants.METHOD_GET.equalsIgnoreCase(method)) {
      	    try {
      	        this.digitalRightsManagementBean = tdbAccessDaoInterface.getDigitalRightsManagement(
      	                this.getLearningObjectId(),
      	                this.getLearningObjectContentType());
     		} catch(Exception ex) {
     		    log.error(ex);
     		    addActionError(ActionConstants.DIGITAL_RIGHTS_MANAGEMENT_UPDATE_ERROR_MSG);
     		    result = ActionConstants.FORWARD_INPUT;
     		}
      	} else if (ActionConstants.METHOD_POST.equalsIgnoreCase(method)) {
            try {
                tdbAccessDaoInterface.updateDigitalRightsManagement(
                        this.getLearningObjectId(),
                        this.getLearningObjectContentType(),
                        this.getDigitalRightsManagementBean());
                this.cloReferenceId = this.digitalRightsManagementBean.getTribe() + "-" + String.format("%06d", this.getLearningObjectId()) + "-" + this.digitalRightsManagementBean.getDateOfUpload().substring(2).replaceAll("-","") + "-" + this.getLearningObjectContentType();
                dbAccessDaoInterface.updateLearningObjectReferenceId(this.getLearningObjectId(), this.cloReferenceId);
                addActionMessage(ActionConstants.DIGITAL_RIGHTS_MANAGEMENT_UPDATE_SUCCESS_MSG +  ". The new reference id of the cultural learning object is: " + this.cloReferenceId);
            } catch(Exception ex) {
                log.error(ex);
                addActionError(ActionConstants.DIGITAL_RIGHTS_MANAGEMENT_UPDATE_ERROR_MSG);
                result = ActionConstants.FORWARD_INPUT;
            }
      	}
        return result;
    }
}
