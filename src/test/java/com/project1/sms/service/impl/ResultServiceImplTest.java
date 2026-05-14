package com.project1.sms.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project1.sms.dto.SemesterResultDto;
import com.project1.sms.model.Course;
import com.project1.sms.model.CourseOffering;
import com.project1.sms.model.Grade;
import com.project1.sms.model.Student;
import com.project1.sms.repository.GradeRepository;
import com.project1.sms.repository.ResultRepository;
import com.project1.sms.repository.StudentRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResultServiceImplTest {

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private StudentRepository studentRepository;

    private ResultServiceImpl resultService;
    private Student student;

    @BeforeEach
    void setUp() {
        resultService = new ResultServiceImpl(resultRepository, gradeRepository, studentRepository);
        student = new Student();
        student.setStudentId("ETS-001");
        student.setFullName("Test Student");
    }

    @Test
    void calculateSemesterResultCalculatesGpaAndCgpa() {
        Grade math = grade("MATH101", "Math", 3, 2026, 1, "A");
        Grade english = grade("ENG101", "English", 2, 2026, 1, "B+");
        Grade history = grade("HIST101", "History", 3, 2025, 2, "B");

        when(studentRepository.findByStudentId("ETS-001")).thenReturn(Optional.of(student));
        when(gradeRepository.findSemesterGrades(student, 2026, 1)).thenReturn(List.of(math, english));
        when(gradeRepository.findGradesThroughSemester(student, 2026, 1))
                .thenReturn(List.of(history, math, english));

        SemesterResultDto result = resultService.calculateSemesterResult("ETS-001", 2026, 1);

        assertThat(result.gpa()).isEqualByComparingTo(new BigDecimal("3.80"));
        assertThat(result.cgpa()).isEqualByComparingTo(new BigDecimal("3.50"));
        assertThat(result.semesterCreditHours()).isEqualTo(5);
        assertThat(result.cumulativeCreditHours()).isEqualTo(8);
        assertThat(result.courses()).extracting("courseCode").containsExactly("ENG101", "MATH101");
    }

    @Test
    void recalculateAndSaveSemesterResultPersistsCalculatedValues() {
        Grade programming = grade("CS101", "Programming", 4, 2026, 1, "A-");

        when(studentRepository.findByStudentId("ETS-001")).thenReturn(Optional.of(student));
        when(gradeRepository.findSemesterGrades(student, 2026, 1)).thenReturn(List.of(programming));
        when(gradeRepository.findGradesThroughSemester(student, 2026, 1)).thenReturn(List.of(programming));
        when(resultRepository.findByStudentAndAcademicYearAndSemester(student, 2026, 1))
                .thenReturn(Optional.empty());

        SemesterResultDto result = resultService.recalculateAndSaveSemesterResult("ETS-001", 2026, 1);

        assertThat(result.gpa()).isEqualByComparingTo(new BigDecimal("3.75"));
        assertThat(result.cgpa()).isEqualByComparingTo(new BigDecimal("3.75"));
        verify(resultRepository).save(any());
    }

    private Grade grade(
            String courseCode,
            String courseName,
            Integer creditHour,
            Integer academicYear,
            Integer semester,
            String letterGrade
    ) {
        Course course = new Course();
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setCreditHour(creditHour);

        CourseOffering offering = new CourseOffering();
        offering.setCourse(course);
        offering.setAcademicYear(academicYear);
        offering.setSemester(semester);

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setOffering(offering);
        grade.setGrade(letterGrade);
        return grade;
    }
}
