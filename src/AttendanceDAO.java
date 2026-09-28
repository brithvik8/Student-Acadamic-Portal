import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public boolean addAttendance(String rollNo, String subjectCode,
                                 int totalClasses, int attendedClasses) {
        String ensureSubjectSql = """
            INSERT INTO subjects (subject_code, subject_name, credits)
            VALUES (?, ?, 3)
            ON DUPLICATE KEY UPDATE subject_code = subject_code
            """;

        String sql = """
            INSERT INTO attendance
                (roll_no, subject_code, total_classes, attended_classes)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                total_classes = VALUES(total_classes),
                attended_classes = VALUES(attended_classes)
            """;

        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement psSub = con.prepareStatement(ensureSubjectSql)) {
                psSub.setString(1, subjectCode);
                psSub.setString(2, subjectCode);
                psSub.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, rollNo);
                ps.setString(2, subjectCode);
                ps.setInt(3, totalClasses);
                ps.setInt(4, attendedClasses);

                return ps.executeUpdate() >= 1;
            }

        } catch (SQLException e) {
            System.out.println("Attendance error: " + e.getMessage());
            return false;
        }
    }

    public List<Attendance> getAttendance(String rollNo) {
        List<Attendance> list = new ArrayList<>();

        String sql = """
            SELECT subject_code, total_classes, attended_classes
            FROM attendance
            WHERE roll_no = ?
            ORDER BY subject_code
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Attendance(
                            rs.getString("subject_code"),
                            rs.getInt("total_classes"),
                            rs.getInt("attended_classes")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Read attendance error: " + e.getMessage());
        }

        return list;
    }
}
