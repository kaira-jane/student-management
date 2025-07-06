package com.burak.studentmanagement.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.burak.studentmanagement.entity.Curriculum;

@Repository
public class CurriculumDaoImpl implements CurriculumDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void save(Curriculum curriculum) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(curriculum);
    }

    @Override
    public Curriculum findById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query<Curriculum> query = session.createQuery("from Curriculum where id=:curriculumId", Curriculum.class);
        query.setParameter("curriculumId", id);

        try {
            return query.getSingleResult();
        } catch (Exception exc) {
            return null;
        }
    }

    @Override
    public Curriculum findByCode(String code) {
        Session session = entityManager.unwrap(Session.class);
        Query<Curriculum> query = session.createQuery("from Curriculum where code=:code", Curriculum.class);
        query.setParameter("code", code);

        try {
            return query.getSingleResult();
        } catch (Exception exc) {
            return null;
        }
    }

    @Override
    public List<Curriculum> findAll() {
        Session session = entityManager.unwrap(Session.class);
        List<Curriculum> curriculums = session.createQuery("from Curriculum", Curriculum.class).getResultList();
        return curriculums;
    }

    @Override
    public List<Curriculum> findActiveCurriculums() {
        Session session = entityManager.unwrap(Session.class);
        Query<Curriculum> query = session.createQuery("from Curriculum where isActive=:isActive", Curriculum.class);
        query.setParameter("isActive", true);
        return query.getResultList();
    }

    @Override
    public void deleteById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete Curriculum where id=:curriculumId");
        query.setParameter("curriculumId", id);
        query.executeUpdate();
    }

    @Override
    public void update(Curriculum curriculum) {
        Session session = entityManager.unwrap(Session.class);
        session.update(curriculum);
    }
}