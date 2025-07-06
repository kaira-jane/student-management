package com.burak.studentmanagement.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "curriculum")
public class Curriculum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "Curriculum name is required")
    @Size(min = 3, max = 255, message = "Curriculum name must be between 3 and 255 characters")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Curriculum code is required")
    @Size(min = 2, max = 10, message = "Curriculum code must be between 2 and 10 characters")
    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "curriculum", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CurriculumCourse> curriculumCourses;

    public Curriculum() {
        this.curriculumCourses = new ArrayList<>();
    }

    public Curriculum(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.isActive = true;
        this.curriculumCourses = new ArrayList<>();
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<CurriculumCourse> getCurriculumCourses() {
        return curriculumCourses;
    }

    public void setCurriculumCourses(List<CurriculumCourse> curriculumCourses) {
        this.curriculumCourses = curriculumCourses;
    }

    public void addCurriculumCourse(CurriculumCourse curriculumCourse) {
        if (curriculumCourses == null) {
            curriculumCourses = new ArrayList<>();
        }
        curriculumCourses.add(curriculumCourse);
        curriculumCourse.setCurriculum(this);
    }

    public void removeCurriculumCourse(CurriculumCourse curriculumCourse) {
        if (curriculumCourses != null) {
            curriculumCourses.remove(curriculumCourse);
            curriculumCourse.setCurriculum(null);
        }
    }

    @Override
    public boolean equals(Object comparedObject) {
        if (this == comparedObject) {
            return true;
        }

        if (!(comparedObject instanceof Curriculum)) {
            return false;
        }

        Curriculum comparedCurriculum = (Curriculum) comparedObject;

        if (this.id == comparedCurriculum.id) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Curriculum [id=" + id + ", name=" + name + ", code=" + code + ", description=" + description +
                ", isActive=" + isActive + "]";
    }
}