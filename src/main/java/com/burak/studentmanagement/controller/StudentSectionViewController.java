package com.burak.studentmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.burak.studentmanagement.entity.Section;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.service.SectionService;
import com.burak.studentmanagement.service.StudentService;

@Controller
@RequestMapping("/student/section")
public class StudentSectionViewController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SectionService sectionService;

    @GetMapping("/{studentId}")
    public String showStudentSection(@PathVariable("studentId") int studentId, Model theModel) {
        Student student = studentService.findByStudentIdWithSection(studentId);

        theModel.addAttribute("student", student);
        return "student/student-section";
    }

    @GetMapping("/{studentId}/classmates")
    public String showClassmates(@PathVariable("studentId") int studentId, Model theModel) {
        Student student = studentService.findByStudentIdWithSection(studentId);
        List<Student> classmates = studentService.findBySectionId(student.getSection().getId());

        theModel.addAttribute("student", student);
        theModel.addAttribute("classmates", classmates);
        return "student/classmates";
    }
}