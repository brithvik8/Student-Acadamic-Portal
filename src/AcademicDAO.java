import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcademicDAO {

    public boolean addAcademicRecord(String rollNo, Subject subject,
                                     double marks, int semester) {
        String subjectSql = """
            INSERT INTO subjects(subject_code, subject_name, credits)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE
                subject_name = VALUES(subject_name),
                credits = VALUES(credits)
            """;

        String recordSql = """
            INSERT INTO academic_records(roll_no, subject_code, marks, semester)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE marks = VALUES(marks)
            """;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(subjectSql);
                 PreparedStatement ps2 = con.prepareStatement(recordSql)) {

                ps1.setString(1, subject.getSubjectCode());
                ps1.setString(2, subject.getSubjectName());
                ps1.setInt(3, subject.getCredits());
                ps1.executeUpdate();

                ps2.setString(1, rollNo);
                ps2.setString(2, subject.getSubjectCode());
                ps2.setDouble(3, marks);
                ps2.setInt(4, semester);
                ps2.executeUpdate();

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Academic record error: " + e.getMessage());
            return false;
        }
    }

    public List<AcademicRecord> getRecords(String rollNo) {
        List<AcademicRecord> records = new ArrayList<>();

        String sql = """
            SELECT s.subject_code, s.subject_name, s.credits,
                   a.marks, a.semester
            FROM academic_records a
            JOIN subjects s ON a.subject_code = s.subject_code
            WHERE a.roll_no = ?
            ORDER BY a.semester, s.subject_code
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject(
                            rs.getString("subject_code"),
                            rs.getString("subject_name"),
                            rs.getInt("credits")
                    );

                    records.add(new AcademicRecord(
                            subject,
                            rs.getDouble("marks"),
                            rs.getInt("semester")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Read academic records error: " + e.getMessage());
        }

        return records;
    }
}
