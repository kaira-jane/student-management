package com.burak.studentmanagement.dao;

import java.util.List;

import com.burak.studentmanagement.entity.Section;

public interface SectionDao {

    public void save(Section section);

    public Section findById(int id);

    public Section findByIdWithStudents(int id);

    public List<Section> findAll();

    public List<Section> findAllWithStudents();

    public List<Section> findByCurriculumId(int curriculumId);

    public List<Section> findByCurriculumIdAndActive(int curriculumId, boolean isActive);

    public List<Section> findByYearLevelAndSemester(int yearLevel, int semester);

    public List<Section> findBySchoolYear(String schoolYear);

    public List<Section> findByActive(boolean isActive);

    public List<Section> findByActiveWithStudents(boolean isActive);

    public void deleteById(int id);

    public Section findByName(String name);

    public int getCurrentEnrollmentCount(int sectionId);
}