package com.burak.studentmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.burak.studentmanagement.dao.CurriculumCourseDao;
import com.burak.studentmanagement.entity.CurriculumCourse;
import com.burak.studentmanagement.entity.EnrollmentRequest;
import com.burak.studentmanagement.entity.EnrollmentRequest.EnrollmentStatus;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.entity.StudentCourseDetails;

@Service
public class CurriculumCourseServiceImpl implements CurriculumCourseService {

    @Autowired
    private CurriculumCourseDao curriculumCourseDao;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentCourseDetailsService studentCourseDetailsService;

    @Autowired
    private EnrollmentRequestService enrollmentRequestService;

    @Override
    @Transactional
    public void save(CurriculumCourse curriculumCourse) {
        curriculumCourseDao.save(curriculumCourse);
    }

    @Override
    @Transactional
    public CurriculumCourse findById(int id) {
        return curriculumCourseDao.findById(id);
    }

    @Override
    @Transactional
    public List<CurriculumCourse> findByCurriculumId(int curriculumId) {
        return curriculumCourseDao.findByCurriculumId(curriculumId);
    }

    @Override
    @Transactional
    public List<CurriculumCourse> findByCurriculumIdAndYearLevel(int curriculumId, int yearLevel) {
        return curriculumCourseDao.findByCurriculumIdAndYearLevel(curriculumId, yearLevel);
    }

    @Override
    @Transactional
    public List<CurriculumCourse> findByCurriculumIdAndSemester(int curriculumId, int semester) {
        return curriculumCourseDao.findByCurriculumIdAndSemester(curriculumId, semester);
    }

    @Override
    @Transactional
    public List<CurriculumCourse> findByCurriculumIdAndYearLevelAndSemester(int curriculumId, int yearLevel,
            int semester) {
        return curriculumCourseDao.findByCurriculumIdAndYearLevelAndSemester(curriculumId, yearLevel, semester);
    }

    @Override
    @Transactional
    public List<CurriculumCourse> findAll() {
        return curriculumCourseDao.findAll();
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        curriculumCourseDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByCurriculumId(int curriculumId) {
        curriculumCourseDao.deleteByCurriculumId(curriculumId);
    }

    @Override
    @Transactional
    public List<CurriculumCourse> getAvailableCoursesForStudent(int studentId,
            List<CurriculumCourse> curriculumCourses) {
        Student student = studentService.findByStudentIdWithCourses(studentId);
        List<CurriculumCourse> availableCourses = new ArrayList<>();

        for (CurriculumCourse curriculumCourse : curriculumCourses) {
            // Check if student is already enrolled in this course
            boolean isEnrolled = false;
            if (student.getCourses() != null) {
                for (var course : student.getCourses()) {
                    if (course.getId() == curriculumCourse.getCourse().getId()) {
                        isEnrolled = true;
                        break;
                    }
                }
            }

            // Check if student has a pending request for this course
            EnrollmentRequest existingRequest = enrollmentRequestService.findByStudentAndCourse(studentId,
                    curriculumCourse.getCourse().getId());
            boolean hasPendingRequest = (existingRequest != null
                    && existingRequest.getStatus() == EnrollmentStatus.PENDING);

            // Add to available courses if not enrolled and no pending request
            if (!isEnrolled && !hasPendingRequest) {
                availableCourses.add(curriculumCourse);
            }
        }

        return availableCourses;
    }
}