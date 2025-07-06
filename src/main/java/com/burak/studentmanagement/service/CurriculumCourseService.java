package com.burak.studentmanagement.service;

import java.util.List;

import com.burak.studentmanagement.entity.CurriculumCourse;

public interface CurriculumCourseService {

    public void save(CurriculumCourse curriculumCourse);

    public CurriculumCourse findById(int id);

    public List<CurriculumCourse> findByCurriculumId(int curriculumId);

    public List<CurriculumCourse> findByCurriculumIdAndYearLevel(int curriculumId, int yearLevel);

    public List<CurriculumCourse> findByCurriculumIdAndSemester(int curriculumId, int semester);

    public List<CurriculumCourse> findByCurriculumIdAndYearLevelAndSemester(int curriculumId, int yearLevel,
            int semester);

    public List<CurriculumCourse> findAll();

    public void deleteById(int id);

    public void deleteByCurriculumId(int curriculumId);

    public List<CurriculumCourse> getAvailableCoursesForStudent(int studentId,
            List<CurriculumCourse> curriculumCourses);
}