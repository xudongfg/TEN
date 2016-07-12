package com.ten.struts2.actions;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.LearningObjectBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;

/**
 * 
 * @author Nita Karande
 * This action invoked by main.jsp 
 * It invokes method to display all unannotated audios in database
 */
public class AnnotateAudioMainAction extends ActionSupport{

	static Logger log = Logger.getLogger(AnnotateAudioMainAction.class);
	
	private static final long serialVersionUID = 1L;
	ArrayList<LearningObjectBean> learningObjects;
        
   	public ArrayList<LearningObjectBean> getLearningObjects() {
		return learningObjects;
	}

	public void setLearningObjects(ArrayList<LearningObjectBean> learningObjects) {
		this.learningObjects = learningObjects;
	}

	/**
	 * This method is configured to be invoked in struts.xml, for retrieving all the unannotated videos.
	 * It makes calls to mysql dao implementation display all the unannotated videos
	 */
	public String execute() throws Exception {
		//Get request method invoked
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String method = request.getMethod();
		String result = ActionConstants.FORWARD_SHOWJSP;
		
		if(ActionConstants.METHOD_GET.equalsIgnoreCase(method)){
			try{
				//get audios from database
				DbAccessDaoInterface dbAccessDaoInterface = new DbAccessDaoImpl();
				learningObjects = dbAccessDaoInterface.getUnannotatedAudios();
				
				//remove learning objects which do not have a story associated with it, user cannot annotate them
				//TriplestoreAccessDaoInterface tdbAccessDaoInterface = new VirtuosoAccessDaoImpl();
				//learningObjects = tdbAccessDaoInterface.removeItemsWithoutStory(learningObjects, TripleStoreConstants.URI_AUDIO);
				
				result = ActionConstants.FORWARD_SUCCESS;
			}catch(Exception ex){
				log.error(ex);
				reset();				
				addActionError(ActionConstants.RETRIEVE_AUDIOS_ERROR_MSG);
				result = ActionConstants.FORWARD_INPUT;
			}           
		}else if(ActionConstants.METHOD_POST.equalsIgnoreCase(method)){	
			//do nothing
			result = ActionConstants.FORWARD_SHOWJSP;;
		}
		return result;
	}	
	
	public void reset(){
		this.learningObjects = new ArrayList<LearningObjectBean>();
	}
}
