package com.burak.studentmanagement.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.burak.studentmanagement.entity.CurriculumCourse;

@Repository
public class CurriculumCourseDaoImpl implements CurriculumCourseDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void save(CurriculumCourse curriculumCourse) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(curriculumCourse);
    }

    @Override
    public CurriculumCourse findById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query<CurriculumCourse> query = session.createQuery("from CurriculumCourse where id=:id",
                CurriculumCourse.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (Exception exc) {
            return null;
        }
    }

    @Override
    public List<CurriculumCourse> findByCurriculumId(int curriculumId) {
        Session session = entityManager.unwrap(Session.class);
        Query<CurriculumCourse> query = session.createQuery("from CurriculumCourse where curriculum.id=:curriculumId",
                CurriculumCourse.class);
        query.setParameter("curriculumId", curriculumId);
        return query.getResultList();
    }

    @Override
    public List<CurriculumCourse> findByCurriculumIdAndYearLevel(int curriculumId, int yearLevel) {
        Session session = entityManager.unwrap(Session.class);
        Query<CurriculumCourse> query = session.createQuery(
                "from CurriculumCourse where curriculum.id=:curriculumId and yearLevel=:yearLevel",
                CurriculumCourse.class);
        query.setParameter("curriculumId", curriculumId);
        query.setParameter("yearLevel", yearLevel);
        return query.getResultList();
    }

    @Override
    public List<CurriculumCourse> findByCurriculumIdAndSemester(int curriculumId, int semester) {
        Session session = entityManager.unwrap(Session.class);
        Query<CurriculumCourse> query = session.createQuery(
                "from CurriculumCourse where curriculum.id=:curriculumId and semester=:semester",
                CurriculumCourse.class);
        query.setParameter("curriculumId", curriculumId);
        query.setParameter("semester", semester);
        return query.getResultList();
    }

    @Override
    public List<CurriculumCourse> findByCurriculumIdAndYearLevelAndSemester(int curriculumId, int yearLevel,
            int semester) {
        Session session = entityManager.unwrap(Session.class);
        Query<CurriculumCourse> query = session.createQuery(
                "from CurriculumCourse where curriculum.id=:curriculumId and yearLevel=:yearLevel and semester=:semester",
                CurriculumCourse.class);
        query.setParameter("curriculumId", curriculumId);
        query.setParameter("yearLevel", yearLevel);
        query.setParameter("semester", semester);
        return query.getResultList();
    }

    @Override
    public List<CurriculumCourse> findAll() {
        Session session = entityManager.unwrap(Session.class);
        List<CurriculumCourse> curriculumCourses = session.createQuery("from CurriculumCourse", CurriculumCourse.class)
                .getResultList();
        return curriculumCourses;
    }

    @Override
    public void deleteById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete CurriculumCourse where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteByCurriculumId(int curriculumId) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete CurriculumCourse where curriculum.id=:curriculumId");
        query.setParameter("curriculumId", curriculumId);
        query.executeUpdate();
    }
}
