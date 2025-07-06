package com.burak.studentmanagement.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.burak.studentmanagement.entity.Schedule;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void save(Schedule schedule) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(schedule);
    }

    @Override
    public Schedule findById(int id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Schedule.class, id);
    }

    @Override
    public List<Schedule> findByCourseId(int courseId) {
        Session session = entityManager.unwrap(Session.class);
        Query<Schedule> theQuery = session.createQuery(
                "from Schedule where course.id = :courseId order by dayOfWeek, startTime", Schedule.class);
        theQuery.setParameter("courseId", courseId);
        return theQuery.getResultList();
    }

    @Override
    public List<Schedule> findByCourseIdAndActive(int courseId, boolean isActive) {
        Session session = entityManager.unwrap(Session.class);
        Query<Schedule> theQuery = session.createQuery(
                "from Schedule where course.id = :courseId and isActive = :isActive order by dayOfWeek, startTime",
                Schedule.class);
        theQuery.setParameter("courseId", courseId);
        theQuery.setParameter("isActive", isActive);
        return theQuery.getResultList();
    }

    @Override
    public List<Schedule> findAll() {
        Session session = entityManager.unwrap(Session.class);
        Query<Schedule> theQuery = session.createQuery(
                "from Schedule order by course.code, dayOfWeek, startTime", Schedule.class);
        return theQuery.getResultList();
    }

    @Override
    public List<Schedule> findBySemesterAndSchoolYear(String semester, String schoolYear) {
        Session session = entityManager.unwrap(Session.class);
        Query<Schedule> theQuery = session.createQuery(
                "from Schedule where semester = :semester and schoolYear = :schoolYear and isActive = true order by dayOfWeek, startTime",
                Schedule.class);
        theQuery.setParameter("semester", semester);
        theQuery.setParameter("schoolYear", schoolYear);
        return theQuery.getResultList();
    }

    @Override
    public void deleteById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query theQuery = session.createQuery("delete from Schedule where id = :scheduleId");
        theQuery.setParameter("scheduleId", id);
        theQuery.executeUpdate();
    }

    @Override
    public void deleteByCourseId(int courseId) {
        Session session = entityManager.unwrap(Session.class);
        Query theQuery = session.createQuery("delete from Schedule where course.id = :courseId");
        theQuery.setParameter("courseId", courseId);
        theQuery.executeUpdate();
    }
}