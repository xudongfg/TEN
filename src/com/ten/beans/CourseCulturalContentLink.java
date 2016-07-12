package com.ten.beans;

/**
 * Cultural content that has been linked courses.
 */
public class CourseCulturalContentLink {

    int id;
    int courseId;
    int culturalContentId;
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
