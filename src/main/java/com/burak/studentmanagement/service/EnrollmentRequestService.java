package com.burak.studentmanagement.service;

import java.util.List;

import com.burak.studentmanagement.entity.EnrollmentRequest;
import com.burak.studentmanagement.entity.EnrollmentRequest.EnrollmentStatus;

public interface EnrollmentRequestService {

    public void save(EnrollmentRequest enrollmentRequest);

    public EnrollmentRequest findById(int id);

    public List<EnrollmentRequest> findAll();

    public List<EnrollmentRequest> findByStatus(EnrollmentStatus status);

    public List<EnrollmentRequest> findByStudentId(int studentId);

    public List<EnrollmentRequest> findByStudentIdAndStatus(int studentId, EnrollmentStatus status);

    public EnrollmentRequest findByStudentAndCourse(int studentId, int courseId);

    public void deleteById(int id);

    public void update(EnrollmentRequest enrollmentRequest);

    public void approveRequest(int requestId, int teacherId, String notes);

    public void rejectRequest(int requestId, int teacherId, String notes);
}