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
import org.springframework.transaction.annotation.Transactional;

import com.burak.studentmanagement.entity.Curriculum;
import com.burak.studentmanagement.entity.Section;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.service.CurriculumService;
import com.burak.studentmanagement.service.SectionService;
import com.burak.studentmanagement.service.StudentService;

@Controller
@RequestMapping("/admin/student-section")
public class StudentSectionController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private CurriculumService curriculumService;

    @GetMapping("/assign/{studentId}")
    public String showAssignSectionForm(@PathVariable("studentId") int studentId, Model theModel) {
        Student student = studentService.findByStudentId(studentId);
        List<Section> availableSections = sectionService.findByCurriculumIdAndActive(student.getCurriculum().getId(),
                true);

        theModel.addAttribute("student", student);
        theModel.addAttribute("availableSections", availableSections);
        return "admin/assign-student-section";
    }

    @PostMapping("/assign/{studentId}")
    @Transactional
    public String assignSection(@PathVariable("studentId") int studentId,
            @RequestParam("sectionId") int sectionId,
            Model theModel) {

        Student student = studentService.findByStudentId(studentId);
        Section section = sectionService.findByIdWithStudents(sectionId);

        if (section != null && !section.isFull()) {
            student.setSection(section);
            studentService.save(student);
            theModel.addAttribute("success", "Student assigned to section successfully!");
        } else {
            theModel.addAttribute("error", "Section is full or not found!");
        }

        return "redirect:/admin/students";
    }

    @PostMapping("/remove/{studentId}")
    @Transactional
    public String removeSection(@PathVariable("studentId") int studentId, Model theModel) {
        Student student = studentService.findByStudentId(studentId);
        student.setSection(null);
        studentService.save(student);

        theModel.addAttribute("success", "Student removed from section successfully!");
        return "redirect:/admin/students";
    }

    @GetMapping("/bulk-assign")
    public String showBulkAssignForm(Model theModel) {
        List<Section> sections = sectionService.findByActive(true);
        List<Student> students = studentService.findAllStudents();

        theModel.addAttribute("sections", sections);
        theModel.addAttribute("students", students);
        return "admin/bulk-assign-sections";
    }

    @PostMapping("/bulk-assign")
    @Transactional
    public String bulkAssignSections(@RequestParam("sectionId") int sectionId,
            @RequestParam("studentIds") List<Integer> studentIds,
            Model theModel) {

        if (studentIds == null || studentIds.isEmpty()) {
            theModel.addAttribute("error", "No students selected for assignment!");
            return "redirect:/admin/student-section/bulk-assign";
        }

        Section section = sectionService.findByIdWithStudents(sectionId);
        if (section == null) {
            theModel.addAttribute("error", "Section not found!");
            return "redirect:/admin/student-section/bulk-assign";
        }

        int assignedCount = 0;
        int skippedCount = 0;
        StringBuilder errorMessages = new StringBuilder();

        for (Integer studentId : studentIds) {
            try {
                Student student = studentService.findByStudentId(studentId);
                if (student == null) {
                    errorMessages.append("Student with ID ").append(studentId).append(" not found. ");
                    continue;
                }

                if (student.getCurriculum() == null) {
                    errorMessages.append("Student ").append(student.getFirstName()).append(" ").append(student.getLastName()).append(" has no curriculum assigned. ");
                    skippedCount++;
                    continue;
                }

                if (student.getCurriculum().getId() != section.getCurriculum().getId()) {
                    errorMessages.append("Student ").append(student.getFirstName()).append(" ").append(student.getLastName()).append(" has different curriculum. ");
                    skippedCount++;
                    continue;
                }

                if (student.getSection() != null) {
                    errorMessages.append("Student ").append(student.getFirstName()).append(" ").append(student.getLastName()).append(" is already assigned to a section. ");
                    skippedCount++;
                    continue;
                }

                if (section.isFull()) {
                    errorMessages.append("Section ").append(section.getName()).append(" is full. ");
                    break;
                }

                student.setSection(section);
                studentService.save(student);
                assignedCount++;

            } catch (Exception e) {
                errorMessages.append("Error processing student ID ").append(studentId).append(": ").append(e.getMessage()).append(" ");
            }
        }

        if (assignedCount > 0) {
            theModel.addAttribute("success", assignedCount + " students assigned to section successfully!" + 
                (skippedCount > 0 ? " " + skippedCount + " students skipped." : ""));
        }
        
        if (errorMessages.length() > 0) {
            theModel.addAttribute("error", errorMessages.toString());
        }

        return "redirect:/admin/section/" + sectionId;
    }
}
}