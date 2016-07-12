package com.ten.struts2.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.ten.beans.DigitalRightsManagementBean;
import com.ten.dao.implementation.DbAccessDaoImpl;
import com.ten.dao.interfaces.DbAccessDaoInterface;
import com.ten.triplestore.dao.implementation.VirtuosoAccessDaoImpl;
import com.ten.triplestore.dao.interfaces.TriplestoreAccessDaoInterface;
/**
 * 
 * @author Nita Karande
 * This action invoked by upload_video.jsp 
 * It invokes method to upload video to database and store annotations in triplestore
 */
public class UploadVideoAction extends ActionSupport{

	static Logger log = Logger.getLogger(UploadVideoAction.class);
	
	private static final long serialVersionUID = 1L;
	private File file;
    private String contentType;
    private String fileName;
    private DigitalRightsManagementBean digitalRightsManagementBean;
    private Date date;
    private String creatorSameAs;
    private String contributorSameAs;
    private String publisherSameAs;
    private String cloReferenceId;
    
    public String getCloReferenceId() {
		return cloReferenceId;
	}

	public void setCloReferenceId(String cloReferenceId) {
		this.cloReferenceId = cloReferenceId;
	}

        
    public String getCreatorSameAs() {
		return creatorSameAs;
	}

	public void setCreatorSameAs(String creatorSameAs) {
		this.creatorSameAs = creatorSameAs;
	}

	public String getContributorSameAs() {
		return contributorSameAs;
	}

	public void setContributorSameAs(String contributorSameAs) {
		this.contributorSameAs = contributorSameAs;
	}

	public String getPublisherSameAs() {
		return publisherSameAs;
	}

	public void setPublisherSameAs(String publisherSameAs) {
		this.publisherSameAs = publisherSameAs;
	}

	public void setUpload(File file) {
       this.file = file;
    }

	public File getUpload() {
		return file;
	}
	
    public void setUploadContentType(String contentType) {
       this.contentType = contentType;
    }

	public String getUploadContentType() {
		return contentType;
	}

    public void setUploadFileName(String filename) {
       this.fileName = filename;
    }

	public String getUploadFileName() {
		return fileName;
	}

	public DigitalRightsManagementBean getDigitalRightsManagementBean() {
		return digitalRightsManagementBean;
	}

	public void setDigitalRightsManagementBean(
			DigitalRightsManagementBean digitalRightsManagementBean) {
		this.digitalRightsManagementBean = digitalRightsManagementBean;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * This method is configured to be invoked in struts.xml, for video file uploading and annotations.
	 * It makes calls to mysql dao implementation to store the uploaded file to database
	 * It also stores annotations for video in triple store 
	 */
	public String execute() throws Exception {
		//Get request method invoked
		HttpServletRequest request = ServletActionContext.getRequest();
		String method = request.getMethod();
		String result = ActionConstants.FORWARD_SHOWJSP;
		
		if(ActionConstants.METHOD_POST.equalsIgnoreCase(method)){
			try{
				//Insert video into rdbms database
				DbAccessDaoInterface dbAccessDaoInterface = new DbAccessDaoImpl();
				int videoId = dbAccessDaoInterface.saveVideo(this.file, this.fileName,this.contentType,false);				
	
				//Insert annotation data in Triplestore
				TriplestoreAccessDaoInterface tdbAccessDaoInterface = new VirtuosoAccessDaoImpl();
				tdbAccessDaoInterface.insertVideoDigitalRightsManagementData(this.digitalRightsManagementBean, videoId);

				this.cloReferenceId = this.digitalRightsManagementBean.getTribe() + "-" + String.format("%06d", videoId) + "-" + this.digitalRightsManagementBean.getDateOfUpload().substring(2).replaceAll("-","") + "-" + "Video";
				
				//Insert CLO reference id into Mysql database
				dbAccessDaoInterface.updateLearningObjectReferenceId(videoId, this.cloReferenceId);
				
				
				//File uploaded successfully
				addActionMessage(ActionConstants.FILE_UPLOAD_SUCCESS_MSG +  ". The reference id of the cultural learning object is: " + cloReferenceId);
				result = ActionConstants.FORWARD_SUCCESS;
			}catch(Exception ex){
				log.error(ex);
				reset();				
				addActionError(ActionConstants.FILE_UPLOAD_ERROR_MSG);
				result = ActionConstants.FORWARD_INPUT;
			}           
		}else if(ActionConstants.METHOD_GET.equalsIgnoreCase(method)){	
			reset();
			result = ActionConstants.FORWARD_SHOWJSP;;
		}
		return result;
	}	
	
	public void reset(){
		this.contentType = "";
		this.fileName = "";
		this.file = null;
		digitalRightsManagementBean = new DigitalRightsManagementBean();
		this.date = new Date();
	}
}
