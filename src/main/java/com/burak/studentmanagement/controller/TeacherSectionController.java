package com.burak.studentmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.burak.studentmanagement.entity.Course;
import com.burak.studentmanagement.entity.Section;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.entity.Teacher;
import com.burak.studentmanagement.service.CourseService;
import com.burak.studentmanagement.service.SectionService;
import com.burak.studentmanagement.service.StudentService;
import com.burak.studentmanagement.service.TeacherService;

@Controller
@RequestMapping("/teacher/section")
public class TeacherSectionController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/{teacherId}")
    public String showTeacherSections(@PathVariable("teacherId") int teacherId, Model theModel) {
        Teacher teacher = teacherService.findByTeacherId(teacherId);
        List<Course> courses = teacher.getCourses();

        theModel.addAttribute("teacher", teacher);
        theModel.addAttribute("courses", courses);
        return "teacher/teacher-sections";
    }

    @GetMapping("/{teacherId}/course/{courseId}")
    public String showCourseSections(@PathVariable("teacherId") int teacherId,
            @PathVariable("courseId") int courseId,
            Model theModel) {
        Teacher teacher = teacherService.findByTeacherId(teacherId);
        Course course = courseService.findCourseById(courseId);
        List<Student> students = course.getStudents();

        theModel.addAttribute("teacher", teacher);
        theModel.addAttribute("course", course);
        theModel.addAttribute("students", students);
        return "teacher/course-sections";
    }

    @GetMapping("/{teacherId}/section/{sectionId}")
    public String showSectionDetails(@PathVariable("teacherId") int teacherId,
            @PathVariable("sectionId") int sectionId,
            Model theModel) {
        Teacher teacher = teacherService.findByTeacherId(teacherId);
        Section section = sectionService.findById(sectionId);
        List<Student> students = studentService.findBySectionId(sectionId);

        theModel.addAttribute("teacher", teacher);
        theModel.addAttribute("section", section);
        theModel.addAttribute("students", students);
        return "teacher/section-details";
    }
}