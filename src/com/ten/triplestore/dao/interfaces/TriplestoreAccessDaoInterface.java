package com.ten.triplestore.dao.interfaces;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.ten.beans.CourseAnnotationsBean;
import com.ten.beans.DigitalRightsManagementBean;
import com.ten.beans.LearningObjectBean;
import com.ten.beans.StudentAnnotationsBean;
import com.ten.beans.TenLearningObjectAnnotationsBean;

public interface TriplestoreAccessDaoInterface {
	public ArrayList<String> queryLearningObject(String learningObjectType, ArrayList<String> orSearchTerms,  ArrayList<String> andSearchTerms) throws Exception;	
	
	//Insert image digital rights management data in triple store
	public boolean insertImageDigitalRightsManagementData(DigitalRightsManagementBean digitalRightsManagementBean, int imageId) throws Exception;
		
	//Insert image annotations data in triple store
	public boolean insertImageAnnotations(TenLearningObjectAnnotationsBean learningObjectAnnotationsBean, int imageId) throws Exception;
	
	//Insert video digital rights management data in triple store
	public boolean insertVideoDigitalRightsManagementData(DigitalRightsManagementBean digitalRightsManagementBean, int videoId) throws Exception;
	
	//Insert video digital rights management data in triple store
	public boolean insertVideoAnnotations(TenLearningObjectAnnotationsBean TenLearningObjectAnnotationsBean, int videoId) throws Exception;
	
	//Insert audio digital rights management data in triple store
	public boolean insertAudioDigitalRightsManagementData(DigitalRightsManagementBean digitalRightsManagementBean, int audioId) throws Exception;
	
	//Insert audio annotations data in triple store
	public boolean insertAudioAnnotations(TenLearningObjectAnnotationsBean TenLearningObjectAnnotationsBean, int audioId) throws Exception;
	
	//Insert text digital rights management data in triple store
	public boolean insertTextDigitalRightsManagementData(DigitalRightsManagementBean digitalRightsManagementBean, int textId) throws Exception;
	
	//Insert text annotations data in triple store
	public boolean insertTextAnnotations(TenLearningObjectAnnotationsBean TenLearningObjectAnnotationsBean, int textId) throws Exception;
	
	//Insert course annotations
	public boolean insertCourseAnnotations(CourseAnnotationsBean courseAnnotationsBean, int courseId) throws Exception;
	
	//get course annotations
	public CourseAnnotationsBean getCourseAnnotations(int courseId) throws Exception;
	
	//get student annotations
	public StudentAnnotationsBean getStudentAnnotations(String user_name) throws Exception;
		
	//Search learning objects
	public HashMap<String, TenLearningObjectAnnotationsBean> searchLearningObjects(String type, String orKeywords, String andKeywords) throws Exception;
	
	//get recommended learning objects for students
	public HashMap<String, ArrayList<String>> queryRecommendedLearningObjects(StudentAnnotationsBean studentAnnotationsBean, String keywords) throws Exception;
	
	//update student preferences
	public void updateStudentAnnotations(String user_name, StudentAnnotationsBean studentAnnotationsBean) throws Exception;
	
	//get digital rights management
    public DigitalRightsManagementBean getDigitalRightsManagement(int id, String learningObjectType) throws Exception;
    
	//update digital rights management
	public void updateDigitalRightsManagement(int id, String learningObjectType, DigitalRightsManagementBean digitalRightsManagementBean) throws Exception;

    //delete course annotation
    public void deleteCourseAnnotations(int id) throws Exception;
		
	public  HashMap<String, ArrayList<String>> queryDefaultLearningObjects(String keywords, String andSearchTerms) throws Exception;
	
	public ArrayList<LearningObjectBean> removeItemsWithoutStory(ArrayList<LearningObjectBean> learningObjects, String learningObjectType) throws Exception;
	
//	Get semantic related keywords
	public ArrayList<String> getSemanticRelatedKeywords(String keyword) throws IOException;
}
