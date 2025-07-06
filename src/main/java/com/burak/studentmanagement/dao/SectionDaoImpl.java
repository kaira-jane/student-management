package com.burak.studentmanagement.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.burak.studentmanagement.entity.Section;

@Repository
public class SectionDaoImpl implements SectionDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void save(Section section) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(section);
    }

    @Override
    public Section findById(int id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Section.class, id);
    }

    @Override
    public Section findByIdWithStudents(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query<Section> theQuery = session.createQuery(
                "from Section s left join fetch s.students where s.id = :sectionId", Section.class);
        theQuery.setParameter("sectionId", id);

        try {
            return theQuery.getSingleResult();
        } catch (Exception exc) {
            return null;
        }
    }

    @Override
    public List<Section> findAll() {
        Session session = entityManager.unwrap(Session.class);
        Query<Section> theQuery = session.createQuery(
                "from Section order by curriculum.code, yearLevel, semester, name", Section.class);
        return theQuery.getResultList();
    }

    @Override
    public List<Section> findAllWithStudents() {
        Session session = entityManager.unwrap(Session.class);
        Query<Section> theQuery = session.createQuery(
                "from Section s left join fetch s.students order by s.curriculum.code, s.yearLevel, s.semester, s.name",
                Section.class);
        return theQuery.getResultList();
    }

    @Override
    public List<Section> findByCurriculumId(int curriculumId) {
        Session session = entityManager.unwrap(Session.class);
        Query<Section> theQuery = session.createQuery(
                "from Section where curriculum.id = :curriculumId order by yearLevel, semester, name", Section.class);
        theQuery.setParameter("curriculumId", curriculumId);
        return theQuery.getResultList();
    }

    @Override
    public List<Section> findByCurriculumIdAndActive(int curriculumId, boolean isActive) {
        Session session = entityManager.unwrap(Session.class);
        Query<Section> theQuery = session.createQuery(
                "from Section where curriculum.id = :curriculumId and isActive = :isActive order by yearLevel, semester, name",
                Section.class);
        theQuery.setParameter("curriculumId", curriculumId);
        theQuery.setParameter("isActive", isActive);
        return theQuery.getResultList();
    }

    @Override
    public List<Section> findByYearLevelAndSemester(int yearLevel, int semester) {
        Session session = entityManager.unwrap(Session.class);
        Query<Section> theQuery = session.createQuery(
                "from Section where yearLevel = :yearLevel and semester = :semester and isActive = true order by name",
                Section.class);
        theQuery.setParameter("yearLevel", yearLevel);
        theQuery.setParameter("semester", semester);
        return theQuery.getResultList();
    }

    @Override
    public List<Section> findBySchoolYear(String schoolYear) {
        Session session = entityManager.unwrap(Session.class);
        Query<Section> theQuery = session.createQuery(
                "from Section where schoolYear = :schoolYear and isActive = true order by curriculum.code, yearLevel, semester, name",
                Section.class);
        theQuery.setParameter("schoolYear", schoolYear);
        return theQuery.getResultList();
    }

    @Override
    public List<Section> findByActive(boolean isActive) {
        Session session = entityManager.unwrap(Session.class);
        Query<Section> theQuery = session.createQuery(
                "from Section where isActive = :isActive order by curriculum.code, yearLevel, semester, name",
                Section.class);
        theQuery.setParameter("isActive", isActive);
        return theQuery.getResultList();
    }

    @Override
    public List<Section> findByActiveWithStudents(boolean isActive) {
        Session session = entityManager.unwrap(Session.class);
        Query<Section> theQuery = session.createQuery(
                "from Section s left join fetch s.students where s.isActive = :isActive order by s.curriculum.code, s.yearLevel, s.semester, s.name",
                Section.class);
        theQuery.setParameter("isActive", isActive);
        return theQuery.getResultList();
    }

    @Override
    public void deleteById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query theQuery = session.createQuery("delete from Section where id = :sectionId");
        theQuery.setParameter("sectionId", id);
        theQuery.executeUpdate();
    }

    @Override
    public Section findByName(String name) {
        Session session = entityManager.unwrap(Session.class);
        Query<Section> theQuery = session.createQuery(
                "from Section where name = :name", Section.class);
        theQuery.setParameter("name", name);

        try {
            return theQuery.getSingleResult();
        } catch (Exception exc) {
            return null;
        }
    }

    @Override
    public int getCurrentEnrollmentCount(int sectionId) {
        Session session = entityManager.unwrap(Session.class);
        Query<Long> theQuery = session.createQuery(
                "select count(s) from Student s where s.section.id = :sectionId", Long.class);
        theQuery.setParameter("sectionId", sectionId);

        try {
            return theQuery.getSingleResult().intValue();
        } catch (Exception exc) {
            return 0;
        }
    }
}