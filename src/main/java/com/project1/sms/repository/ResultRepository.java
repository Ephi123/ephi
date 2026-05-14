package com.project1.sms.repository;

import com.project1.sms.model.Result;
import com.project1.sms.model.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Long> {

    Optional<Result> findByStudentAndAcademicYearAndSemester(
            Student student,
            Integer academicYear,
            Integer semester
    );

    List<Result> findByStudentOrderByAcademicYearAscSemesterAsc(Student student);
}
