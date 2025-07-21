import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;

public class Database {
    private static final String url = "jdbc:sqlite:studentData.db";
    public Database(){
        try(Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("A new database has been created.");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for(int i = 2013; i <= currentYear+1; i++){
            String sql = String.format("""
            CREATE TABLE IF NOT EXISTS students%d (
                id INTEGER NOT NULL,
                email TEXT NOT NULL UNIQUE,
                major TEXT NOT NULL,
                firstname TEXT NOT NULL,
                lastname TEXT NOT NULL,
                dob DATE NOT NULL,
                enrolledyear INTEGER NOT NULL,
                phone TEXT NOT NULL,
                status TEXT NOT NULL
            );
            """,i);
            try (Connection conn = DriverManager.getConnection(url);
                 java.sql.Statement stmt = conn.createStatement()) {

                stmt.execute(sql);
                System.out.println("Table 'students' created or already exists.");

            } catch (SQLException e) {
                System.out.println("Error creating table: " + e.getMessage());
            }
        }

    }

    public static void main(String[] args) {
        Database database = new Database();
    }
}
