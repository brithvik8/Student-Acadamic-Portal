import java.sql.*;

public class AdminDAO {

    public Admin login(String username, String password) {
        String sql = """
            SELECT admin_id, username, name, phone, email
            FROM admins
            WHERE username = ? AND password = ?
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                            rs.getInt("admin_id"),
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Admin login error: " + e.getMessage());
        }
        return null;
    }
}
