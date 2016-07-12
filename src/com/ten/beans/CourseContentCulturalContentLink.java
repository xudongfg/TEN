package com.ten.beans;

/**
 * @author Salma Bashar
 * Cultural content that has been linked courses.
 */
public class CourseContentCulturalContentLink {

    int id;
    int courseId;
    int courseContentId;
	int culturalContentId;
	String courseContentName;
	String culturalContentFileName;
    String culturalContentFileType;
    String culturalContentContentType;
    byte[] content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    public int getCourseContentId() {
		return courseContentId;
	}

	public void setCourseContentId(int courseContentId) {
		this.courseContentId = courseContentId;
	}
	
	public String getCourseContentName() {
		return courseContentName;
	}

	public void setCourseContentName(String courseContentName) {
		this.courseContentName = courseContentName;
	}
	
    public int getCulturalContentId() {
        return culturalContentId;
    }

    public void setCulturalContentId(int culturalContentId) {
        this.culturalContentId = culturalContentId;
    }

    public String getCulturalContentFileName() {
        return culturalContentFileName;
    }

    public void setCulturalContentFileName(String culturalContentFileName) {
        this.culturalContentFileName = culturalContentFileName;
    }

    public String getCulturalContentFileType() {
        return culturalContentFileType;
    }

    public void setCulturalContentFileType(String culturalContentFileType) {
        this.culturalContentFileType = culturalContentFileType;
    }

    public String getCulturalContentContentType() {
        return culturalContentContentType;
    }

    public void setCulturalContentContentType(String culturalContentContentType) {
        this.culturalContentContentType = culturalContentContentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
