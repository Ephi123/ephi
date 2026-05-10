package com.project1.sms.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Complete GPA/CGPA summary for a student across all available semesters.
 */
public record StudentCgpaResult(
        String studentId,
        int totalCreditHours,
        BigDecimal totalQualityPoints,
        BigDecimal cgpa,
        List<SemesterGpaResult> semesters
) {
}
