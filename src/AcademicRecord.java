public class AcademicRecord {
    private Subject subject;
    private double marks;
    private int semester;

    public AcademicRecord(Subject subject, double marks, int semester) {
        if (marks < 0 || marks > 100)
            throw new IllegalArgumentException("Marks must be between 0 and 100.");
        if (semester < 1 || semester > 8)
            throw new IllegalArgumentException("Semester must be between 1 and 8.");

        this.subject = subject;
        this.marks = marks;
        this.semester = semester;
    }

    public Subject getSubject() { return subject; }
    public double getMarks() { return marks; }
    public int getSemester() { return semester; }

    public String calculateGrade() {
        if (marks >= 90) return "O";
        if (marks >= 80) return "A+";
        if (marks >= 70) return "A";
        if (marks >= 60) return "B+";
        if (marks >= 50) return "B";
        return "F";
    }

    public double getGradePoint() {
        if (marks >= 90) return 10;
        if (marks >= 80) return 9;
        if (marks >= 70) return 8;
        if (marks >= 60) return 7;
        if (marks >= 50) return 6;
        return 0;
    }
}
