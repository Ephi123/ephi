package com.project1.sms.Service.imp;

import com.project1.sms.Service.ResultService;
import com.project1.sms.dto.CourseResultRecord;
import com.project1.sms.dto.SemesterGpaResult;
import com.project1.sms.dto.StudentCgpaResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Default ResultService implementation.
 *
 * <p>Formula:</p>
 * <ul>
 *     <li>quality point for a course = grade point * credit hour</li>
 *     <li>semester GPA = semester quality points / semester credit hours</li>
 *     <li>CGPA = all quality points / all credit hours</li>
 * </ul>
 */
public class ResultServiceImpl implements ResultService {
    private static final int RESULT_SCALE = 2;
    private static final Map<String, BigDecimal> GRADE_POINTS = Map.ofEntries(
            Map.entry("A+", BigDecimal.valueOf(4.00)),
            Map.entry("A", BigDecimal.valueOf(4.00)),
            Map.entry("A-", BigDecimal.valueOf(3.75)),
            Map.entry("B+", BigDecimal.valueOf(3.50)),
            Map.entry("B", BigDecimal.valueOf(3.00)),
            Map.entry("B-", BigDecimal.valueOf(2.75)),
            Map.entry("C+", BigDecimal.valueOf(2.50)),
            Map.entry("C", BigDecimal.valueOf(2.00)),
            Map.entry("C-", BigDecimal.valueOf(1.75)),
            Map.entry("D", BigDecimal.valueOf(1.00)),
            Map.entry("F", BigDecimal.ZERO)
    );

    @Override
    public SemesterGpaResult calculateSemesterGpa(String studentId, int academicYear, int studyYear, int semester,
                                                  List<CourseResultRecord> courseResults) {
        String normalizedStudentId = requireStudentId(studentId);
        List<CourseResultRecord> semesterCourses = filterStudentCourses(normalizedStudentId, courseResults).stream()
                .filter(course -> course.academicYear() == academicYear)
                .filter(course -> course.studyYear() == studyYear)
                .filter(course -> course.semester() == semester)
                .sorted(courseComparator())
                .toList();

        return buildSemesterResult(normalizedStudentId, academicYear, studyYear, semester, semesterCourses);
    }

    @Override
    public List<SemesterGpaResult> calculateAllSemesterGpas(String studentId, List<CourseResultRecord> courseResults) {
        String normalizedStudentId = requireStudentId(studentId);
        Map<SemesterKey, List<CourseResultRecord>> groupedCourses = filterStudentCourses(normalizedStudentId, courseResults)
                .stream()
                .sorted(courseComparator())
                .collect(Collectors.groupingBy(
                        course -> new SemesterKey(course.academicYear(), course.studyYear(), course.semester()),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<SemesterGpaResult> results = new ArrayList<>();
        groupedCourses.forEach((key, courses) -> results.add(
                buildSemesterResult(normalizedStudentId, key.academicYear(), key.studyYear(), key.semester(), courses)
        ));
        return List.copyOf(results);
    }

    @Override
    public StudentCgpaResult calculateCgpa(String studentId, List<CourseResultRecord> courseResults) {
        String normalizedStudentId = requireStudentId(studentId);
        List<SemesterGpaResult> semesters = calculateAllSemesterGpas(normalizedStudentId, courseResults);
        int totalCreditHours = semesters.stream().mapToInt(SemesterGpaResult::totalCreditHours).sum();
        BigDecimal totalQualityPoints = semesters.stream()
                .map(SemesterGpaResult::totalQualityPoints)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new StudentCgpaResult(
                normalizedStudentId,
                totalCreditHours,
                totalQualityPoints.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                divide(totalQualityPoints, totalCreditHours),
                semesters
        );
    }

    private SemesterGpaResult buildSemesterResult(String studentId, int academicYear, int studyYear, int semester,
                                                  List<CourseResultRecord> courses) {
        int totalCreditHours = courses.stream().mapToInt(CourseResultRecord::creditHour).sum();
        BigDecimal totalQualityPoints = courses.stream()
                .map(this::qualityPoint)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new SemesterGpaResult(
                studentId,
                academicYear,
                studyYear,
                semester,
                totalCreditHours,
                totalQualityPoints.setScale(RESULT_SCALE, RoundingMode.HALF_UP),
                divide(totalQualityPoints, totalCreditHours),
                List.copyOf(courses)
        );
    }

    private BigDecimal qualityPoint(CourseResultRecord course) {
        return gradePoint(course.grade()).multiply(BigDecimal.valueOf(course.creditHour()));
    }

    private BigDecimal gradePoint(String grade) {
        BigDecimal point = GRADE_POINTS.get(grade.toUpperCase(Locale.ROOT));
        if (point == null) {
            throw new IllegalArgumentException("Unsupported grade: " + grade);
        }
        return point;
    }

    private BigDecimal divide(BigDecimal totalQualityPoints, int totalCreditHours) {
        if (totalCreditHours == 0) {
            return BigDecimal.ZERO.setScale(RESULT_SCALE, RoundingMode.HALF_UP);
        }
        return totalQualityPoints.divide(BigDecimal.valueOf(totalCreditHours), RESULT_SCALE, RoundingMode.HALF_UP);
    }

    private String requireStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("studentId is required");
        }
        return studentId.trim();
    }

    private List<CourseResultRecord> filterStudentCourses(String studentId, List<CourseResultRecord> courseResults) {
        Objects.requireNonNull(courseResults, "courseResults is required");
        return courseResults.stream()
                .filter(course -> studentId.equals(course.studentId()))
                .toList();
    }

    private Comparator<CourseResultRecord> courseComparator() {
        return Comparator.comparingInt(CourseResultRecord::academicYear)
                .thenComparingInt(CourseResultRecord::studyYear)
                .thenComparingInt(CourseResultRecord::semester)
                .thenComparing(CourseResultRecord::courseCode);
    }

    private record SemesterKey(int academicYear, int studyYear, int semester) {
    }
}
