package com.project1.sms.repository;

import com.project1.sms.model.Grade;
import com.project1.sms.model.Student;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query("""
            select g
            from Grade g
            join fetch g.offering offering
            join fetch offering.course course
            where g.student = :student
              and offering.academicYear = :academicYear
              and offering.semester = :semester
            order by course.courseCode
            """)
    List<Grade> findSemesterGrades(
            @Param("student") Student student,
            @Param("academicYear") Integer academicYear,
            @Param("semester") Integer semester
    );

    @Query("""
            select g
            from Grade g
            join fetch g.offering offering
            join fetch offering.course course
            where g.student = :student
            order by offering.academicYear, offering.semester, course.courseCode
            """)
    List<Grade> findAllGradesForStudent(@Param("student") Student student);

    @Query("""
            select g
            from Grade g
            join fetch g.offering offering
            join fetch offering.course course
            where g.student = :student
              and (offering.academicYear < :academicYear
                   or (offering.academicYear = :academicYear and offering.semester <= :semester))
            order by offering.academicYear, offering.semester, course.courseCode
            """)
    List<Grade> findGradesThroughSemester(
            @Param("student") Student student,
            @Param("academicYear") Integer academicYear,
            @Param("semester") Integer semester
    );
}
