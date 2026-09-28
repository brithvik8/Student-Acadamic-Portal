import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String HOST = System.getenv("MYSQL_HOST") != null && !System.getenv("MYSQL_HOST").isBlank()
            ? System.getenv("MYSQL_HOST") : "localhost";
    private static final String PORT = System.getenv("MYSQL_PORT") != null && !System.getenv("MYSQL_PORT").isBlank()
            ? System.getenv("MYSQL_PORT") : "3306";
    private static final String DATABASE = System.getenv("MYSQL_DATABASE") != null && !System.getenv("MYSQL_DATABASE").isBlank()
            ? System.getenv("MYSQL_DATABASE") : "student_academic_portal";
    private static final String USER = System.getenv("MYSQL_USER") != null && !System.getenv("MYSQL_USER").isBlank()
            ? System.getenv("MYSQL_USER") : "root";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata";

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, DBConnectionPassword.get());
    }
}
