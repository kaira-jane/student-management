package com.burak.studentmanagement.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@NotBlank(message = "is required")
	@Size(min = 1, message = "is required")
	@Column(name = "code")
	private String code;

	@NotBlank(message = "is required")
	@Size(min = 1, message = "is required")
	@Column(name = "name")
	private String name;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;

	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "student_course_details", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
	private List<Student> students;

	@OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Schedule> schedules;

	public Course() {
		this.students = new ArrayList<>();
		this.schedules = new ArrayList<>();
	}

	public Course(int id, String code, String name, Teacher teacher, List<Student> students) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.teacher = teacher;
		this.students = students;
		this.schedules = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
		teacher.addCourse(this);
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

	public int studentListSize() {
		return students.size();
	}

	public void addStudent(Student student) {
		if (students == null) {
			students = new ArrayList<>();
		}
		students.add(student);
	}

	public void removeStudent(Student student) {
		if (students.contains(student)) {
			students.remove(student);
		}
	}

	public void addSchedule(Schedule schedule) {
		if (schedules == null) {
			schedules = new ArrayList<>();
		}
		schedules.add(schedule);
		schedule.setCourse(this);
	}

	public void removeSchedule(Schedule schedule) {
		if (schedules.contains(schedule)) {
			schedules.remove(schedule);
			schedule.setCourse(null);
		}
	}

	public boolean equals(Object comparedObject) {
		if (this == comparedObject) {
			return true;
		}

		if (!(comparedObject instanceof Course)) {
			return false;
		}

		Course comparedCourse = (Course) comparedObject;

		if (this.id == comparedCourse.id) {
			return true;
		}

		return false;
	}

}