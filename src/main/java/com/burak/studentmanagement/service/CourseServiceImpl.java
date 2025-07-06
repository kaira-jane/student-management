package com.burak.studentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.burak.studentmanagement.dao.CourseDao;
import com.burak.studentmanagement.entity.Course;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseDao courseDao;

	@Override
	@Transactional
	public void save(Course course) {
		courseDao.saveCourse(course);
	}

	@Override
	@Transactional
	public List<Course> findAllCourses() {
		return courseDao.findAllCourses();
	}

	@Override
	@Transactional
	public Course findCourseById(int id) {
		return courseDao.findCourseById(id);
	}

	@Override
	@Transactional
	public Course findCourseByIdWithStudents(int id) {
		Course course = courseDao.findCourseById(id);
		if (course != null && course.getStudents() != null) {
			// Force loading of students
			course.getStudents().size();
		}
		return course;
	}

	@Override
	@Transactional
	public Course findCourseByIdWithSchedules(int id) {
		Course course = courseDao.findCourseById(id);
		// Force load schedules
		if (course != null && course.getSchedules() != null) {
			course.getSchedules().size();
		}
		return course;
	}

	@Override
	@Transactional
	public void deleteCourseById(int id) {
		courseDao.deleteCourseById(id);
	}

}
