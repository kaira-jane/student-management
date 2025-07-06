package com.burak.studentmanagement.dao;

import java.util.List;

import com.burak.studentmanagement.entity.Curriculum;

public interface CurriculumDao {

    public void save(Curriculum curriculum);

    public Curriculum findById(int id);

    public Curriculum findByCode(String code);

    public List<Curriculum> findAll();

    public List<Curriculum> findActiveCurriculums();

    public void deleteById(int id);

    public void update(Curriculum curriculum);
}