package com.ten.dao.implementation;

import java.io.File;

/**
 * @author Nita Karande
 * This class contains constants used in dao classes 
 * @author Jay Vyas
 * @version 2.0
 * Added constants for file upload location.
 */
public interface DaoConstants {
	public static final String DB_JNDI_LOOKUP_NAME = "jdbc/TenDB";
		
	public static final String INSERT_IMAGE_PROCEDURE_CALL = "call INSERT_IMAGE(?,?,?,?,?)";
	
	public static final String INSERT_VIDEO_PROCEDURE_CALL = "call INSERT_VIDEO(?,?,?,?,?)";
	
	public static final String INSERT_AUDIO_PROCEDURE_CALL = "call INSERT_AUDIO(?,?,?,?,?)";
	
	public static final String INSERT_TEXT_PROCEDURE_CALL = "call INSERT_TEXT(?,?,?,?,?,?)";
	
	public static final String INSERT_USER_PROCEDURE_CALL = "call INSERT_USER(?,?,?,?,?,?,?)";
	
	public static final String DELETE_COURSE_PROCEDURE_CALL = "call DELETE_COURSE(?)";
	
    public static final String UPDATE_CLO_REFERENCE_ID = "update culturalcontent set ReferenceId=? where Id=?";
	
	public static final String INSERT_COURSE_SQL = "insert into course(OwnerId, Name, Description, Prerequisites, Topics, Overview, Timeline, IndividualAssignments, GroupAssignemnts, OnsiteAssignments," +
            "Exams, Quizzes, KnowledgeCheckpoints, GradingRubric, TechnologyRequirements, SupportServices, EvaluationFormat,ProgressMeterBadges)" +
            " values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?) ;";
    
    public static final String INSERT_COURSE_CULTURAL_CONTENT_LINK = "insert into courseculturalcontentlink(CourseId, CulturalContentId, CourseFieldName) values(?,?,?)";

    public static final String INSERT_COURSE_CULTURAL_CONTENT_LINK_FROM_CONTENT_NAME = "insert into courseculturalcontentlink(CourseId, CulturalContentId, CourseFieldName) select ?, cc.Id, '' from culturalContent cc where cc.FileName = ?";

    public static final String INSERT_COURSE_CONTENT_CULTURAL_CONTENT_LINK = "insert into coursecontentculturalcontentlink(CourseId, CourseContentName, CulturalContentId) values(?,?,?)";

    public static final String INSERT_COURSE_CONTENT_CULTURAL_CONTENT_LINK_FROM_CONTENT_NAME = "insert into coursecontentculturalcontentlink(CourseId, CourseContentName, CulturalContentId) select ?,?, cc.Id from culturalContent cc where cc.FileName = ?;";
    
    public static final String INSERT_COURSE_OBJECTIVE = "insert into courseobjective(CourseId, CourseObjectiveDescription) values ";
   	
    public static final String INSERT_COURSE_OBJECTIVE_CULTURAL_CONTENT_LINK = "insert into courseobjectiveculturalcontentlink(CourseObjectiveId, CulturalContentId) values(?,?)";

    public static final String GET_ROLES = "select Id, Name, Description from role";

    public static final String GET_USER_DETAILS = "select u.Id, u.UserName, u.FirstName, u.LastName, url.RoleName, u.RoleId from users u join userrolelink url on url.UserName=? where u.UserName=?;";

    public static final String GET_UNANNOTATED_IMAGES_SQL = "select Id, FileName from culturalcontent where IsAnnotated=? and ContentType=\'Image\'";
	
	public static final String GET_IMAGE_SQL = "select FileName, FileType, Content from culturalcontent where Id=?";	
	
	public static final String GET_UNANNOTATED_VIDEOS_SQL = "select Id, FileName from culturalcontent where IsAnnotated=? and ContentType=\'Video\'";
	
	public static final String GET_VIDEO_SQL = "select FileName, FileType, Content from culturalcontent where Id=?";	
	
	public static final String GET_UNANNOTATED_AUDIOS_SQL = "select AUDIO_ID, FILE_NAME from AUDIOS_TABLE where IsAnnotated=? and ContentType=\'Audio\'";
	
	public static final String GET_AUDIO_SQL = "select FileName, FileType, Content from culturalcontent where Id=?";
		
	public static final String GET_UNANNOTATED_TEXTS_SQL = "select Id, FileName from culturalcontent where IsAnnotated=? and ContentType=\'Text\'";
	
	public static final String GET_TEXT_SQL = "select FileName, FileType, Content from culturalcontent where Id=?";

    public static final String GET_LEARNING_OBJECT_BY_ID = "select FileName, FileType, Content, ContentType, ReferenceId from culturalcontent where Id=?";
    
    public static final String GET_LEARNING_OBJECT_BY_FULL_TEXT_SEARCH = "select Id from culturalcontent where match ContentText against (? in boolean mode)";
	
	public static final String GET_COURSES_FOR_USER_SQL =
            "select Id, OwnerId, Name, Description, Prerequisites, Topics, Overview, Timeline, IndividualAssignments, GroupAssignemnts, OnsiteAssignments, " +
            "Exams, Quizzes, KnowledgeCheckpoints, GradingRubric, TechnologyRequirements, SupportServices, EvaluationFormat,ProgressMeterBadges " +
            "from course where OwnerId=?";

    public static final String GET_COURSES_BY_ID_SQL =
            "select Id, OwnerId, Name, Description, Prerequisites, Topics, Overview, Timeline, IndividualAssignments, GroupAssignemnts, OnsiteAssignments, " +
                    "Exams, Quizzes, KnowledgeCheckpoints, GradingRubric, TechnologyRequirements, SupportServices, EvaluationFormat,ProgressMeterBadges " +
                    "from course where Id=?";

    public static final String GET_ALL_COURSES_SQL = "select Id, OwnerId, Name, Description, Prerequisites from course";

    public static final String GET_ALL_OBJECTIVE_DESCRIPTION = "select Id, CourseObjectiveDescription from courseobjective where CourseId=?";
    
    public static final String GET_LEARNING_OBJECT_LINKS_FOR_COURSE = "select Id, CulturalContentId, CourseFieldName from courseculturalcontentlink where CourseId=?";

    public static final String GET_LINKED_CULTURAL_OBJECTS_FOR_COURSE = "select link.Id, link.CourseFieldName, cc.Id, cc.FileName, cc.FileType, cc.ContentType, cc.Content from culturalcontent cc join courseculturalcontentlink link on link.CulturalContentId = cc.Id where link.CourseId=? order by cc.ContentType";

    public static final String GET_ALL_CULTURAL_LEARNING_OBJECTS = "select Id, FileName, FileType, Content, ContentType, ReferenceId from culturalcontent";
    
    public static final String GET_COURSE_DATA_FOR_EXPORT = "SELECT cc.Content, cc.FileName, cc.FileType from course c left join courseculturalcontentlink link on link.CourseId = c.Id left join culturalcontent cc on cc.Id = link.CulturalContentId where c.Id=? ; ";

    public static final String UPDATE_COURSE_SQL = "update course set Name=?, Description=?, Prerequisites=?, Topics=?, Overview=?, Timeline=?, IndividualAssignments=?, GroupAssignemnts=?, OnsiteAssignments=?, " +
            "Exams=?, Quizzes=?, KnowledgeCheckpoints=?, GradingRubric=?, TechnologyRequirements=?, SupportServices=?, EvaluationFormat=?,ProgressMeterBadges=? where Id=?";

    public static final String UPDATE_IMAGE_SQL = "update culturalcontent set IsAnnotated=? where Id=?";
	
	public static final String UPDATE_AUDIO_SQL = "update culturalcontent set IsAnnotated=? where Id=?";
	
	public static final String UPDATE_VIDEO_SQL = "update culturalcontent set IsAnnotated=? where Id=?";
	
	public static final String UPDATE_TEXT_SQL = "update culturalcontent set IsAnnotated=? where Id=?";

    public static final String DELETE_COURSE_CULTURAL_CONTENT_LINK = "delete from courseculturalcontentlink where CourseId=? and CulturalContentId=?";

    public static final String DELETE_COURSE_CULTURAL_CONTENT_LINK_BY_LINK_ID = "delete from courseculturalcontentlink where Id=? ;";

    public static final String DELETE_COURSE_OBJECTIVE_CULTURAL_CONTENT_LINK = "delete from courseobjectiveculturalcontentlink where CourseObjectiveId=? and CulturalContentId=?";

    public static final String DELETE_COURSE_OBJECTIVE_CULTURAL_CONTENT_LINK_BY_LINK_ID = "delete from courseobjectiveculturalcontentlink where Id=? ;";

    public static final String LOG_BEGIN = " Begin ";
	
	public static final String LOG_END = " End ";
    
    public enum ContentTypes {
        Audio,
        Image,
        Text,
        Video,
    }
	
    // TODO Fix this to work on any machine
	public static final String FILES_DIR =
			"C:" + File.separator + "TEN" + File.separator
			+ "workspace" + File.separator + "TribalEducationNetwork" + File.separator
			+ "WebContent" + File.separator + "videos" + File.separator;
	
	public static final String FRAMES_DIR =
			"C:" + File.separator + "TEN" + File.separator
			+ "workspace" + File.separator + "TribalEducationNetwork" + File.separator
			+ "WebContent" + File.separator + "frames" + File.separator;
	
	public static final String THUMBS_DIR =
			"C:" + File.separator + "TEN" + File.separator
			+ "apache-tomcat-7.0.12" + File.separator + "wtpwebapps"
			+ File.separator + "TribalEducationNetwork" + File.separator
			+ "frames" + File.separator;
	
}