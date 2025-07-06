package com.burak.studentmanagement.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.burak.studentmanagement.entity.Course;
import com.burak.studentmanagement.entity.Curriculum;
import com.burak.studentmanagement.entity.CurriculumCourse;
import com.burak.studentmanagement.service.CourseService;
import com.burak.studentmanagement.service.CurriculumCourseService;
import com.burak.studentmanagement.service.CurriculumService;

@Controller
@RequestMapping("/admin/curriculum")
public class CurriculumController {

    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private CurriculumCourseService curriculumCourseService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/list")
    public String listCurriculums(Model theModel) {
        List<Curriculum> curriculums = curriculumService.findAll();
        theModel.addAttribute("curriculums", curriculums);
        return "admin/curriculum-list";
    }

    @GetMapping("/showForm")
    public String showCurriculumForm(Model theModel) {
        theModel.addAttribute("curriculum", new Curriculum());
        return "admin/curriculum-form";
    }

    @PostMapping("/save")
    public String saveCurriculum(@Valid @ModelAttribute("curriculum") Curriculum curriculum,
            BindingResult theBindingResult, Model theModel) {

        if (theBindingResult.hasErrors()) {
            return "admin/curriculum-form";
        }

        // Check if curriculum code already exists (for new curriculums)
        if (curriculum.getId() == 0) {
            Curriculum existingCurriculum = curriculumService.findByCode(curriculum.getCode());
            if (existingCurriculum != null) {
                theModel.addAttribute("curriculum", curriculum);
                theModel.addAttribute("duplicateCodeError",
                        "Curriculum code '" + curriculum.getCode() + "' already exists. Please use a different code.");
                return "admin/curriculum-form";
            }
        } else {
            // For updates, check if the code exists on a different curriculum
            Curriculum existingCurriculum = curriculumService.findByCode(curriculum.getCode());
            if (existingCurriculum != null && existingCurriculum.getId() != curriculum.getId()) {
                theModel.addAttribute("curriculum", curriculum);
                theModel.addAttribute("duplicateCodeError",
                        "Curriculum code '" + curriculum.getCode() + "' already exists. Please use a different code.");
                return "admin/curriculum-form";
            }
        }

        curriculumService.save(curriculum);
        return "redirect:/admin/curriculum/list";
    }

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam("curriculumId") int curriculumId, Model theModel) {
        Curriculum curriculum = curriculumService.findById(curriculumId);
        theModel.addAttribute("curriculum", curriculum);
        return "admin/curriculum-form";
    }

    @GetMapping("/delete")
    public String deleteCurriculum(@RequestParam("curriculumId") int curriculumId) {
        curriculumService.deleteById(curriculumId);
        return "redirect:/admin/curriculum/list";
    }

    @GetMapping("/{curriculumId}/courses")
    public String showCurriculumCourses(@PathVariable("curriculumId") int curriculumId, Model theModel) {
        Curriculum curriculum = curriculumService.findById(curriculumId);
        List<CurriculumCourse> curriculumCourses = curriculumCourseService.findByCurriculumId(curriculumId);

        theModel.addAttribute("curriculum", curriculum);
        theModel.addAttribute("curriculumCourses", curriculumCourses);
        return "admin/curriculum-courses";
    }

    @GetMapping("/{curriculumId}/addCourse")
    public String showAddCourseForm(@PathVariable("curriculumId") int curriculumId, Model theModel) {
        Curriculum curriculum = curriculumService.findById(curriculumId);
        List<Course> availableCourses = courseService.findAllCourses();

        // Remove courses that are already in the curriculum
        List<CurriculumCourse> existingCourses = curriculumCourseService.findByCurriculumId(curriculumId);
        for (CurriculumCourse cc : existingCourses) {
            availableCourses.remove(cc.getCourse());
        }

        theModel.addAttribute("curriculum", curriculum);
        theModel.addAttribute("availableCourses", availableCourses);
        theModel.addAttribute("curriculumCourse", new CurriculumCourse());
        return "admin/add-curriculum-course";
    }

    @PostMapping("/{curriculumId}/saveCourse")
    public String saveCurriculumCourse(@PathVariable("curriculumId") int curriculumId,
            @ModelAttribute("curriculumCourse") CurriculumCourse curriculumCourse,
            @RequestParam("courseId") int courseId,
            @RequestParam("semester") int semester,
            @RequestParam("yearLevel") int yearLevel,
            @RequestParam(value = "isRequired", defaultValue = "false") boolean isRequired) {

        Curriculum curriculum = curriculumService.findById(curriculumId);
        Course course = courseService.findCourseById(courseId);

        curriculumCourse.setCurriculum(curriculum);
        curriculumCourse.setCourse(course);
        curriculumCourse.setSemester(semester);
        curriculumCourse.setYearLevel(yearLevel);
        curriculumCourse.setRequired(isRequired);

        curriculumCourseService.save(curriculumCourse);
        return "redirect:/admin/curriculum/" + curriculumId + "/courses";
    }

    @GetMapping("/{curriculumId}/courses/delete")
    public String deleteCurriculumCourse(@PathVariable("curriculumId") int curriculumId,
            @RequestParam("curriculumCourseId") int curriculumCourseId) {
        curriculumCourseService.deleteById(curriculumCourseId);
        return "redirect:/admin/curriculum/" + curriculumId + "/courses";
    }
}