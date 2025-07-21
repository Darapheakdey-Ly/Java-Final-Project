import java.sql.*;
import java.util.Calendar;
import java.util.UUID;

public class StudentLogin {
    private final String url = "jdbc:sqlite:studentLogin.db";
    public StudentLogin(){
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
                email TEXT NOT NULL UNIQUE,
                password INTEGER
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

    public Integer[] studentHash(String email){
        try{
            int year = Integer.parseInt(email.substring(0,4)); // get year of enrollment
            int ID = Integer.parseInt(email.substring(4,7));
            return new Integer[]{year,ID};
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    //to be moved to admin only
    public void addStudent(String firstName, String DOB, String lastName, String major){
        String uuid = UUID.randomUUID().toString();
        String password = uuid.replaceAll("-", "").substring(0, 12); // 12-char password

        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String query = String.format("SELECT COUNT(*) FROM students%s",currentYear);
        int counter = 0;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                counter = rs.getInt(1);
                System.out.println("Number of entries in 'students' table: " + counter);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String email = (currentYear+String.format("%03d", counter + 1)+lastName.toLowerCase()+"@aupp.edu.kh");
        String sql = String.format("INSERT INTO students%s (email, password) VALUES(?, ?)",currentYear);

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            int rowsInserted = pstmt.executeUpdate();

            System.out.println("Rows inserted: " + rowsInserted);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws SQLException {


        StudentLogin studentLogin = new StudentLogin();
        Integer[] hash = studentLogin.studentHash("2024116@aupp.edu.au");
        System.out.println(hash[0] + " " + hash[1]);

        studentLogin.addStudent("John", "Doe", "Smith","CSA");
    }
}
