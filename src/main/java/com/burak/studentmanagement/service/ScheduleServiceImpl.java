package com.burak.studentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.burak.studentmanagement.dao.ScheduleDao;
import com.burak.studentmanagement.entity.Schedule;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleDao scheduleDao;

    @Override
    @Transactional
    public void save(Schedule schedule) {
        scheduleDao.save(schedule);
    }

    @Override
    @Transactional
    public Schedule findById(int id) {
        return scheduleDao.findById(id);
    }

    @Override
    @Transactional
    public List<Schedule> findByCourseId(int courseId) {
        return scheduleDao.findByCourseId(courseId);
    }

    @Override
    @Transactional
    public List<Schedule> findByCourseIdAndActive(int courseId, boolean isActive) {
        return scheduleDao.findByCourseIdAndActive(courseId, isActive);
    }

    @Override
    @Transactional
    public List<Schedule> findAll() {
        return scheduleDao.findAll();
    }

    @Override
    @Transactional
    public List<Schedule> findBySemesterAndSchoolYear(String semester, String schoolYear) {
        return scheduleDao.findBySemesterAndSchoolYear(semester, schoolYear);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        scheduleDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByCourseId(int courseId) {
        scheduleDao.deleteByCourseId(courseId);
    }
}