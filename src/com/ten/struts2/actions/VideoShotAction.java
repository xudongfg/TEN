package com.ten.struts2.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.commons.lang3.StringUtils;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import com.ten.dao.implementation.VideoShotDesign;


/**
 * Struts2 MVC Model class which handles the 
 * detect shots functionality. It sets the video 
 * file name passed in the HTTP post request 
 * from AJAX front-end to struts.xml interceptor.
 * @author Jay
 * @version 2.0
 */

public class VideoShotAction extends ActionSupport
{
	private static final long serialVersionUID = 1L;
	
	HttpServletRequest request = ServletActionContext.getRequest();
	
	private String fileName = "abc"; //the Struts2 way by getter setter.
	private String frameString = "";
	private ArrayList<Integer> tempList;
	
	public String getFileName() {
		System.out.println("Inside get: " + fileName);
		
		return fileName;
	}
	public void setFileName(String fileName) {
		System.out.println("Inside Set:" + fileName);
		this.fileName = fileName;
	}
	@Override
	public String execute() throws Exception 
	{
		
		System.out.println("Ajax request parameter incoming: " + request.getParameter("fileName"));
		//fileName = request.getParameter("fileName");
		/*
		 * The below lines can be uncommented for detecting shots  for a video.
		 * Currently it takes a while for VideoShotDesign to process 
		 * the video. This is a compute intensive task and a solution
		 * to store frame numbers associated with each video file is 
		 * considered. When a newly uploaded video file from the intaker
		 * is stored in the file system, the VideoShotDesign can be process
		 * once every time such a new content is acquired. The frame numbers
		 * can then be stored in a file system or database table for each video
		 * file name. This is proposed as a future enhancement.
		 */
		VideoShotDesign videoShotDesign = new VideoShotDesign();
		videoShotDesign.detectFrameHandler(this.fileName);
		tempList = videoShotDesign.getMergeList();
		
		for(int i = 0; i < tempList.size(); i++)
		{
			if(i == (tempList.size() - 1))
			{
				frameString += tempList.get(i).toString();
			}
			else
			{	
				frameString += tempList.get(i).toString() + ",";
			}
			System.out.println("Frame String added: " + frameString);
		}
		this.fileName = frameString;
		return Action.SUCCESS;
	}
	
	
}
