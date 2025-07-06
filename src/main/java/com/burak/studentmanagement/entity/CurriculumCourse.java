package com.burak.studentmanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "curriculum_course")
public class CurriculumCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "semester")
    private int semester;

    @Column(name = "year_level")
    private int yearLevel;

    @Column(name = "is_required")
    private boolean isRequired = true;

    public CurriculumCourse() {
    }

    public CurriculumCourse(Curriculum curriculum, Course course, int semester, int yearLevel, boolean isRequired) {
        this.curriculum = curriculum;
        this.course = course;
        this.semester = semester;
        this.yearLevel = yearLevel;
        this.isRequired = isRequired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    @Override
    public boolean equals(Object comparedObject) {
        if (this == comparedObject) {
            return true;
        }

        if (!(comparedObject instanceof CurriculumCourse)) {
            return false;
        }

        CurriculumCourse comparedCurriculumCourse = (CurriculumCourse) comparedObject;

        if (this.id == comparedCurriculumCourse.id) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "CurriculumCourse [id=" + id + ", curriculum=" + curriculum.getName() +
                ", course=" + course.getName() + ", semester=" + semester +
                ", yearLevel=" + yearLevel + ", isRequired=" + isRequired + "]";
    }
}