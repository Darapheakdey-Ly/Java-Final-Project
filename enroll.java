
public class enroll {
    public static void main(String[] args) {
        Database db = new Database("filepath");
        String[] allCourses = db.readAllCourses();

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
    private String filePath;
    
    public Database(String filePath) {
        this.filePath = filePath;
    }
    
    public Student addUser(String username, String password) {
        // 
        return new Student(username,password, this);
    }
    
    public Student login(String username, String password) {
        // 
        return new Student(username,password, this);
    }
    
    public String[] readCurrentCourses(String username) {
        // 
        return new String[] {"temp"};
    }
    
    public String addCourse(String username, String course){
        //
        return "success";
    }

    public String removeCourse(String username, String course){
        //
        return "success or failure";
    }

    public String[] readAllCourses(){
        //
        return new String[] {"temp"};
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
        String[] currentCourses = database.readCurrentCourses(username);
        //

    }

    public void addCourse(String username, String course){
        database.addCourse(username, course);
        //
        showRegistered(username);
        

    }

    public void deleteCourse(String username, String course){
        database.removeCourse(username, course);
        //
        showRegistered(username);
    }

    public void registration(String username, String course){
        //display all courses
        //
        addCourse(username,course);
    }

    public void drop(String username, String course){
        showRegistered(username);
        //
        deleteCourse(username, course);
    }
}