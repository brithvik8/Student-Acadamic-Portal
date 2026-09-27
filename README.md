# Student Academic Portal (JDBC + MySQL)

A Java command-line application for managing student profiles, academic marks and results, and subject attendance using MySQL.

## Overview

The portal provides separate administrator and student sign-in flows. Administrators maintain student records and enter academic and attendance data; students can view their own profile, results, and attendance. It is an object-oriented programming and JDBC laboratory project, not a web application.

## Features

- Administrator and student login menus.
- Add, edit, delete, search, and list student records.
- Add or update marks and view subject grades, average marks, and credit-weighted overall CGPA.
- Add or update attendance and view attendance percentage/status; 75% is the eligibility threshold.
- Automatically create/check the configured database and tables at startup, and seed an administrator if its username is absent.
- JDBC prepared statements for database operations; academic subject and record writes use a transaction.

## Tech Stack

| Area | Technology |
|---|---|
| Language/runtime | Java 17 |
| Database | MySQL |
| Database access | JDBC, MySQL Connector/J 26.7.0 |
| Build/run | Maven |
| Interface | Console (`Scanner` and standard output) |

There is no web framework, browser frontend, or HTTP/REST API.

## Project Structure

```text
Student_Academic_Portal_JDBC/
├── database/
│   └── schema.sql               # Database/table schema and bootstrap admin insert
├── src/
│   ├── Main.java                # Application entry point
│   ├── AcademicPortal.java       # Console menus and application workflows
│   ├── DatabaseInitializer.java # Creates/checks database, tables, and admin
│   ├── DBConnection.java        # Reads MySQL host/database/user settings
│   ├── DBConnectionPassword.java # Reads MYSQL_PASSWORD (source fallback exists)
│   ├── *DAO.java                # JDBC persistence for students/admin/marks/attendance
│   └── Person.java, Student.java, Admin.java,
│       Subject.java, AcademicRecord.java, Attendance.java
├── pom.xml                      # Maven build and dependency configuration
└── README.md
```

`target/` and `bin/`, if present, are generated build output and are not needed as source.

## Prerequisites

- JDK 17 or later.
- Apache Maven.
- MySQL Server reachable from the application machine.
- Git to clone the repository (optional if downloading a ZIP).
- VS Code or another editor is optional.

Verify installations with:

```bash
git --version
java --version
mvn --version
mysql --version
```

## Installation

Clone the repository and move into the created directory:

```bash
git clone https://github.com/brithvik8/Student-Acadamic-Portal.git
cd Student-Acadamic-Portal
```

Alternatively, download the repository ZIP from the GitHub page and extract it. Open a terminal in the project root, the folder containing `pom.xml`, `src/`, and `database/`.

Compile the project; Maven resolves the declared MySQL Connector/J dependency:

```bash
mvn clean compile
```

## Configuration

`src/DBConnection.java` reads the following environment variables. Unset or blank values use the defaults shown:

| Variable | Default |
|---|---|
| `MYSQL_HOST` | `localhost` |
| `MYSQL_PORT` | `3306` |
| `MYSQL_DATABASE` | `student_academic_portal` |
| `MYSQL_USER` | `root` |
| `MYSQL_PASSWORD` | Supplied through `DBConnectionPassword.get()` |

Set the database password in the shell before running the application. For PowerShell:

```powershell
$env:MYSQL_PASSWORD = "your-local-mysql-password"
```

No `.env` file is read, and there is no `.env.example`. `src/DBConnectionPassword.java` currently contains a source-code fallback password. Remove or replace that fallback before sharing/deploying and provide the password through the local environment; do not commit real credentials. The bootstrap administrator password is also defined in source. Change it before using the application with non-demo data. This README intentionally does not reproduce password values.

## Database Setup

The default database is `student_academic_portal`. At startup, `DatabaseInitializer` attempts to create the database and then creates the tables. The MySQL account must have the required database and table privileges. Alternatively, import the checked-in SQL file from the project root:

```bash
mysql -u root -p < database/schema.sql
```

The script creates and selects the database, then creates these tables in foreign-key order:

| Table | Purpose |
|---|---|
| `admins` | Administrator login and profile fields. |
| `students` | Student roll number, profile, branch/year, and login password. |
| `subjects` | Subject code, name, and credits. |
| `academic_records` | Student/subject marks and semester; unique per student, subject, and semester. |
| `attendance` | Class counts per student and subject; unique per student and subject. |

Student deletion cascades to the student's academic and attendance rows. Subject deletion is restricted while referenced. The SQL script inserts a bootstrap administrator only if that username is not already present.

Verify the database in the MySQL client:

```sql
SHOW DATABASES;
USE student_academic_portal;
SHOW TABLES;
DESCRIBE students;
DESCRIBE academic_records;
```

## Running the Project

Start MySQL, configure the connection variables if needed, and run from the project root:

```bash
mvn exec:java
```

The Maven exec plugin launches `Main`. There is no browser URL, separate frontend/backend process, or `run.bat` / `run.ps1` script in the project.

## Complete Project Workflow

### 1. Get the project

The repository is [brithvik8/Student-Acadamic-Portal](https://github.com/brithvik8/Student-Acadamic-Portal). Clone it with the commands in [Installation](#installation), or download and extract its ZIP.

### 2. Understand the components

- `src/` contains the console application, domain classes, JDBC connection code, database initializer, and DAO classes.
- `database/schema.sql` contains the SQL setup and seed administrator insert.
- `pom.xml` specifies Java 17, MySQL Connector/J, and Maven plugins.
- There are no frontend/backend subdirectories and no `.env` configuration file.

### 3. Install software and dependencies

Install JDK 17+, Maven, and MySQL Server. Install Git if using clone. Check versions with the commands in [Prerequisites](#prerequisites). From the project root, `mvn clean compile` downloads/resolves the dependencies defined by `pom.xml`; there is no npm install step.

### 4. Start and set up MySQL

Start the MySQL service using the method for your OS/installation. The default server is `localhost:3306`. Either let the application create the database/tables on startup (using an account with sufficient privileges), or import `database/schema.sql` with:

```bash
mysql -u root -p < database/schema.sql
```

Then verify with `SHOW TABLES;` and `DESCRIBE students;` as shown above.

### 5. Configure the connection

The application reads `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_DATABASE`, `MYSQL_USER`, and `MYSQL_PASSWORD`; defaults and setup are listed in [Configuration](#configuration). No variables are marked production-safe; passwords are stored in plaintext by this academic implementation, and the JDBC URL disables SSL. Use only a trusted learning environment and remove source-code password fallbacks before sharing.

### 6. Compile and start

With MySQL running and configuration set, execute:

```bash
mvn clean compile
mvn exec:java
```

The application starts in the terminal. No expected backend/frontend URLs or browser steps apply.

### 7. First-time setup checklist

- [ ] Clone the repository or extract its ZIP.
- [ ] Install JDK 17+, Maven, and MySQL Server.
- [ ] Start MySQL and configure `MYSQL_*` values as needed.
- [ ] Ensure the database account can create the database/tables, or import `database/schema.sql`.
- [ ] Compile with `mvn clean compile`.
- [ ] Start with `mvn exec:java`.
- [ ] Log in as an administrator and add a student.
- [ ] Enter marks and attendance, then sign in as the student to view them.

### 8. Application workflow

```text
Main
  ↓
AcademicPortal.start()
  ↓
DatabaseInitializer checks/creates database and tables
  ↓
Console login selection
  ├── Administrator → manage student profiles, marks, and attendance
  └── Student → view own profile, result, and attendance
  ↓
DAO classes execute JDBC queries against MySQL
```

The result view calculates grade bands, average marks, and credit-weighted CGPA. Attendance status is calculated from class counts, with 75% as the threshold. Deleting a student also removes their dependent academic and attendance rows through foreign-key cascades.

## Usage

1. Start the program and choose Admin Login or Student Login.
2. Sign in as an administrator to create student accounts and maintain details, marks, and attendance.
3. Sign in as a student using the roll number and password assigned when the account was created.
4. Use the student menu to view profile, academic result, or attendance. Choose `0` to log out or exit the relevant menu.

The program validates student year as 1–4, marks as 0–100, semester as 1–8, and attendance counts so attended classes cannot exceed total classes.

## User Roles

| Role | Capabilities |
|---|---|
| Administrator | Manage student profiles; enter/update marks and attendance; view results and attendance; view own profile. |
| Student | View own profile, academic result, and attendance. |

## API Documentation

There is no HTTP/REST API. Application operations are exposed through interactive console menus and internal Java DAO classes.

## Authentication & Security

The application checks administrator username/password or student roll number/password against MySQL records. DAO queries use `PreparedStatement`. Passwords are stored and compared as plain text; there is no password hashing, session/token system, or role authorization beyond the separate menus. The JDBC URL disables SSL (`useSSL=false`), so use this only in a trusted local learning environment. Configure database credentials outside source control and remove the source password fallback before sharing.

## Future Improvements

- Hash account passwords and remove the hard-coded database-password fallback.
- Require encrypted database connections outside local development.
- Add automated tests and stronger validation for database and input errors.
- Move connection settings to a managed configuration mechanism and improve error reporting.
- Add a graphical or web interface if the project scope expands.

## Troubleshooting

| Problem | Suggested check |
|---|---|
| MySQL connection failure | Confirm MySQL is running and `MYSQL_HOST` / `MYSQL_PORT` point to the server. |
| Access denied | Verify `MYSQL_USER` and `MYSQL_PASSWORD`; the account needs the required database privileges. |
| Database or tables missing | Run the application with a user allowed to create them, or import `database/schema.sql`. |
| Maven dependency resolution fails | Check network access to the configured Maven repository, then retry `mvn clean compile`. |
| Duplicate roll number or subject code | These are primary keys; use a unique value. |
| Login fails | Use an existing account. The bootstrap administrator is inserted only if its username is absent; student accounts are created through the admin menu. |

Node/npm setup, frontend startup, browser URLs, web-server port conflicts, CORS, and frontend/backend connectivity do not apply to this console application.

## Git Workflow for Contributors

Contributor: **Rithvik** — GitHub: [`birthvik8`](https://github.com/birthvik8).

A basic contribution workflow is:

```bash
git clone https://github.com/brithvik8/Student-Acadamic-Portal.git
cd Student-Acadamic-Portal
git checkout -b feature/<feature-name>

# Make and review changes

git add .
git commit -m "Add <feature-name>"
git push origin feature/<feature-name>
```

After pushing, create a Pull Request on GitHub. This is a general workflow and does not imply that the project enforces a particular branch naming policy.

## Contributors

- **Rithvik** — [GitHub: `birthvik8`](https://github.com/birthvik8).


## 📄 License

This project is intended primarily for academic and educational purposes.
