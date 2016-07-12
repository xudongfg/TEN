package com.ten.struts2.actions;

/**
 * @author Nita Karande
 * This class contains constants used in action classes
 */
public interface ActionConstants {
	public static final String PATH_IMAGES = "C:/TEN/UploadedImages/";
	
	public static final String PATH_AUDIO = "C:/TEN/UploadedAudios/";
	
	public static final String PATH_VIDEOS = "C:/TEN/UploadedVideos/";
	
	public static final String PATH_TEXT = "C:/TEN/UploadedText/";
	
	public static final String ROLE_INTAKER = "Intaker";
	
	public static final String ROLE_ANNOTATOR = "Annotator";
	
	public static final String ROLE_CREATOR = "Creator";
	
	public static final String ROLE_STUDENT = "Student";
	
	public static final String ROLE_ADMIN = "Admin";
	
	public static final String ROLE_MENTOR = "mentor";
	
	public static final String[] ALL_ROLES = {ROLE_INTAKER, ROLE_ANNOTATOR, ROLE_CREATOR, ROLE_STUDENT, ROLE_MENTOR};
	
	public static final String KEY_USER_DETAILS = "user_details";
	
	public static final String KEY_STUDENT_DETAILS = "student_details";
	
	public static final String KEY_DIGITAL_RIGHTS_MANAGEMENT = "digital_rights_management";
	
	public static final String FILE_UPLOAD_SUCCESS_MSG = "File uploaded successfully ";
	
	public static final String PROFILE_UPDATE_SUCCESS_MSG = "Profile updated successfully ";
	
	public static final String PROFILE_UPDATE_ERROR_MSG = "Profile could not be updated ";
	
    public static final String DIGITAL_RIGHTS_MANAGEMENT_UPDATE_SUCCESS_MSG = "Digital rights management updated successfully ";
	
	public static final String DIGITAL_RIGHTS_MANAGEMENT_UPDATE_ERROR_MSG = "Digital rights management could not be updated ";
	
	public static final String CREATE_ACCOUNT_SUCCESS_MSG = "Account created successfully ";
	    
	public static final String CREATE_ACCOUNT_ERROR_MSG = "Failed to create account ";  

	public static final String PROFILE_RETRIEVE_ERROR_MSG = "Profile could not be loaded ";
	
	public static final String FILE_UPLOAD_ERROR_MSG = "File upload failed ";
	
	public static final String RETRIEVE_IMAGES_ERROR_MSG = "Failed to retrieve images ";
	
	public static final String RETRIEVE_AUDIOS_ERROR_MSG = "Failed to retrieve audios ";
	
	public static final String RETRIEVE_VIDEOS_ERROR_MSG = "Failed to retrieve videos ";
	
	public static final String RETRIEVE_TEXTS_ERROR_MSG = "Failed to retrieve texts ";
	
	public static final String ANNOTATION_SUCCESS_MSG = "Annotation was successfull ";
	
	public static final String ANNOTATION_ERROR_MSG = "Annotation failed ";
	
	public static final String CREATE_COURSE_SUCCESS_MSG = "Course created successfully ";

    public static final String CULTURAL_CONTENT_LINKED_SUCCESS_MSG = "Cultural content linked to course successfully";

    public static final String CULTURAL_CONTENT_LINKED_ERROR_MSG = "Failed to link cultural content to course (could possibly have been done already).";

    public static final String CREATE_COURSE_ERROR_MSG = "Failed to create course ";
	
	public static final String VIEW_COURSE_ERROR_MSG = "Failed to view courses ";
	
	public static final String SEARCH_LEARNING_OBJECTS_ERROR_MSG = "Learning objects cannot be searched ";
	
	public static final String ACTION_DISPLAY = "display";	
	
	public static final String ACTION_ANNOTATE = "annotate";
	
	public static final String METHOD_POST = "POST";
	
	public static final String METHOD_GET = "GET";
	
	public static final String FORWARD_SHOWJSP = "showjsp";
	
	public static final String FORWARD_INPUT = "input";
	
	public static final String FORWARD_SUCCESS = "success";
	
	public static final String DELETE_COURSE = "delete";
	
	public static final String FORWARD_NEXT_ACTION = "nextaction";
	
	public static final String FORWARD_ERROR = "error";

    public static final String DOWNLOAD_COURSE = "downloadCourse";

    public static final String SESSION_COURSE_KEY = "course";

    public static final String SESSION_LEARNING_OBJECTS_SEARCH_RESULTS_KEY = "learningObjectsSearchResults";
}
