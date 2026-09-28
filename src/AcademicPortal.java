import java.util.List;
import java.util.Scanner;

public class AcademicPortal {

    private final Scanner scanner = new Scanner(System.in);
    private final StudentDAO studentDAO = new StudentDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private final AcademicDAO academicDAO = new AcademicDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    public void start() {
        DatabaseInitializer.initialize();

        int choice;
        do {
            System.out.println("\n==============================================");
            System.out.println("          STUDENT ACADEMIC PORTAL");
            System.out.println("==============================================");
            System.out.println("1. Admin Login");
            System.out.println("2. Student Login");
            System.out.println("0. Exit");
            System.out.println("==============================================");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> adminLogin();
                case 2 -> studentLogin();
                case 0 -> System.out.println("Thank you for using the portal!");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        scanner.close();
    }

    private void adminLogin() {
        System.out.println("\n========== ADMIN LOGIN ==========");
        String username = read("Username: ");
        String password = read("Password: ");

        Admin admin = adminDAO.login(username, password);

        if (admin != null) {
            System.out.println("Login successful.");
            adminMenu(admin);
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private void studentLogin() {
        System.out.println("\n========== STUDENT LOGIN ==========");
        String rollNo = read("Roll Number: ");
        String password = read("Password: ");

        Student student = studentDAO.loginStudent(rollNo, password);

        if (student != null) {
            System.out.println("Login successful.");
            studentMenu(student);
        } else {
            System.out.println("Invalid student credentials.");
        }
    }

    private void adminMenu(Admin admin) {
        int choice;

        do {
            System.out.println("\n==============================================");
            System.out.println("               ADMIN DASHBOARD");
            System.out.println("==============================================");
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Search Student");
            System.out.println("5. View All Students");
            System.out.println("6. Add/Update Marks");
            System.out.println("7. View Student Result");
            System.out.println("8. Add/Update Attendance");
            System.out.println("9. View Attendance");
            System.out.println("10. Admin Profile");
            System.out.println("0. Logout");

            choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> updateStudent();
                case 3 -> deleteStudent();
                case 4 -> searchStudent();
                case 5 -> viewAllStudents();
                case 6 -> addMarks();
                case 7 -> viewResult();
                case 8 -> addAttendance();
                case 9 -> viewAttendance();
                case 10 -> admin.displayDetails();
                case 0 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void addStudent() {
        String roll = read("Roll Number: ");

        if (studentDAO.findStudent(roll) != null) {
            System.out.println("Student already exists.");
            return;
        }

        String name = read("Name: ");
        String phone = read("Phone: ");
        String email = read("Email: ");
        String branch = read("Branch: ");
        int year = readInt("Year (1-4): ");
        String password = read("Student Password: ");

        if (year < 1 || year > 4) {
            System.out.println("Invalid year.");
            return;
        }

        Student student = new Student(
                roll, name, phone, email, branch, year, password);

        if (studentDAO.addStudent(student))
            System.out.println("Student added to MySQL successfully.");
    }

    private void updateStudent() {
        String roll = read("Roll Number: ");
        Student student = studentDAO.findStudent(roll);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        String name = read("Name [" + student.getName() + "]: ");
        String phone = read("Phone [" + student.getPhone() + "]: ");
        String email = read("Email [" + student.getEmail() + "]: ");
        String branch = read("Branch [" + student.getBranch() + "]: ");

        if (name.isBlank()) name = student.getName();
        if (phone.isBlank()) phone = student.getPhone();
        if (email.isBlank()) email = student.getEmail();
        if (branch.isBlank()) branch = student.getBranch();

        if (studentDAO.updateStudent(roll, name, phone, email, branch))
            System.out.println("Student updated in MySQL.");
    }

    private void deleteStudent() {
        String roll = read("Roll Number: ");

        if (studentDAO.findStudent(roll) == null) {
            System.out.println("Student not found.");
            return;
        }

        String confirm = read("Confirm delete (Y/N): ");

        if (confirm.equalsIgnoreCase("Y")) {
            if (studentDAO.deleteStudent(roll))
                System.out.println("Student deleted from MySQL.");
        }
    }

    private void searchStudent() {
        String roll = read("Roll Number: ");
        Student student = studentDAO.findStudent(roll);

        if (student != null) student.displayDetails();
        else System.out.println("Student not found.");
    }

    private void viewAllStudents() {
        List<Student> students = studentDAO.getAllStudents();

        System.out.println("\n============== STUDENTS ==============");
        System.out.printf("%-15s %-20s %-15s %-8s%n",
                "Roll No", "Name", "Branch", "Year");

        for (Student s : students) {
            System.out.printf("%-15s %-20s %-15s %-8d%n",
                    s.getRollNo(), s.getName(), s.getBranch(), s.getYear());
        }
    }

    private void addMarks() {
        String roll = read("Student Roll Number: ");

        if (studentDAO.findStudent(roll) == null) {
            System.out.println("Student not found.");
            return;
        }

        String code = read("Subject Code: ");
        String name = read("Subject Name: ");
        int credits = readInt("Credits: ");
        int semester = readInt("Semester: ");
        double marks = readDouble("Marks (0-100): ");

        try {
            Subject subject = new Subject(code, name, credits);
            new AcademicRecord(subject, marks, semester);

            if (academicDAO.addAcademicRecord(roll, subject, marks, semester))
                System.out.println("Academic record saved to MySQL.");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewResult() {
        String roll = read("Student Roll Number: ");

        if (studentDAO.findStudent(roll) == null) {
            System.out.println("Student not found.");
            return;
        }

        displayResult(roll);
    }

    private void displayResult(String roll) {
        List<AcademicRecord> records = academicDAO.getRecords(roll);

        if (records.isEmpty()) {
            System.out.println("No academic records.");
            return;
        }

        System.out.println("\n================ RESULT ================");
        System.out.printf("%-10s %-22s %-8s %-8s %-8s%n",
                "Code", "Subject", "Marks", "Grade", "Point");

        double totalWeighted = 0;
        int totalCredits = 0;
        double totalMarks = 0;

        for (AcademicRecord r : records) {
            System.out.printf("%-10s %-22s %-8.2f %-8s %-8.1f%n",
                    r.getSubject().getSubjectCode(),
                    r.getSubject().getSubjectName(),
                    r.getMarks(),
                    r.calculateGrade(),
                    r.getGradePoint());

            totalMarks += r.getMarks();
            totalWeighted += r.getGradePoint() * r.getSubject().getCredits();
            totalCredits += r.getSubject().getCredits();
        }

        System.out.printf("%nAverage Marks: %.2f%n",
                totalMarks / records.size());

        System.out.printf("Overall CGPA: %.2f%n",
                totalCredits == 0 ? 0 : totalWeighted / totalCredits);
    }

    private void addAttendance() {
        String roll = read("Student Roll Number: ");

        if (studentDAO.findStudent(roll) == null) {
            System.out.println("Student not found.");
            return;
        }

        String code = read("Subject Code: ");
        int total = readInt("Total Classes: ");
        int attended = readInt("Attended Classes: ");

        try {
            Attendance a = new Attendance(code, total, attended);

            if (attendanceDAO.addAttendance(
                    roll, code, total, attended)) {

                System.out.printf("Attendance saved: %.2f%%%n",
                        a.calculatePercentage());
                System.out.println("Status: " + a.getStatus());
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewAttendance() {
        String roll = read("Student Roll Number: ");

        if (studentDAO.findStudent(roll) == null) {
            System.out.println("Student not found.");
            return;
        }

        List<Attendance> list = attendanceDAO.getAttendance(roll);

        System.out.println("\n================ ATTENDANCE ================");
        System.out.printf("%-12s %-12s %-12s %-15s %-10s%n",
                "Subject", "Total", "Attended", "Percentage", "Status");

        for (Attendance a : list) {
            System.out.printf("%-12s %-12d %-12d %-15.2f %-10s%n",
                    a.getSubjectCode(),
                    a.getTotalClasses(),
                    a.getAttendedClasses(),
                    a.calculatePercentage(),
                    a.getStatus());
        }
    }

    private void studentMenu(Student student) {
        int choice;

        do {
            System.out.println("\n========== STUDENT DASHBOARD ==========");
            System.out.println("Welcome, " + student.getName());
            System.out.println("1. View Profile");
            System.out.println("2. View Academic Result");
            System.out.println("3. View Attendance");
            System.out.println("0. Logout");

            choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> student.displayDetails();
                case 2 -> displayResult(student.getRollNo());
                case 3 -> {
                    List<Attendance> list =
                            attendanceDAO.getAttendance(student.getRollNo());

                    for (Attendance a : list) {
                        System.out.printf(
                                "%s : %.2f%% (%s)%n",
                                a.getSubjectCode(),
                                a.calculatePercentage(),
                                a.getStatus());
                    }
                }
                case 0 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private String read(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private int readInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(read(message));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    private double readDouble(String message) {
        while (true) {
            try {
                return Double.parseDouble(read(message));
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }
}
