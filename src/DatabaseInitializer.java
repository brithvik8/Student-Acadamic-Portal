import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void main(String[] args) {
        initialize();
    }

    public static void initialize() {
        createDatabase();
        createTables();
        createDefaultAdmin();
    }

    private static void createDatabase() {
        String host = System.getenv("MYSQL_HOST") != null && !System.getenv("MYSQL_HOST").isBlank()
                ? System.getenv("MYSQL_HOST") : "localhost";
        String port = System.getenv("MYSQL_PORT") != null && !System.getenv("MYSQL_PORT").isBlank()
                ? System.getenv("MYSQL_PORT") : "3306";
        String database = System.getenv("MYSQL_DATABASE") != null && !System.getenv("MYSQL_DATABASE").isBlank()
                ? System.getenv("MYSQL_DATABASE") : "student_academic_portal";
        String user = System.getenv("MYSQL_USER") != null && !System.getenv("MYSQL_USER").isBlank()
                ? System.getenv("MYSQL_USER") : "root";

        String url = "jdbc:mysql://" + host + ":" + port + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata";

        try (Connection con = java.sql.DriverManager.getConnection(
                url, user, DBConnectionPassword.get())) {

            try (Statement st = con.createStatement()) {
                st.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS " + database
                );
                System.out.println("Database checked/created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Database creation failed: " + e.getMessage());
            System.out.println("Make sure MySQL Server is running and the password is correct.");
        }
    }

    private static void createTables() {
        String[] statements = {
            """
            CREATE TABLE IF NOT EXISTS admins (
                admin_id INT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(50) NOT NULL UNIQUE,
                password VARCHAR(100) NOT NULL,
                name VARCHAR(100) NOT NULL,
                phone VARCHAR(20),
                email VARCHAR(100)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS students (
                roll_no VARCHAR(30) PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                phone VARCHAR(20),
                email VARCHAR(100),
                branch VARCHAR(100) NOT NULL,
                year INT NOT NULL,
                password VARCHAR(100) NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS subjects (
                subject_code VARCHAR(30) PRIMARY KEY,
                subject_name VARCHAR(100) NOT NULL,
                credits INT NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS academic_records (
                record_id INT PRIMARY KEY AUTO_INCREMENT,
                roll_no VARCHAR(30) NOT NULL,
                subject_code VARCHAR(30) NOT NULL,
                marks DOUBLE NOT NULL,
                semester INT NOT NULL,
                UNIQUE KEY unique_student_subject_semester
                    (roll_no, subject_code, semester),
                FOREIGN KEY (roll_no) REFERENCES students(roll_no)
                    ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY (subject_code) REFERENCES subjects(subject_code)
                    ON DELETE RESTRICT ON UPDATE CASCADE
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS attendance (
                attendance_id INT PRIMARY KEY AUTO_INCREMENT,
                roll_no VARCHAR(30) NOT NULL,
                subject_code VARCHAR(30) NOT NULL,
                total_classes INT NOT NULL,
                attended_classes INT NOT NULL,
                UNIQUE KEY unique_student_attendance_subject
                    (roll_no, subject_code),
                FOREIGN KEY (roll_no) REFERENCES students(roll_no)
                    ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY (subject_code) REFERENCES subjects(subject_code)
                    ON DELETE RESTRICT ON UPDATE CASCADE
            )
            """
        };

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            for (String sql : statements) {
                st.executeUpdate(sql);
            }
            System.out.println("All tables checked/created successfully.");

        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    private static void createDefaultAdmin() {
        String sql = """
            INSERT INTO admins(username, password, name, phone, email)
            SELECT ?, ?, ?, ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM admins WHERE username = ?
            )
            """;

        try (Connection con = DBConnection.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "admin");
            ps.setString(2, "admin123");
            ps.setString(3, "System Administrator");
            ps.setString(4, "9999999999");
            ps.setString(5, "admin@bvrit.edu");
            ps.setString(6, "admin");

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Default admin setup failed: " + e.getMessage());
        }
    }
}
