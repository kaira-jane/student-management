package com.burak.studentmanagement.controller;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.burak.studentmanagement.entity.Course;
import com.burak.studentmanagement.entity.Schedule;
import com.burak.studentmanagement.service.CourseService;
import com.burak.studentmanagement.service.ScheduleService;

@Controller
@RequestMapping("/admin/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/course/{courseId}")
    public String showCourseSchedules(@PathVariable("courseId") int courseId, Model theModel) {
        Course course = courseService.findCourseByIdWithSchedules(courseId);
        List<Schedule> schedules = scheduleService.findByCourseId(courseId);

        theModel.addAttribute("course", course);
        theModel.addAttribute("schedules", schedules);
        return "admin/course-schedules";
    }

    @GetMapping("/course/{courseId}/add")
    public String showAddScheduleForm(@PathVariable("courseId") int courseId, Model theModel) {
        Course course = courseService.findCourseById(courseId);
        theModel.addAttribute("course", course);
        theModel.addAttribute("schedule", new Schedule());
        return "admin/add-schedule";
    }

    @PostMapping("/course/{courseId}/save")
    public String saveSchedule(@PathVariable("courseId") int courseId,
            @RequestParam("dayOfWeek") String dayOfWeek,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("room") String room,
            @RequestParam("semester") String semester,
            @RequestParam("schoolYear") String schoolYear,
            Model theModel) {

        Course course = courseService.findCourseById(courseId);

        // Convert time strings to Time objects
        Time startTimeObj = null;
        Time endTimeObj = null;

        try {
            // Parse time strings (format: HH:mm) and convert to Time objects
            LocalTime startLocalTime = LocalTime.parse(startTime);
            LocalTime endLocalTime = LocalTime.parse(endTime);

            startTimeObj = Time.valueOf(startLocalTime);
            endTimeObj = Time.valueOf(endLocalTime);
        } catch (Exception e) {
            theModel.addAttribute("error", "Invalid time format. Please use HH:mm format (e.g., 09:00)");
            theModel.addAttribute("course", course);
            return "admin/add-schedule";
        }

        Schedule schedule = new Schedule();
        schedule.setCourse(course);
        schedule.setDayOfWeek(dayOfWeek);
        schedule.setStartTime(startTimeObj);
        schedule.setEndTime(endTimeObj);
        schedule.setRoom(room);
        schedule.setSemester(semester);
        schedule.setSchoolYear(schoolYear);
        schedule.setActive(true);

        scheduleService.save(schedule);

        theModel.addAttribute("success", "Schedule added successfully!");
        return "redirect:/admin/schedule/course/" + courseId;
    }

    @PostMapping("/{scheduleId}/delete")
    public String deleteSchedule(@PathVariable("scheduleId") int scheduleId,
            @RequestParam("courseId") int courseId,
            Model theModel) {

        scheduleService.deleteById(scheduleId);
        theModel.addAttribute("success", "Schedule deleted successfully!");
        return "redirect:/admin/schedule/course/" + courseId;
    }

    @PostMapping("/{scheduleId}/toggle")
    public String toggleScheduleStatus(@PathVariable("scheduleId") int scheduleId,
            @RequestParam("courseId") int courseId,
            Model theModel) {

        Schedule schedule = scheduleService.findById(scheduleId);
        if (schedule != null) {
            schedule.setActive(!schedule.isActive());
            scheduleService.save(schedule);
            theModel.addAttribute("success", "Schedule status updated successfully!");
        }

        return "redirect:/admin/schedule/course/" + courseId;
    }
}