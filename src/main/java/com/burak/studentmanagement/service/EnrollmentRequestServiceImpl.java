package com.burak.studentmanagement.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.burak.studentmanagement.dao.EnrollmentRequestDao;
import com.burak.studentmanagement.entity.EnrollmentRequest;
import com.burak.studentmanagement.entity.EnrollmentRequest.EnrollmentStatus;
import com.burak.studentmanagement.entity.StudentCourseDetails;
import com.burak.studentmanagement.entity.GradeDetails;
import com.burak.studentmanagement.entity.Assignment;
import com.burak.studentmanagement.entity.Teacher;

@Service
public class EnrollmentRequestServiceImpl implements EnrollmentRequestService {

    @Autowired
    private EnrollmentRequestDao enrollmentRequestDao;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentCourseDetailsService studentCourseDetailsService;

    @Autowired
    private GradeDetailsService gradeDetailsService;

    @Override
    @Transactional
    public void save(EnrollmentRequest enrollmentRequest) {
        enrollmentRequestDao.save(enrollmentRequest);
    }

    @Override
    @Transactional
    public EnrollmentRequest findById(int id) {
        return enrollmentRequestDao.findById(id);
    }

    @Override
    @Transactional
    public List<EnrollmentRequest> findAll() {
        return enrollmentRequestDao.findAll();
    }

    @Override
    @Transactional
    public List<EnrollmentRequest> findByStatus(EnrollmentStatus status) {
        return enrollmentRequestDao.findByStatus(status);
    }

    @Override
    @Transactional
    public List<EnrollmentRequest> findByStudentId(int studentId) {
        return enrollmentRequestDao.findByStudentId(studentId);
    }

    @Override
    @Transactional
    public List<EnrollmentRequest> findByStudentIdAndStatus(int studentId, EnrollmentStatus status) {
        return enrollmentRequestDao.findByStudentIdAndStatus(studentId, status);
    }

    @Override
    @Transactional
    public EnrollmentRequest findByStudentAndCourse(int studentId, int courseId) {
        return enrollmentRequestDao.findByStudentAndCourse(studentId, courseId);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        enrollmentRequestDao.deleteById(id);
    }

    @Override
    @Transactional
    public void update(EnrollmentRequest enrollmentRequest) {
        enrollmentRequestDao.update(enrollmentRequest);
    }

    @Override
    @Transactional
    public void approveRequest(int requestId, int teacherId, String notes) {
        EnrollmentRequest request = findById(requestId);
        if (request != null && request.getStatus() == EnrollmentStatus.PENDING) {
            Teacher teacher = teacherService.findByTeacherId(teacherId);

            request.setStatus(EnrollmentStatus.APPROVED);
            request.setProcessedBy(teacher);
            request.setProcessedDate(new Timestamp(System.currentTimeMillis()));
            request.setNotes(notes);

            update(request);

            // Create student course enrollment
            StudentCourseDetails studentCourseDetails = new StudentCourseDetails(
                    request.getStudent().getId(),
                    request.getCourse().getId(),
                    new java.util.ArrayList<Assignment>(),
                    new GradeDetails());
            studentCourseDetails.setEnrollmentRequestId(requestId);
            studentCourseDetailsService.save(studentCourseDetails);
        }
    }

    @Override
    @Transactional
    public void rejectRequest(int requestId, int teacherId, String notes) {
        EnrollmentRequest request = findById(requestId);
        if (request != null && request.getStatus() == EnrollmentStatus.PENDING) {
            Teacher teacher = teacherService.findByTeacherId(teacherId);

            request.setStatus(EnrollmentStatus.REJECTED);
            request.setProcessedBy(teacher);
            request.setProcessedDate(new Timestamp(System.currentTimeMillis()));
            request.setNotes(notes);

            update(request);
        }
    }
}