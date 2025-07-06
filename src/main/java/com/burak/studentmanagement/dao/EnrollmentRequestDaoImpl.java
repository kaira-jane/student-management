package com.burak.studentmanagement.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.burak.studentmanagement.entity.EnrollmentRequest;
import com.burak.studentmanagement.entity.EnrollmentRequest.EnrollmentStatus;

@Repository
public class EnrollmentRequestDaoImpl implements EnrollmentRequestDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void save(EnrollmentRequest enrollmentRequest) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(enrollmentRequest);
    }

    @Override
    public EnrollmentRequest findById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query<EnrollmentRequest> query = session.createQuery("from EnrollmentRequest where id=:requestId",
                EnrollmentRequest.class);
        query.setParameter("requestId", id);

        try {
            return query.getSingleResult();
        } catch (Exception exc) {
            return null;
        }
    }

    @Override
    public List<EnrollmentRequest> findAll() {
        Session session = entityManager.unwrap(Session.class);
        List<EnrollmentRequest> requests = session
                .createQuery("from EnrollmentRequest order by requestDate desc", EnrollmentRequest.class)
                .getResultList();
        return requests;
    }

    @Override
    public List<EnrollmentRequest> findByStatus(EnrollmentStatus status) {
        Session session = entityManager.unwrap(Session.class);
        Query<EnrollmentRequest> query = session.createQuery(
                "from EnrollmentRequest where status=:status order by requestDate desc", EnrollmentRequest.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<EnrollmentRequest> findByStudentId(int studentId) {
        Session session = entityManager.unwrap(Session.class);
        Query<EnrollmentRequest> query = session.createQuery(
                "from EnrollmentRequest where student.id=:studentId order by requestDate desc",
                EnrollmentRequest.class);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }

    @Override
    public List<EnrollmentRequest> findByStudentIdAndStatus(int studentId, EnrollmentStatus status) {
        Session session = entityManager.unwrap(Session.class);
        Query<EnrollmentRequest> query = session.createQuery(
                "from EnrollmentRequest where student.id=:studentId and status=:status order by requestDate desc",
                EnrollmentRequest.class);
        query.setParameter("studentId", studentId);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public EnrollmentRequest findByStudentAndCourse(int studentId, int courseId) {
        Session session = entityManager.unwrap(Session.class);
        Query<EnrollmentRequest> query = session.createQuery(
                "from EnrollmentRequest where student.id=:studentId and course.id=:courseId", EnrollmentRequest.class);
        query.setParameter("studentId", studentId);
        query.setParameter("courseId", courseId);

        try {
            return query.getSingleResult();
        } catch (Exception exc) {
            return null;
        }
    }

    @Override
    public void deleteById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from EnrollmentRequest where id=:requestId");
        query.setParameter("requestId", id);
        query.executeUpdate();
    }

    @Override
    public void update(EnrollmentRequest enrollmentRequest) {
        Session session = entityManager.unwrap(Session.class);
        session.update(enrollmentRequest);
    }
}