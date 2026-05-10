package com.project1.sms.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * GPA summary for one student's one semester.
 */
public record SemesterGpaResult(
        String studentId,
        int academicYear,
        int studyYear,
        int semester,
        int totalCreditHours,
        BigDecimal totalQualityPoints,
        BigDecimal gpa,
        List<CourseResultRecord> courses
) {
}
