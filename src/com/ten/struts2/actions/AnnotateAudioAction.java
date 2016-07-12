package com.ten.struts2.actions;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.LearningObjectDetailsBean;
import com.ten.beans.TenLearningObjectAnnotationsBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;
import com.ten.triplestore.dao.implementation.VirtuosoAccessDaoImpl;
import com.ten.triplestore.dao.interfaces.TriplestoreAccessDaoInterface;

/**
 * 
 * @author Nita Karande
 * This action invoked by annotatate_audios_main.jsp
 * It get audio details from database as well as stores audio annotations in triplestore
 */
public class AnnotateAudioAction extends ActionSupport{

	static Logger log = Logger.getLogger(AnnotateAudioAction.class);
	
	private static final long serialVersionUID = 1L;
	LearningObjectDetailsBean learningObjectDetailsBean;
	int learningObjectId;
	String actionType;
	TenLearningObjectAnnotationsBean tenLearningObjectAnnotationsBean;
    private Date date;
        
   	public LearningObjectDetailsBean getLearningObjectDetailsBean() {
		return learningObjectDetailsBean;
	}

	public void setLearningObjectDetailsBean(
			LearningObjectDetailsBean learningObjectDetailsBean) {
		this.learningObjectDetailsBean = learningObjectDetailsBean;
	}

	public int getLearningObjectId() {
		return learningObjectId;
	}

	public void setLearningObjectId(int learningObjectId) {
		this.learningObjectId = learningObjectId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public TenLearningObjectAnnotationsBean getTenLearningObjectAnnotationsBean() {
		return tenLearningObjectAnnotationsBean;
	}

	public void setTenLearningObjectAnnotationsBean(
			TenLearningObjectAnnotationsBean tenLearningObjectAnnotationsBean) {
		this.tenLearningObjectAnnotationsBean = tenLearningObjectAnnotationsBean;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * This method is configured to be invoked in struts.xml, for retrieving the audio to be annotated
	 * It makes calls to mysql dao implementation to get audio content from database
	 * It also is invoked to store audio annotations to triplestore
	 */
	public String execute() throws Exception {
		//Get request method invoked
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String method = request.getMethod();
		String result = ActionConstants.FORWARD_SHOWJSP;
		
		if(ActionConstants.METHOD_POST.equalsIgnoreCase(method)){
			if(this.actionType.equals(ActionConstants.ACTION_DISPLAY)){
				try{
					this.reset();
					
					//get audio from database
					DbAccessDaoInterface dbAccessDaoInterface = new DbAccessDaoImpl();
					learningObjectDetailsBean = dbAccessDaoInterface.getAudio(this.learningObjectId);
										
					result = ActionConstants.FORWARD_SUCCESS;
				}catch(Exception ex){
					log.error(ex);
					reset();				
					addActionError(ActionConstants.RETRIEVE_AUDIOS_ERROR_MSG);
					result = ActionConstants.FORWARD_INPUT;
				}
			}else if (this.actionType.equals(ActionConstants.ACTION_ANNOTATE)){
				try{
					//save annotations to triple store
					TriplestoreAccessDaoInterface tdbAccessDaoInterface = new VirtuosoAccessDaoImpl();
					tdbAccessDaoInterface.insertAudioAnnotations(this.tenLearningObjectAnnotationsBean, this.learningObjectId);
					
					//set audio status to being annotated
					DbAccessDaoInterface dbAccessDaoInterface = new DbAccessDaoImpl();
					dbAccessDaoInterface.updateAudio(this.learningObjectId);					

					addActionMessage(ActionConstants.ANNOTATION_SUCCESS_MSG);		
					result = ActionConstants.FORWARD_SUCCESS;
				}catch(Exception ex){
					log.error(ex);
					reset();				
					addActionError(ActionConstants.ANNOTATION_ERROR_MSG);
					result = ActionConstants.FORWARD_INPUT;
				}
			}
		}else if(ActionConstants.METHOD_GET.equalsIgnoreCase(method)){	
			//do nothing
			result = ActionConstants.FORWARD_SHOWJSP;;
		}
		return result;
	}	
	
	public void reset(){
		this.learningObjectDetailsBean = null;
		this.tenLearningObjectAnnotationsBean = new TenLearningObjectAnnotationsBean();
		this.date = new Date();
	}
}
