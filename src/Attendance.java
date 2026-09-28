public class Attendance {
    private String subjectCode;
    private int totalClasses;
    private int attendedClasses;

    public Attendance(String subjectCode, int totalClasses, int attendedClasses) {
        if (totalClasses <= 0)
            throw new IllegalArgumentException("Total classes must be greater than zero.");
        if (attendedClasses < 0 || attendedClasses > totalClasses)
            throw new IllegalArgumentException("Attended classes must be between 0 and total classes.");

        this.subjectCode = subjectCode;
        this.totalClasses = totalClasses;
        this.attendedClasses = attendedClasses;
    }

    public String getSubjectCode() { return subjectCode; }
    public int getTotalClasses() { return totalClasses; }
    public int getAttendedClasses() { return attendedClasses; }

    public double calculatePercentage() {
        return ((double) attendedClasses / totalClasses) * 100;
    }

    public String getStatus() {
        return calculatePercentage() >= 75 ? "Eligible" : "Shortage";
    }
}
