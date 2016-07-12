package com.ten.beans;

import java.io.File;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 *
 * @author Nita Karande
 * @author Jay Vyas
 * @version 2.0
 */
public class LearningObjectDetailsBean extends LearningObjectBean {
	byte[] content;
	File content2;
	boolean isVideo = false;
	String fileType;
	String contentType;
	String referenceId;

	public LearningObjectDetailsBean()
	{
		super();
	}
	public LearningObjectDetailsBean(boolean isVideo)
	{
		this();
		this.isVideo = isVideo;
	}
	
	public String getReferenceId() {
        return referenceId;
    }
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
    public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContent() {
		
		/*if(isVideo == true)
			return this.content2.getAbsolutePath();*/
		
		return Base64.encode(this.content);
	}
	/*//Specifically for Video files
	public void setContent(File content)
	{
		this.content2 = content;
		isVideo = true;
	}*/
	public void setContent(byte[] content) {
		//isVideo = false;
		this.content = content;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}	
}
