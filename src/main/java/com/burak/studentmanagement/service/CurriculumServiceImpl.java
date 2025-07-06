package com.burak.studentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.burak.studentmanagement.dao.CurriculumDao;
import com.burak.studentmanagement.entity.Curriculum;

@Service
public class CurriculumServiceImpl implements CurriculumService {

    @Autowired
    private CurriculumDao curriculumDao;

    @Override
    @Transactional
    public void save(Curriculum curriculum) {
        curriculumDao.save(curriculum);
    }

    @Override
    @Transactional
    public Curriculum findById(int id) {
        return curriculumDao.findById(id);
    }

    @Override
    @Transactional
    public Curriculum findByIdWithCourses(int id) {
        Curriculum curriculum = curriculumDao.findById(id);
        if (curriculum != null && curriculum.getCurriculumCourses() != null) {
            // Force loading of curriculum courses
            curriculum.getCurriculumCourses().size();
        }
        return curriculum;
    }

    @Override
    @Transactional
    public Curriculum findByCode(String code) {
        return curriculumDao.findByCode(code);
    }

    @Override
    @Transactional
    public List<Curriculum> findAll() {
        return curriculumDao.findAll();
    }

    @Override
    @Transactional
    public List<Curriculum> findActiveCurriculums() {
        return curriculumDao.findActiveCurriculums();
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        curriculumDao.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Curriculum curriculum) {
        curriculumDao.update(curriculum);
    }
}