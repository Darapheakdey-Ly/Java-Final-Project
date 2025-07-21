import java.sql.*;
import java.util.Arrays;
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
                password TEXT NOT NULL,
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

    //to be changed to returning student object possibly
    public boolean login(String email, String password){
        Integer[] studentHash = studentHash(email);
        System.out.println(Arrays.toString(studentHash));
        //to be implemented, method for checking invalid email
        String sql = String.format("SELECT * FROM students%d WHERE rowid = ?", studentHash[0]);
        String trueEmail = "";
        String truePassword = "";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentHash[1]);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                trueEmail = rs.getString("email");
                truePassword = rs.getString("password");
            } else {
                System.out.println("No entry found with rowid " + studentHash[1]);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trueEmail.equals(email) && truePassword.equals(password);
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
        Integer[] hash = studentLogin.studentHash("2025001smith@aupp.edu.kh");
        System.out.println(hash[0] + " " + hash[1]);

        boolean login = studentLogin.login("2025002smith@aupp.edu.kh", "23a07ee62084");

        System.out.println(login);

        //studentLogin.addStudent("John", "Doe", "Smith","CSA");
    }
}
