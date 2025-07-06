package com.burak.studentmanagement.service;

import java.util.List;

import com.burak.studentmanagement.entity.Curriculum;

public interface CurriculumService {

    public void save(Curriculum curriculum);

    public Curriculum findById(int id);

    public Curriculum findByIdWithCourses(int id);

    public Curriculum findByCode(String code);

    public List<Curriculum> findAll();

    public List<Curriculum> findActiveCurriculums();

    public void deleteById(int id);

    public void update(Curriculum curriculum);
}