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

import com.burak.studentmanagement.entity.Curriculum;
import com.burak.studentmanagement.entity.Section;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.service.CurriculumService;
import com.burak.studentmanagement.service.SectionService;
import com.burak.studentmanagement.service.StudentService;

@Controller
@RequestMapping("/admin/section")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public String showSectionList(Model theModel) {
        List<Section> sections = sectionService.findAll();
        theModel.addAttribute("sections", sections);
        return "admin/section-list";
    }

    @GetMapping("/add")
    public String showAddSectionForm(Model theModel) {
        List<Curriculum> curriculums = curriculumService.findAll();
        theModel.addAttribute("curriculums", curriculums);
        theModel.addAttribute("section", new Section());
        return "admin/add-section";
    }

    @PostMapping("/save")
    public String saveSection(@RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("curriculumId") int curriculumId,
            @RequestParam("yearLevel") int yearLevel,
            @RequestParam("semester") int semester,
            @RequestParam("schoolYear") String schoolYear,
            @RequestParam("capacity") int capacity,
            Model theModel) {

        Curriculum curriculum = curriculumService.findById(curriculumId);

        Section section = new Section();
        section.setName(name);
        section.setDescription(description);
        section.setCurriculum(curriculum);
        section.setYearLevel(yearLevel);
        section.setSemester(semester);
        section.setSchoolYear(schoolYear);
        section.setCapacity(capacity);
        section.setActive(true);

        sectionService.save(section);

        theModel.addAttribute("success", "Section created successfully!");
        return "redirect:/admin/section/list";
    }

    @GetMapping("/{sectionId}")
    public String showSectionDetails(@PathVariable("sectionId") int sectionId, Model theModel) {
        Section section = sectionService.findById(sectionId);
        List<Student> students = studentService.findBySectionId(sectionId);

        theModel.addAttribute("section", section);
        theModel.addAttribute("students", students);
        return "admin/section-details";
    }

    @GetMapping("/{sectionId}/edit")
    public String showEditSectionForm(@PathVariable("sectionId") int sectionId, Model theModel) {
        Section section = sectionService.findById(sectionId);
        List<Curriculum> curriculums = curriculumService.findAll();

        theModel.addAttribute("section", section);
        theModel.addAttribute("curriculums", curriculums);
        return "admin/edit-section";
    }

    @PostMapping("/{sectionId}/update")
    public String updateSection(@PathVariable("sectionId") int sectionId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("curriculumId") int curriculumId,
            @RequestParam("yearLevel") int yearLevel,
            @RequestParam("semester") int semester,
            @RequestParam("schoolYear") String schoolYear,
            @RequestParam("capacity") int capacity,
            Model theModel) {

        Section section = sectionService.findById(sectionId);
        Curriculum curriculum = curriculumService.findById(curriculumId);

        section.setName(name);
        section.setDescription(description);
        section.setCurriculum(curriculum);
        section.setYearLevel(yearLevel);
        section.setSemester(semester);
        section.setSchoolYear(schoolYear);
        section.setCapacity(capacity);

        sectionService.save(section);

        theModel.addAttribute("success", "Section updated successfully!");
        return "redirect:/admin/section/" + sectionId;
    }

    @PostMapping("/{sectionId}/delete")
    public String deleteSection(@PathVariable("sectionId") int sectionId, Model theModel) {
        sectionService.deleteById(sectionId);
        theModel.addAttribute("success", "Section deleted successfully!");
        return "redirect:/admin/section/list";
    }

    @PostMapping("/{sectionId}/toggle")
    public String toggleSectionStatus(@PathVariable("sectionId") int sectionId, Model theModel) {
        Section section = sectionService.findById(sectionId);
        if (section != null) {
            section.setActive(!section.isActive());
            sectionService.save(section);
            theModel.addAttribute("success", "Section status updated successfully!");
        }
        return "redirect:/admin/section/" + sectionId;
    }
}