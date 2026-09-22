# Student Academic Portal — JDBC + MySQL

## 1. What changed from the previous version?

The old `students.dat` file-storage design has been replaced with a MySQL database accessed through JDBC.

The project now uses:
- Java 17+
- MySQL Server 8.x+
- JDBC
- MySQL Connector/J 26.7.0
- Maven
- PreparedStatement
- Transactions for academic record saving
- Foreign keys and cascading student deletion

MySQL documents Connector/J as the official JDBC driver for MySQL and publishes it through Maven as `com.mysql:mysql-connector-j`. The current MySQL documentation lists Connector/J 26.7 and its MySQL compatibility information.

## 2. Project Structure

```text
Student_Academic_Portal_JDBC/
├── src/
│   ├── Main.java
│   ├── AcademicPortal.java
│   ├── Person.java
│   ├── Student.java
│   ├── Admin.java
│   ├── Subject.java
│   ├── AcademicRecord.java
│   ├── Attendance.java
│   ├── DBConnection.java
│   ├── DBConnectionPassword.java
│   ├── DatabaseInitializer.java
│   ├── StudentDAO.java
│   ├── AdminDAO.java
│   ├── AcademicDAO.java
│   └── AttendanceDAO.java
├── database/
│   └── schema.sql
├── pom.xml
├── Student_Academic_Portal_JDBC_Report.docx
└── README.md
```

## 3. Install MySQL

Install MySQL Server and make sure the MySQL service is running.

Open MySQL Workbench or MySQL command line and confirm that you can log in using the root account.

## 4. Configure Password

Open:

`src/DBConnection.java`

Change:

```java
private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";
```

to your actual MySQL root password.

For a cleaner approach, you can set the environment variable `MYSQL_PASSWORD`. The project includes `DBConnectionPassword.java` for database-initialization access.

## 5. Database Creation

The application automatically executes:

```sql
CREATE DATABASE IF NOT EXISTS student_academic_portal;
```

and creates all required tables.

You can also manually execute:

`database/schema.sql`

in MySQL Workbench.

## 6. Tables

### admins
Stores administrator login/profile data.

### students
Stores student profile and login data.

### subjects
Stores subject name and credits.

### academic_records
Stores marks and semester for each student-subject combination.

### attendance
Stores total and attended classes for each student and subject.

Foreign keys connect academic and attendance records to students.

## 7. Maven Run

Open a terminal in the project root.

Run:

```bash
mvn clean compile
mvn exec:java
```

Maven downloads MySQL Connector/J automatically.

## 8. Default Admin

Username:
`admin`

Password:
`admin123`

## 9. Typical Demonstration

1. Start MySQL Server.
2. Run the Maven project.
3. Login as admin.
4. Add a student.
5. Add four subjects and marks.
6. View the result.
7. Add attendance.
8. View attendance.
9. Logout.
10. Login as the student.
11. View profile, result and attendance.
12. Open MySQL Workbench and show that records exist in the tables.
13. Update/delete a student and demonstrate the database changes.

## 10. JDBC Concepts to Explain in Viva

- Driver / Connector
- JDBC connection
- DriverManager
- Connection
- PreparedStatement
- ResultSet
- SQL queries
- Transactions
- Commit and rollback
- Foreign keys
- DAO pattern
- CRUD operations
- SQL exceptions

## 11. OOP Concepts

Encapsulation:
Private fields and public methods.

Abstraction:
`Person` is an abstract class.

Inheritance:
`Student` and `Admin` extend `Person`.

Polymorphism:
`displayDetails()` is overridden.

Composition:
Academic records contain `Subject` objects.

## 12. Security Note

This is an academic laboratory project. Passwords are stored as plain text to keep the JDBC/OOP implementation easy to understand. A production system should use password hashing, stronger authentication and secrets management.

## 13. Troubleshooting

### Communications link failure
Make sure MySQL Server is running and the port is 3306.

### Access denied for user 'root'
Check the MySQL username/password and update `DBConnection.java`.

### Unknown database
Run the program once; `DatabaseInitializer` creates it automatically. You can also run `database/schema.sql`.

### Maven dependency error
Check internet access and run:

```bash
mvn clean compile
```

### Duplicate student
The roll number is the primary key.

### Delete student
Academic and attendance records are configured with `ON DELETE CASCADE`, so dependent records are removed when a student is deleted.

## 15. Submission Checklist

- Fill four team member names and roll numbers.
- Replace the MySQL password configuration.
- Run the project on your own computer.
- Capture screenshots of the working portal.
- Capture a MySQL Workbench screenshot showing tables/data.
- Insert screenshots into the Results section.
- Verify the final report page count.
- Do not claim features that were not demonstrated.
