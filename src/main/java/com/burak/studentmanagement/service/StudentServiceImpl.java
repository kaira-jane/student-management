package com.burak.studentmanagement.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.burak.studentmanagement.dao.RoleDao;
import com.burak.studentmanagement.dao.StudentDao;
import com.burak.studentmanagement.entity.Role;
import com.burak.studentmanagement.entity.Student;
import com.burak.studentmanagement.entity.Curriculum;
import com.burak.studentmanagement.user.UserDto;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private CurriculumService curriculumService;

	@Override
	@Transactional
	public Student findByStudentName(String studentName) {
		return studentDao.findByStudentName(studentName);
	}

	@Override
	@Transactional
	public Student findByStudentId(int id) {
		return studentDao.findByStudentId(id);
	}

	@Override
	@Transactional
	public Student findByStudentIdWithCourses(int id) {
		Student student = studentDao.findByStudentId(id);
		if (student != null && student.getCourses() != null) {
			// Force loading of courses
			student.getCourses().size();
		}
		return student;
	}

	@Override
	@Transactional
	public Student findByStudentIdWithCurriculum(int id) {
		Student student = studentDao.findByStudentId(id);
		if (student != null && student.getCurriculum() != null) {
			// Force loading of curriculum
			student.getCurriculum().getName();
		}
		return student;
	}

	@Override
	@Transactional
	public Student findByStudentIdWithSection(int id) {
		Student student = studentDao.findByStudentId(id);
		if (student != null && student.getSection() != null) {
			// Force loading of section
			student.getSection().getName();
		}
		return student;
	}

	@Override
	@Transactional
	public List<Student> findBySectionId(int sectionId) {
		return studentDao.findBySectionId(sectionId);
	}

	@Override
	@Transactional
	public List<Student> findByCurriculumId(int curriculumId) {
		return studentDao.findByCurriculumId(curriculumId);
	}

	@Override
	@Transactional
	public void save(UserDto userDto) {
		Student student = new Student();
		student.setUserName(userDto.getUserName());
		student.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		student.setFirstName(userDto.getFirstName());
		student.setLastName(userDto.getLastName());
		student.setEmail(userDto.getEmail());
		student.setRole(userDto.getRole());

		// Set curriculum if provided
		if (userDto.getCurriculumId() != null) {
			Curriculum curriculum = curriculumService.findById(userDto.getCurriculumId());
			student.setCurriculum(curriculum);
		}

		studentDao.save(student);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Student student = studentDao.findByStudentName(username);
		if (student == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		Collection<Role> role = new ArrayList<>();
		role.add(student.getRole());
		return new org.springframework.security.core.userdetails.User(student.getUserName(), student.getPassword(),
				mapRolesToAuthorities(role));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<Student> findAllStudents() {
		return studentDao.findAllStudents();
	}

	@Override
	@Transactional
	public void save(Student student) {
		studentDao.save(student);

	}

	@Override
	@Transactional
	public void deleteById(int id) {
		studentDao.deleteById(id);
	}

}
