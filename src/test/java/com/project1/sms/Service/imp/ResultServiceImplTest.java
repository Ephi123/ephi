package com.project1.sms.Service.imp;

import com.project1.sms.Service.ResultService;
import com.project1.sms.dto.CourseResultRecord;
import com.project1.sms.dto.SemesterGpaResult;
import com.project1.sms.dto.StudentCgpaResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResultServiceImplTest {
    private final ResultService resultService = new ResultServiceImpl();

    @Test
    void calculatesSemesterGpaUsingCreditHourWeightedGrades() {
        SemesterGpaResult result = resultService.calculateSemesterGpa("S-001", 2026, 1, 1, sampleResults());

        assertEquals(9, result.totalCreditHours());
        assertEquals(new BigDecimal("31.50"), result.totalQualityPoints());
        assertEquals(new BigDecimal("3.50"), result.gpa());
    }

    @Test
    void calculatesEachSemesterAndOverallCgpaForStudent() {
        StudentCgpaResult result = resultService.calculateCgpa("S-001", sampleResults());

        assertEquals(2, result.semesters().size());
        assertEquals(16, result.totalCreditHours());
        assertEquals(new BigDecimal("52.50"), result.totalQualityPoints());
        assertEquals(new BigDecimal("3.28"), result.cgpa());
    }

    @Test
    void rejectsUnknownGrades() {
        List<CourseResultRecord> results = List.of(
                new CourseResultRecord("S-001", "BAD101", "Bad Grade", 3, "NG", 2026, 1, 1)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> resultService.calculateCgpa("S-001", results)
        );
        assertEquals("Unsupported grade: NG", exception.getMessage());
    }

    private List<CourseResultRecord> sampleResults() {
        return List.of(
                new CourseResultRecord("S-001", "MATH101", "Calculus", 3, "A", 2026, 1, 1),
                new CourseResultRecord("S-001", "ENG101", "English", 2, "B+", 2026, 1, 1),
                new CourseResultRecord("S-001", "CS101", "Programming", 4, "B", 2026, 1, 1),
                new CourseResultRecord("S-001", "CS102", "Data Structures", 4, "B+", 2026, 1, 2),
                new CourseResultRecord("S-001", "PHY101", "Physics", 3, "C", 2026, 1, 2),
                new CourseResultRecord("S-002", "MATH101", "Calculus", 3, "F", 2026, 1, 1)
        );
    }
}
