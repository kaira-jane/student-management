package com.burak.studentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.burak.studentmanagement.dao.SectionDao;
import com.burak.studentmanagement.entity.Section;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionDao sectionDao;

    @Override
    @Transactional
    public void save(Section section) {
        sectionDao.save(section);
    }

    @Override
    @Transactional
    public Section findById(int id) {
        return sectionDao.findById(id);
    }

    @Override
    @Transactional
    public List<Section> findAll() {
        return sectionDao.findAll();
    }

    @Override
    @Transactional
    public List<Section> findByCurriculumId(int curriculumId) {
        return sectionDao.findByCurriculumId(curriculumId);
    }

    @Override
    @Transactional
    public List<Section> findByCurriculumIdAndActive(int curriculumId, boolean isActive) {
        return sectionDao.findByCurriculumIdAndActive(curriculumId, isActive);
    }

    @Override
    @Transactional
    public List<Section> findByYearLevelAndSemester(int yearLevel, int semester) {
        return sectionDao.findByYearLevelAndSemester(yearLevel, semester);
    }

    @Override
    @Transactional
    public List<Section> findBySchoolYear(String schoolYear) {
        return sectionDao.findBySchoolYear(schoolYear);
    }

    @Override
    @Transactional
    public List<Section> findByActive(boolean isActive) {
        return sectionDao.findByActive(isActive);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        sectionDao.deleteById(id);
    }

    @Override
    @Transactional
    public Section findByName(String name) {
        return sectionDao.findByName(name);
    }

    @Override
    @Transactional
    public Section findByIdWithStudents(int id) {
        return sectionDao.findByIdWithStudents(id);
    }
}