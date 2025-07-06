package com.burak.studentmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.burak.studentmanagement.entity.Course;
import com.burak.studentmanagement.entity.Schedule;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.service.CourseService;
import com.burak.studentmanagement.service.ScheduleService;
import com.burak.studentmanagement.service.StudentService;

@Controller
@RequestMapping("/student/schedule")
public class StudentScheduleController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/{studentId}")
    public String showStudentSchedule(@PathVariable("studentId") int studentId, Model theModel) {
        Student student = studentService.findByStudentIdWithCourses(studentId);
        List<Course> courses = student.getCourses();

        theModel.addAttribute("student", student);
        theModel.addAttribute("courses", courses);
        return "student/student-schedule";
    }

    @GetMapping("/{studentId}/course/{courseId}")
    public String showCourseSchedule(@PathVariable("studentId") int studentId,
            @PathVariable("courseId") int courseId,
            Model theModel) {
        Student student = studentService.findByStudentIdWithCourses(studentId);
        Course course = courseService.findCourseByIdWithSchedules(courseId);
        List<Schedule> schedules = scheduleService.findByCourseIdAndActive(courseId, true);

        theModel.addAttribute("student", student);
        theModel.addAttribute("course", course);
        theModel.addAttribute("schedules", schedules);
        return "student/course-schedule";
    }
}