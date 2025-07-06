package com.burak.studentmanagement.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "enrollment_request")
public class EnrollmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curriculum_course_id")
    private CurriculumCourse curriculumCourse;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    @Column(name = "request_date")
    private Timestamp requestDate;

    @Column(name = "processed_date")
    private Timestamp processedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "processed_by")
    private Teacher processedBy;

    @Column(name = "notes")
    private String notes;

    public enum EnrollmentStatus {
        PENDING, APPROVED, REJECTED
    }

    public EnrollmentRequest() {
        this.requestDate = new Timestamp(System.currentTimeMillis());
    }

    public EnrollmentRequest(Student student, Course course, CurriculumCourse curriculumCourse) {
        this();
        this.student = student;
        this.course = course;
        this.curriculumCourse = curriculumCourse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CurriculumCourse getCurriculumCourse() {
        return curriculumCourse;
    }

    public void setCurriculumCourse(CurriculumCourse curriculumCourse) {
        this.curriculumCourse = curriculumCourse;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    public Timestamp getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Timestamp processedDate) {
        this.processedDate = processedDate;
    }

    public Teacher getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Teacher processedBy) {
        this.processedBy = processedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "EnrollmentRequest [id=" + id + ", student=" + student.getFirstName() + " " + student.getLastName() +
                ", course=" + course.getName() + ", status=" + status + ", requestDate=" + requestDate + "]";
    }
}