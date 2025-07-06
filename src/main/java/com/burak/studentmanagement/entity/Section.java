package com.burak.studentmanagement.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "is required")
    @Size(min = 1, message = "is required")
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;

    @Column(name = "year_level")
    private int yearLevel;

    @Column(name = "semester")
    private int semester;

    @Column(name = "school_year")
    private String schoolYear;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Student> students;

    public Section() {
        this.students = new ArrayList<>();
    }

    public Section(String name, String description, Curriculum curriculum, int yearLevel, int semester,
            String schoolYear, int capacity) {
        this.name = name;
        this.description = description;
        this.curriculum = curriculum;
        this.yearLevel = yearLevel;
        this.semester = semester;
        this.schoolYear = schoolYear;
        this.capacity = capacity;
        this.isActive = true;
        this.students = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        if (students == null) {
            students = new ArrayList<>();
        }
        students.add(student);
        student.setSection(this);
    }

    public void removeStudent(Student student) {
        if (students.contains(student)) {
            students.remove(student);
            student.setSection(null);
        }
    }

    public int getCurrentEnrollment() {
        return students != null ? students.size() : 0;
    }

    public boolean isFull() {
        return getCurrentEnrollment() >= capacity;
    }

    public int getAvailableSlots() {
        return capacity - getCurrentEnrollment();
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", curriculum=" + (curriculum != null ? curriculum.getCode() : "null") +
                ", yearLevel=" + yearLevel +
                ", semester=" + semester +
                ", schoolYear='" + schoolYear + '\'' +
                ", capacity=" + capacity +
                ", isActive=" + isActive +
                '}';
    }
}