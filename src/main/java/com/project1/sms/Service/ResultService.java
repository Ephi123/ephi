package com.project1.sms.Service;

import com.project1.sms.dto.CourseResultRecord;
import com.project1.sms.dto.SemesterGpaResult;
import com.project1.sms.dto.StudentCgpaResult;

import java.util.List;

/**
 * Calculates GPA for each semester and CGPA across all semesters for a student.
 */
public interface ResultService {
    SemesterGpaResult calculateSemesterGpa(String studentId, int academicYear, int studyYear, int semester,
                                           List<CourseResultRecord> courseResults);

    List<SemesterGpaResult> calculateAllSemesterGpas(String studentId, List<CourseResultRecord> courseResults);

    StudentCgpaResult calculateCgpa(String studentId, List<CourseResultRecord> courseResults);
}
