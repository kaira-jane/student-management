package com.burak.studentmanagement.service;

import java.util.List;

import com.burak.studentmanagement.entity.Schedule;

public interface ScheduleService {

    public void save(Schedule schedule);

    public Schedule findById(int id);

    public List<Schedule> findByCourseId(int courseId);

    public List<Schedule> findByCourseIdAndActive(int courseId, boolean isActive);

    public List<Schedule> findAll();

    public List<Schedule> findBySemesterAndSchoolYear(String semester, String schoolYear);

    public void deleteById(int id);

    public void deleteByCourseId(int courseId);
}