
import java.io.*;
import java.util.*;



public class enroll {
    //reads all courses in the courses file
    private static List<String> readAllCourses(){
        try (BufferedReader br = new BufferedReader(new FileReader("courses.csv"))) {
            return Arrays.asList(br.readLine().split(","));
        } catch (IOException e) {
            return Arrays.asList(new String[] {"Error: " + e.getMessage()}); 
        }  
    }
    public static void main(String[] args) {
        //sets names and headers for the databases
        String[] databases = {"studentLogInDB.csv","studentInfo.csv"};
        String[] headers = {"ID,userName,password","ID,userName,fullName,Courses"};
        
        //check if database exists, if not create them with proper headers
        for(int i = 0; i < 2; i++){
            File file = new File(databases[i]);

            if (!file.exists()){
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
                    bw.write(headers[i]);
                } catch (Exception e){
                    System.err.println(e.getMessage());
                }
            }

        }

        


        Database db = new Database(databases);
        Student Baba = db.addUser("Baba", "password");
        List<String> allCourses = readAllCourses();
        System.err.println(db.readCurrentCourses("Baba"));
        db.updateStudentCourses("fgs", allCourses);
        


        while (true) { 
            //log in loop
            break;
        }

        //if login == true:
        while (true) { 
            //menu loop
            break;
        }


    }
}

class Database {
    private String filePathLogin;
    private String filePathInfo;
    
    public Database(String[] filePath) {
        this.filePathLogin = filePath[0];
        this.filePathInfo = filePath[1];
    }
    
    public Student addUser(String username, String password) {
        //writes the student information to the login database
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePathLogin,true))){
            bw.newLine();
            //implement random ID TODO
            bw.write(1 + "," + username + "," + password);
            Student student = new Student(username, password, this);
            return student;
        }
        catch (Exception e){
            System.err.println("ERROR: "+e.getMessage());
            //add error msg
            System.exit(0);
        }
        return new Student(username,password, this);
    }
    
    public Student login(String username, String password) {
        try(BufferedReader br = new BufferedReader(new FileReader(filePathLogin))){
            
        }
        catch(Exception e){
            System.err.println("ERROR: "+e.getMessage());
            //add error msg
            System.exit(0);
        }
        return new Student(username,password, this);
    }
    
    public List<String> readCurrentCourses(String username) {
        List<String> courses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathInfo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", 4); // Ensure we split only into 3 parts
                if (data[1].equals(username)) {
                    if (data.length > 2 && !data[3].isEmpty()) {
                        courses = new ArrayList<>(Arrays.asList(data[3].split(";")));
                    }
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return courses;
    }
    
    public String addCourse(String username, String course){
        List<String> courses = readCurrentCourses(username);
        if (courses.contains(course)) {
            return "Course already registered.";
        }
        courses.add(course);
        updateStudentCourses(username, courses);
        return "Course added successfully.";
    }

    public String removeCourse(String username, String course){
        //
        return "success or failure";
    }

    public void updateStudentCourses(String username,List<String> courses){
        List<String[]> data = new ArrayList<>();
        String headers = "ID,userName,fullName,Courses";
        try(BufferedReader br = new BufferedReader(new FileReader(filePathInfo)) ){
            String line;
            while ((line = br.readLine()) != null){
                String[] info = line.split(",");
                data.add(info);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        for(String[] row : data){
            if(row[1].equals(username)){
                System.out.println(Arrays.toString(row));
                row[3] = String.join(";",courses);
            }
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePathInfo))){
            bw.write(headers);
            bw.newLine();

            for(String[] row : data){
                bw.write(String.join(",",row));
                bw.newLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}

class Student {
    private String username;
    private String password;
    private Database database;
    
    public Student(String username, String password, Database database) {
        this.username = username;
        this.password = password;
        this.database = database;
    }
    
    public void showRegistered(String username){
        List<String> currentCourses = database.readCurrentCourses(username);
        System.out.println("Registered Courses: " + currentCourses);

    }

    public void addCourse(String username, String course){
        System.out.println(database.addCourse(username, course));
        showRegistered(username);
        

    }

    public void deleteCourse(String username, String course){
        System.out.println(database.removeCourse(username, course));
        showRegistered(username);
    }
}
