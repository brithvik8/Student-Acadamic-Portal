
CREATE DATABASE IF NOT EXISTS student_academic_portal;
USE student_academic_portal;
CREATE TABLE IF NOT EXISTS admins (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100)
);
CREATE TABLE IF NOT EXISTS students (
    roll_no VARCHAR(30) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    branch VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    password VARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS subjects (
    subject_code VARCHAR(30) PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL
);
CREATE TABLE IF NOT EXISTS academic_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    roll_no VARCHAR(30) NOT NULL,
    subject_code VARCHAR(30) NOT NULL,
    marks DOUBLE NOT NULL,
    semester INT NOT NULL,
    UNIQUE KEY unique_student_subject_semester (roll_no, subject_code, semester),
    FOREIGN KEY (roll_no) REFERENCES students(roll_no) ON DELETE CASCADE,
    FOREIGN KEY (subject_code) REFERENCES subjects(subject_code)
);
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    roll_no VARCHAR(30) NOT NULL,
    subject_code VARCHAR(30) NOT NULL,
    total_classes INT NOT NULL,
    attended_classes INT NOT NULL,
    UNIQUE KEY unique_student_attendance_subject (roll_no, subject_code),
    FOREIGN KEY (roll_no) REFERENCES students(roll_no) ON DELETE CASCADE,
    FOREIGN KEY (subject_code) REFERENCES subjects(subject_code)
);
INSERT INTO admins(username, password, name, phone, email)
SELECT 'admin',
    'admin123',
    'System Administrator',
    '9876543210',
    'admin@bvrit.edu'
WHERE NOT EXISTS (
        SELECT 1
        FROM admins
        WHERE username = 'admin'
    );
