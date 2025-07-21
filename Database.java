import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String url = "jdbc:sqlite:studentData.db";
    public static Connection initDatabase() throws SQLException {
        try(Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("A new database has been created.");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return DriverManager.getConnection(url);
    }
    public static void main(String[] args) {
        String url = "jdbc:sqlite:studentData.db";
        try(Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("A new database has been created.");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
