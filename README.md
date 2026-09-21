
# STUDENT ACADEMIC PORTAL

1. QUICK OVERVIEW
This is a console-based Student Academic Portal developed in Java.
It demonstrates core OOP concepts and provides Admin and Student workflows.

2. FEATURES
- Admin login
- Student login
- Add/update/delete/search students
- View all students
- Add subjects and marks
- Automatic grade calculation
- Semester GPA and overall CGPA
- Attendance percentage and eligibility
- File-based persistence using students.dat
- Input validation and exception handling

3. DEFAULT ADMIN LOGIN
Username: admin
Password: admin123

4. PROJECT FILES
Main.java              -> Program entry point
Person.java            -> Abstract parent class
Admin.java             -> Admin model / inheritance
Student.java           -> Student model and academic calculations
Subject.java           -> Subject model
AcademicRecord.java    -> Marks, grades and grade points
Attendance.java        -> Attendance calculations
StudentManager.java    -> Student collection and CRUD operations
FileManager.java       -> Save/load data
AcademicPortal.java    -> Menus, login and application controller

5. REQUIREMENTS
JDK 17 or newer is recommended because the project uses modern switch syntax.
No external libraries or database are required.

6. RUN FROM TERMINAL
Open the src folder:
    cd src

Compile:
    javac *.java

Run:
    java Main

7. DATA FILE
When data is saved, students.dat is created in the current working directory.
The program automatically loads it at startup.

8. TEST FLOW
A. Start program.
B. Login as admin using admin/admin123.
C. Add a student.
D. Add marks for at least 4 subjects.
E. Add attendance for at least 2 subjects.
F. Save data.
G. Logout.
H. Login as the student.
I. View profile, results, GPA/CGPA and attendance.
J. Exit and restart to verify persistence.

9. OOP CONCEPTS FOR VIVA
- Encapsulation: private fields with getters/setters
- Abstraction: abstract Person class
- Inheritance: Student and Admin extend Person
- Polymorphism: displayDetails() is overridden
- Constructors: used to initialize objects
- Collections: ArrayList
- Exception handling: validation and try/catch
- File handling: ObjectOutputStream/ObjectInputStream

10. IMPORTANT SUBMISSION NOTE
Replace placeholder team-member names and roll numbers in the report before submission.
Do not claim MySQL, JDBC, Swing, JavaFX, Spring, or another technology unless you actually add and test it.
