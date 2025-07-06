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
import com.burak.studentmanagement.entity.Teacher;
import com.burak.studentmanagement.service.CourseService;
import com.burak.studentmanagement.service.ScheduleService;
import com.burak.studentmanagement.service.TeacherService;

@Controller
@RequestMapping("/teacher/schedule")
public class TeacherScheduleController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/{teacherId}")
    public String showTeacherSchedule(@PathVariable("teacherId") int teacherId, Model theModel) {
        Teacher teacher = teacherService.findByTeacherId(teacherId);
        List<Course> courses = teacher.getCourses();

        theModel.addAttribute("teacher", teacher);
        theModel.addAttribute("courses", courses);
        return "teacher/teacher-schedule";
    }

    @GetMapping("/{teacherId}/course/{courseId}")
    public String showCourseSchedule(@PathVariable("teacherId") int teacherId,
            @PathVariable("courseId") int courseId,
            Model theModel) {
        Teacher teacher = teacherService.findByTeacherId(teacherId);
        Course course = courseService.findCourseByIdWithSchedules(courseId);
        List<Schedule> schedules = scheduleService.findByCourseIdAndActive(courseId, true);

        theModel.addAttribute("teacher", teacher);
        theModel.addAttribute("course", course);
        theModel.addAttribute("schedules", schedules);
        return "teacher/course-schedule";
    }
}