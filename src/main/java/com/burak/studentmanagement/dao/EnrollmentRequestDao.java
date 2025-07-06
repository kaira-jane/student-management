package com.burak.studentmanagement.dao;

import java.util.List;

import com.burak.studentmanagement.entity.EnrollmentRequest;
import com.burak.studentmanagement.entity.EnrollmentRequest.EnrollmentStatus;

public interface EnrollmentRequestDao {

    public void save(EnrollmentRequest enrollmentRequest);

    public EnrollmentRequest findById(int id);

    public List<EnrollmentRequest> findAll();

    public List<EnrollmentRequest> findByStatus(EnrollmentStatus status);

    public List<EnrollmentRequest> findByStudentId(int studentId);

    public List<EnrollmentRequest> findByStudentIdAndStatus(int studentId, EnrollmentStatus status);

    public EnrollmentRequest findByStudentAndCourse(int studentId, int courseId);

    public void deleteById(int id);

    public void update(EnrollmentRequest enrollmentRequest);
}