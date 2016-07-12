package com.ten.dao.interfaces;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

import com.ten.beans.*;

import model.Permission;
import model.Role;

public interface DbAccessDaoInterface {
	//Method to save image details to database
	public int saveImage(File file, String fileName, String fileType, boolean annotated) throws Exception;
	
	//Method to change image status to being annotated
	public boolean updateImage(int id) throws Exception;
	
	//Method to save video to database
	public int saveVideo(File file, String fileName, String fileType, boolean annotated) throws Exception;
	
	//Method to change video status to being annotated
	public boolean updateVideo(int id) throws Exception;
 
	//Method to store CLO reference id
	public void updateLearningObjectReferenceId(int id, String referenceId) throws Exception;
	
	//Method to save audio to database
	public int saveAudio(File file, String fileName, String fileType, boolean annotated) throws Exception;
	
	//Method to change audio status to being annotated
	public boolean updateAudio(int id) throws Exception;
	
	//Method to save text to database
	public int saveText(File file, String fileName, String fileType, boolean annotated) throws Exception;
	
	//Method to change text status to being annotated
	public boolean updateText(int id) throws Exception;
	
	//Method to get unannotated images from database
	public ArrayList<LearningObjectBean> getUnannotatedImages() throws Exception;
	
	//Method to get image from database
	public LearningObjectDetailsBean getImage(int id) throws Exception;	
	
	//Method to get unannotated videos from database
	public ArrayList<LearningObjectBean> getUnannotatedVideos() throws Exception;
	
	//Method to get video from database
	public LearningObjectDetailsBean getVideo(int id) throws Exception;	
	
	//Method to get unannotated audios from database
	public ArrayList<LearningObjectBean> getUnannotatedAudios() throws Exception;
	
	//Method to get audio from database
	public LearningObjectDetailsBean getAudio(int id) throws Exception;	
	
	//Method to get unannotated texts from database
	public ArrayList<LearningObjectBean> getUnannotatedTexts() throws Exception;

	//Method to get text from database
	public LearningObjectDetailsBean getText(int id) throws Exception;

    /**
     * Get map of learning objects mapped by given Uris
     * */
    public HashMap<String, LearningObjectDetailsBean> getMapOfLearningObjects(Collection<String> uris) throws Exception;

    /**
     * Get course by its id
     * */
    public CourseBean getCourse( int courseId ) throws SQLException;

    /**
     * This method is invoked by CreateCourseAction to store the newly created course to database.
     * Method returns an integer which is the primary key of the course stored in database.
     * This course is related to its annotations stored in triple store through the image id primary key.
     */
    public int addCourse( CourseBean course ) throws Exception;

    /**
     * Saves all objectives of the course. The course objective bean ids will be set from the database.
     * */
    public void addCourseObjectives(CourseBean course, Collection<CourseObjectiveBean> courseObjectives) throws Exception;

    /**
     * Update course information for the given course.
     * */
    public void updateCourse( CourseBean course ) throws Exception;
    
    // get all course objectives
    public ArrayList<CourseObjectiveBean> getCourseObjectives(int courseId) throws Exception;

    /**
     * Get all course for the given user. The given userDetails object is assumed to be in the 'Creator' role.
     * */
    public ArrayList<CourseBean> getAllCourses()throws Exception;

	/**
     * Get all course for the given user. The given userDetails object is assumed to be in the 'Creator' role.
     * */
	public ArrayList<CourseBean> getCoursesForUser(UserDetailsBean userDetails)throws Exception;

    /**
     * Given the courseId  get all the linked course content.
     * */
    public ArrayList<CourseLearningObjectLinkBean> getCourseLearningObjectLinks( int courseId ) throws Exception;

    /**
     * Get the learning object with the given id
     * */
    public LearningObjectDetailsBean getLearningObject(int id) throws Exception;

    /**
     * Get all the cultural learning objects
     * */
    public ArrayList<LearningObjectDetailsBean> getAllLearningObjects() throws Exception;
    
    /**
     * Get all the course content-- including all the linked cultural objects for exporting.
     * */
    public File getCourseContentForExport( int courseId ) throws Exception;

    public void linkLearningObjectToCourse(CourseLearningObjectLinkBean courseLearningObjectLink) throws Exception;

    /**
     * Link the given cultural content to the given course.
     * @param courseId id of the course to link content to
     * @param learningObjectId id of the learning object to link the course to
     * @param courseFieldName field name of the course to link the cultural content to 
     * */
    public int linkLearningObjectToCourse( int courseId, int learningObjectId, String courseFieldName ) throws Exception;


    /**
     * Link the given cultural content to the given course.
     * @param courseId id of the course to link content to
     * @param culturalContentName name of the cultural content to link the culturalContentTo
     * */
    public void linkLearningObjectToCourse( int courseId, String[] culturalContentName  ) throws Exception;

    /**
     * Insert course objective
     * */
    public void insertCourseObjective(int courseId, String objectiveDescription) throws Exception;

    //Method to insert a new user in database
    public void insertUser(String userName, String firstName, String middleName, String lastName, String emailId, String userPassword, String role) throws Exception;
   
    //Method to delete a course
    public void deleteCourse(int courseId) throws Exception;
   
    /**
     * Link the given cultural content to the given course content.
     * */
   public void linkCulturalContentToCourseContent( int courseId, String courseFieldName, int culturalContentId ) throws Exception;

    /**
     * Link the given cultural content to the given course content.
     * @param courseId id of the course to link content to
     * @param courseContentId id of the course content to link content to
     * @param culturalContentName name of the cultural content to link the culturalContentTo
     * */
    public void linkCulturalContentToCourseContent( int courseId, String courseFieldName, String[] culturalContentName  ) throws Exception;

    // Link course objective to cultural objective
    public int linkLearningObjectToCourseObjective(int courseObjectiveId, int culturalContentId) throws Exception;

    /**
     * Remove all links between a learning object and a course.
     * */
    public void unlinkLearningObjectToCourse(int courseId, int learningObjectId) throws Exception;

    /**
     * Remove the given learning object links to a course.
     * */
    public void removeLearningObjectToCourseLink(Collection<Integer> linksToRemove) throws Exception;

    /**
     * Remove all links between a learning object and a course.
     * */
    public void unlinkLearningObjectToCourseObjective(int courseObjectiveId, int learningObjectId) throws Exception;

    /**
     * Given the userName get the User's details (meta data for the user).
     * */
	public UserDetailsBean getUserDetails( String userName ) throws Exception;

    /**
     * Get all roles in the system.
     * */
    public List<Role> getRoles() throws Exception;

    /**
     * Given a roleId get all the permission for this role.
     */
    public Set<Permission> getPermissionForRole( int roleId );
}
