package com.ten.beans;

/**
 * Represents a link between a course objective and learning object.
 *
 * @author Salma Bashar
 */

public class CourseObjectiveLearningObjectLinkBean {

    int id;
    CourseObjectiveBean courseObjective;
    LearningObjectDetailsBean learningObject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CourseObjectiveBean getCourse() {
        return courseObjective;
    }

    public void setCourse(CourseObjectiveBean course) {
        this.courseObjective = course;
    }

    public LearningObjectDetailsBean getLearningObject() {
        return learningObject;
    }

    public void setLearningObject(LearningObjectDetailsBean learningObject) {
        this.learningObject = learningObject;
    }
}
