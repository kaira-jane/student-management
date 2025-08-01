package com.burak.studentmanagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.burak.studentmanagement.dao.RoleDao;
import com.burak.studentmanagement.entity.Role;
import com.burak.studentmanagement.service.StudentService;
import com.burak.studentmanagement.service.TeacherService;
import com.burak.studentmanagement.service.CurriculumService;
import com.burak.studentmanagement.user.UserDto;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private CurriculumService curriculumService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/showRegistrationForm")
	public String showRegistrationForm(Model theModel) {
		theModel.addAttribute("userDto", new UserDto());
		// Add curriculums for student registration
		theModel.addAttribute("curriculums", curriculumService.findActiveCurriculums());
		return "registration/registration-form";
	}

	@PostMapping("/processRegistrationForm")
	public String processRegistrationForm(@Valid @ModelAttribute("userDto") UserDto user,
			BindingResult theBindingResult, @RequestParam(value = "role") String roleName,
			@RequestParam(value = "curriculumId", required = false) Integer curriculumId, Model theModel) {
		if (theBindingResult.hasErrors()) {
			theModel.addAttribute("curriculums", curriculumService.findActiveCurriculums());
			return "registration/registration-form";
		}

		if (roleName.equals("ROLE_STUDENT")) {
			String userName = user.getUserName();

			// if username already exists in db
			if (studentService.findByStudentName(userName) != null) {
				theModel.addAttribute("userDto", new UserDto());
				theModel.addAttribute("registrationError", "User name already exists!");
				theModel.addAttribute("curriculums", curriculumService.findActiveCurriculums());
				return "registration/registration-form";
			}

			// Validate curriculum selection for students
			if (curriculumId == null || curriculumId == 0) {
				theModel.addAttribute("userDto", new UserDto());
				theModel.addAttribute("registrationError", "Please select a curriculum for students!");
				theModel.addAttribute("curriculums", curriculumService.findActiveCurriculums());
				return "registration/registration-form";
			}

			Role role = roleDao.findRoleByName(roleName);
			user.setRole(role);
			user.setCurriculumId(curriculumId);
			studentService.save(user); // save() method converts UserDto to Student and saves it in db
		} else {

			String userName = user.getUserName();

			// if username already exists in db
			if (teacherService.findByTeacherName(userName) != null) {
				theModel.addAttribute("userDto", new UserDto());
				theModel.addAttribute("registrationError", "User name already exists!");
				theModel.addAttribute("curriculums", curriculumService.findActiveCurriculums());
				return "registration/registration-form";
			}

			Role role = roleDao.findRoleByName(roleName);
			user.setRole(role);
			teacherService.save(user);
		}

		return "registration/registration-confirmation";
	}
}
