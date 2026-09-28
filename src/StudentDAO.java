import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean addStudent(Student s) {
        String sql = """
            INSERT INTO students(roll_no, name, phone, email, branch, year, password)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getRollNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getBranch());
            ps.setInt(6, s.getYear());
            ps.setString(7, s.getPassword());

            return ps.executeUpdate() == 1;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Student already exists or violates a database constraint.");
        } catch (SQLException e) {
            System.out.println("Add student error: " + e.getMessage());
        }
        return false;
    }

    public Student findStudent(String rollNo) {
        String sql = "SELECT * FROM students WHERE roll_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapStudent(rs);
            }

        } catch (SQLException e) {
            System.out.println("Find student error: " + e.getMessage());
        }
        return null;
    }

    public Student loginStudent(String rollNo, String password) {
        String sql = "SELECT * FROM students WHERE roll_no = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapStudent(rs);
            }

        } catch (SQLException e) {
            System.out.println("Student login error: " + e.getMessage());
        }
        return null;
    }

    public boolean updateStudent(String rollNo, String name, String phone,
                                 String email, String branch) {
        String sql = """
            UPDATE students
            SET name = ?, phone = ?, email = ?, branch = ?
            WHERE roll_no = ?
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, branch);
            ps.setString(5, rollNo);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Update student error: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteStudent(String rollNo) {
        String sql = "DELETE FROM students WHERE roll_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Delete student error: " + e.getMessage());
        }
        return false;
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY roll_no";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapStudent(rs));

        } catch (SQLException e) {
            System.out.println("List students error: " + e.getMessage());
        }
        return list;
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getString("roll_no"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("branch"),
                rs.getInt("year"),
                rs.getString("password")
        );
    }
}
