package com.burak.studentmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.burak.studentmanagement.entity.CurriculumCourse;
import com.burak.studentmanagement.entity.EnrollmentRequest;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.service.CurriculumCourseService;
import com.burak.studentmanagement.service.EnrollmentRequestService;
import com.burak.studentmanagement.service.StudentService;

@Controller
@RequestMapping("/student/enrollment")
public class StudentEnrollmentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CurriculumCourseService curriculumCourseService;

    @Autowired
    private EnrollmentRequestService enrollmentRequestService;

    @GetMapping("/{studentId}/available-courses")
    public String showAvailableCourses(@PathVariable("studentId") int studentId, Model theModel) {
        Student student = studentService.findByStudentIdWithCurriculum(studentId);

        if (student.getCurriculum() == null) {
            theModel.addAttribute("error", "No curriculum assigned. Please contact your administrator.");
            theModel.addAttribute("student", student);
            return "student/enrollment-error";
        }

        // Get all courses in the student's curriculum
        List<CurriculumCourse> curriculumCourses = curriculumCourseService
                .findByCurriculumId(student.getCurriculum().getId());

        // Filter out courses the student is already enrolled in or has pending requests
        // for
        List<CurriculumCourse> availableCourses = curriculumCourseService.getAvailableCoursesForStudent(studentId,
                curriculumCourses);

        // Get student's enrollment requests
        List<EnrollmentRequest> enrollmentRequests = enrollmentRequestService.findByStudentId(studentId);

        theModel.addAttribute("student", student);
        theModel.addAttribute("availableCourses", availableCourses);
        theModel.addAttribute("enrollmentRequests", enrollmentRequests);

        return "student/available-courses";
    }

    @PostMapping("/{studentId}/enroll")
    public String enrollInCourse(@PathVariable("studentId") int studentId,
            @RequestParam("curriculumCourseId") int curriculumCourseId,
            Model theModel) {

        Student student = studentService.findByStudentIdWithCurriculum(studentId);
        CurriculumCourse curriculumCourse = curriculumCourseService.findById(curriculumCourseId);

        if (curriculumCourse == null) {
            theModel.addAttribute("error", "Course not found.");
            theModel.addAttribute("student", student);
            return "student/enrollment-error";
        }

        // Check if student already has a pending request for this course
        EnrollmentRequest existingRequest = enrollmentRequestService.findByStudentAndCourse(studentId,
                curriculumCourse.getCourse().getId());
        if (existingRequest != null) {
            theModel.addAttribute("error", "You already have a pending enrollment request for this course.");
            theModel.addAttribute("student", student);
            return "student/enrollment-error";
        }

        // Create new enrollment request
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest(student, curriculumCourse.getCourse(),
                curriculumCourse);
        enrollmentRequestService.save(enrollmentRequest);

        theModel.addAttribute("success", "Enrollment request submitted successfully! Please wait for admin approval.");
        theModel.addAttribute("student", student);

        return "redirect:/student/enrollment/" + studentId + "/available-courses";
    }

    @GetMapping("/{studentId}/requests")
    public String showEnrollmentRequests(@PathVariable("studentId") int studentId, Model theModel) {
        Student student = studentService.findByStudentIdWithCurriculum(studentId);
        List<EnrollmentRequest> enrollmentRequests = enrollmentRequestService.findByStudentId(studentId);

        theModel.addAttribute("student", student);
        theModel.addAttribute("enrollmentRequests", enrollmentRequests);

        return "student/enrollment-requests";
    }

    @PostMapping("/{studentId}/cancel-request")
    public String cancelEnrollmentRequest(@PathVariable("studentId") int studentId,
            @RequestParam("requestId") int requestId,
            Model theModel) {

        EnrollmentRequest request = enrollmentRequestService.findById(requestId);

        if (request != null && request.getStudent().getId() == studentId &&
                request.getStatus() == EnrollmentRequest.EnrollmentStatus.PENDING) {
            enrollmentRequestService.deleteById(requestId);
            theModel.addAttribute("success", "Enrollment request cancelled successfully.");
        } else {
            theModel.addAttribute("error", "Cannot cancel this request.");
        }

        return "redirect:/student/enrollment/" + studentId + "/requests";
    }
}