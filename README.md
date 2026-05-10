# ephiwork

## ResultService

This project includes a `ResultService` implementation for calculating:

- semester GPA for one student and semester
- GPA for every semester available for a student
- cumulative GPA (CGPA) across all semesters available for a student

The calculation uses credit-hour weighting:

```text
quality point = grade point * credit hour
GPA = total quality points / total credit hours
CGPA = all quality points / all credit hours
```

Supported grade points are: `A+`, `A`, `A-`, `B+`, `B`, `B-`, `C+`, `C`, `C-`, `D`, and `F`.
