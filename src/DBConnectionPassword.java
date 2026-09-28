import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionPassword {

    private DBConnectionPassword() {}

    public static String get() {
        String envPassword = System.getenv("MYSQL_PASSWORD");

        if (envPassword != null && !envPassword.isBlank()) {
            return envPassword;
        }

        return "sql@rithvik577";
    }
}
