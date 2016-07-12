package com.ten.beans;

/**
 * Represents a link between a course and learning object.
 *
 * @author Salma Bashar
 */
public class CourseLearningObjectLinkBean {

    int id;
    CourseBean course;
    LearningObjectDetailsBean learningObject;
    String courseContentName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CourseBean getCourse() {
        return course;
    }

    public void setCourse(CourseBean course) {
        this.course = course;
    }

    public LearningObjectDetailsBean getLearningObject() {
        return learningObject;
    }

    public void setLearningObject(LearningObjectDetailsBean learningObject) {
        this.learningObject = learningObject;
    }

    public String getCourseContentName() {
        return courseContentName;
    }

    public void setCourseContentName(String courseContentName) {
        this.courseContentName = courseContentName;
    }
}
