
import java.io.*;
import java.util.*;



public class enroll {
    //reads all courses in the courses file
    public static List<String> readAllCourses(){
        try (BufferedReader br = new BufferedReader(new FileReader("courses.csv"))) {
            return Arrays.asList(br.readLine().split(","));
        } catch (IOException e) {
            return Arrays.asList(new String[] {"Error: " + e.getMessage()}); 
        }  
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
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

        List<String> allCourses = readAllCourses();

        
        Student student = null;

        OUTER: 
        while (true) {
            System.out.println("===== Welcome to the Course Enrollment System =====");
            System.out.println("1. Register\n2. Log In\n3. Quit\nPlease choose an option");
            int choice = input.nextInt();
            input.nextLine();
            switch (choice) {
                case 1:
                    {
                        // Register
                        System.out.print("Enter your Full Name: ");
                        String fullname = input.nextLine();
                        System.out.print("Enter your username: ");
                        String username = input.nextLine();
                        System.out.print("Enter your password: ");
                        String password = input.nextLine();
                        student = db.addUser(username, password,fullname);
                        System.out.println("Registration successful!");
                        System.out.print("Press ENTER to Continue...");
                        input.nextLine();
                        break OUTER;
                    }
                case 2:
                    {
                        // Log In
                        System.out.print("Enter your username: ");
                        String username = input.nextLine();
                        System.out.print("Enter your password: ");
                        String password = input.nextLine();
                        student = db.login(username, password);
                        if (!student.getUsername().equals("Invalid")) {
                            System.out.println("Login successful!");
                            break OUTER; // Exit loop if login is successful
                        } else {
                            System.out.println("Invalid username or password. Please try again.");
                            System.out.print("Press ENTER to Continue...");
                            input.nextLine();
                        }
                        break;
                    }
                case 3:
                    // Exit
                    System.out.println("Exiting program.");
                    input.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.print("Press ENTER to Continue...");
                    input.nextLine();
                    break;
            }
        }

        OUTER_1:
        while (true) { 
            System.out.println("\n===== Student Hub =====");
            System.out.println("1. View Available Courses\n2. View Registered Courses\n3. Add Course\n4. Remove Course\n5. Logout\nWhat would you like to do?");
            int hubChoice = input.nextInt();
            input.nextLine();
            switch (hubChoice) {
                case 1:
                    // View all courses
                    System.out.println("Available Courses: ");
                    int count = 0;
                    for (String course : allCourses) {
                        System.out.printf("%-50s", course);
                        count++;

                        if (count % 4 == 0) {
                            System.out.println();
                        }
                    }
                    System.out.print("Press ENTER to Continue...");
                    input.nextLine();
                    break;
                case 2:
                    // View registered courses
                    System.out.println("Your Current Courses Are:");
                    student.showRegistered();
                    System.out.print("Press ENTER to Continue...");
                    input.nextLine();
                    break;
                case 3:
                    {
                        // Register for a course

                        System.out.println("Available Courses: ");
                        count = 0;
                        for (String course : allCourses) {
                            System.out.printf("%-50s", course);
                            count++;
    
                            if (count % 4 == 0) {
                                System.out.println();
                            }
                        }
                        System.out.print("Enter course name to register: ");
                        String course = input.nextLine();
                        student.addCourse(course);
                        System.out.print("Press ENTER to Continue...");
                        input.nextLine();
                        break;
                    }
                case 4:
                    {
                        // Drop a course
                        System.out.print("Your current courses are ");
                        student.showRegistered();
                        System.out.print("Enter course name to drop: ");
                        String course = input.nextLine();
                        student.deleteCourse(course);
                        System.out.print("Press ENTER to Continue...");
                        input.nextLine();
                        break;
                    }
                case 5:
                    // Logout
                    System.out.println("Logging out");
                    break OUTER_1; // exits the menu loop
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        input.close();
    }
}

class Database {
    private String filePathLogin;
    private String filePathInfo;
    
    public Database(String[] filePath) {
        this.filePathLogin = filePath[0];
        this.filePathInfo = filePath[1];
    }
    
    public Student addUser(String username, String password ,String FullName) {
        Random random = new Random();
        int ID = 1000000 + random.nextInt(9000000);
        //writes the student information to the login database
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePathLogin,true))){
            bw.newLine();
            bw.write(ID + "," + username + "," + password);
        }
        catch (Exception e){
            System.err.println("ERROR: "+e.getMessage());
            //add error msg
            System.exit(0);
        }

        //write to studentinfo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePathInfo,true))){
            bw.newLine();
            bw.write(ID + "," + username + "," + FullName);
            Student student = new Student(username, password, this);
            return student;
        }
        catch (Exception e){
            System.err.println("ERROR: "+e.getMessage());
            System.exit(0);
        }
        return new Student(username,password, this);
    }
    
    public Student login(String username, String password) {
        try(BufferedReader br = new BufferedReader(new FileReader(filePathLogin))){
            String line;
            while((line = br.readLine())!=null){
                String values[] = line.split(",");
                if(values[1].equals(username)){
                    if(values[2].equals(password)){
                        return new Student(username, password, this);
                    }
                }
            }


        }
        catch(Exception e){
            System.err.println("ERROR: "+e.getMessage());
            System.exit(0);
        }
        return new Student("Invalid",password, this);
    }
    
    public List<String> readCurrentCourses(String username) {
        List<String> courses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathInfo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[1].equals(username)) {
                    if (data.length > 3 && !data[3].isEmpty()) {
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
    
    public String addCourses(String username, String course){
        List<String> courses = readCurrentCourses(username);
        try{
            if (courses.contains(course)) {
                return "Course already registered.";
            }}catch (Exception e){

            }
        courses.add(course);
        
        updateStudentCourses(username, courses);
        return "\nCourse added successfully.";
    }

    public String removeCourse(String username, String course){
        List<String> courses = readCurrentCourses(username);
    
        if (!courses.contains(course)) {
            return "Course not found in your registered courses.";
        }
    
        courses.remove(course);
        updateStudentCourses(username, courses);
        return "Course removed successfully.";
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

        for (int i = 0; i < data.size(); i++){
            String[] row = data.get(i);
            
            if(row[1].equals(username)){
                if(row.length > 3){
                    row[3] = String.join(";",courses);}
                else{
                    List<String> rowList = new ArrayList<>(Arrays.asList(row));
                    rowList.add(String.join(";", courses));
                    data.set(i, rowList.toArray(new String[0]));
                }
            }
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePathInfo))){

            for(int i = 0; i < data.size(); i++){
                bw.write(String.join(",",data.get(i)));
                if (i < data.size() - 1) {
                    bw.newLine();
                }  
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
    
    public String getUsername(){
        return username;
    }

    public Student(String username, String password, Database database) {
        this.username = username;
        this.password = password;
        this.database = database;
    }
    
    public void showRegistered(){
        List<String> currentCourses = database.readCurrentCourses(username);
        System.out.println("Registered Courses: " );
        int count = 0;
        for (String course : currentCourses) {
            System.out.printf("%-50s", course);
            count++;

            if (count % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println("");
        System.out.println("");
    }

    public void addCourse(String course){
        List<String> allcourse = enroll.readAllCourses();
        if(!allcourse.contains(course)){
            System.out.println("Course not available...");
        }
        else{
        System.out.println(database.addCourses(username, course));
        System.out.println("");
        showRegistered();}
        System.out.println("");
        

    }

    public void deleteCourse(String course){
        System.out.println(database.removeCourse(username, course));
        System.out.println("");
        System.out.println("Your current courses are: ");
        showRegistered();
        System.out.println("");
    }
}
