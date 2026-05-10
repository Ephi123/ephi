package com.project1.sms.dto;

import java.util.Objects;

/**
 * A single completed course result used by ResultService to calculate GPA/CGPA.
 */
public record CourseResultRecord(
        String studentId,
        String courseCode,
        String courseTitle,
        int creditHour,
        String grade,
        int academicYear,
        int studyYear,
        int semester
) {
    public CourseResultRecord {
        studentId = requireText(studentId, "studentId");
        courseCode = requireText(courseCode, "courseCode");
        courseTitle = Objects.requireNonNullElse(courseTitle, courseCode).trim();
        grade = requireText(grade, "grade").toUpperCase();
        if (creditHour <= 0) {
            throw new IllegalArgumentException("creditHour must be greater than zero");
        }
        if (semester <= 0) {
            throw new IllegalArgumentException("semester must be greater than zero");
        }
        if (studyYear <= 0) {
            throw new IllegalArgumentException("studyYear must be greater than zero");
        }
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value.trim();
    }
}
