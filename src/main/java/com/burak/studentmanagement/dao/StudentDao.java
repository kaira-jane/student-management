package com.burak.studentmanagement.dao;

import java.util.List;

import com.burak.studentmanagement.entity.Student;

public interface StudentDao {

	public Student findByStudentName(String theStudentName);

	public void save(Student student);

	public Student findByStudentId(int id);

	public List<Student> findAllStudents();

	public List<Student> findBySectionId(int sectionId);

	public List<Student> findByCurriculumId(int curriculumId);

	public void deleteById(int id);
}
