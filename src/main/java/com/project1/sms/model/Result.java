package com.project1.sms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uk_result_student_year_semester",
                columnNames = {"student_id", "academic_year", "semester"}
        )
)
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "academic_year", nullable = false)
    private Integer academicYear;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal gpa;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal cgpa;

    @Column(nullable = false)
    private Integer semesterCreditHours;

    @Column(nullable = false)
    private Integer cumulativeCreditHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public BigDecimal getGpa() {
        return gpa;
    }

    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }

    public BigDecimal getCgpa() {
        return cgpa;
    }

    public void setCgpa(BigDecimal cgpa) {
        this.cgpa = cgpa;
    }

    public Integer getSemesterCreditHours() {
        return semesterCreditHours;
    }

    public void setSemesterCreditHours(Integer semesterCreditHours) {
        this.semesterCreditHours = semesterCreditHours;
    }

    public Integer getCumulativeCreditHours() {
        return cumulativeCreditHours;
    }

    public void setCumulativeCreditHours(Integer cumulativeCreditHours) {
        this.cumulativeCreditHours = cumulativeCreditHours;
    }
}
