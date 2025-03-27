# Java-Final-Project
private void initializeCourses() {
        String[] initialCourses = {"Foundation of Computing", "Applied Environmental Science", "Arts and Culture", "Java Programming I", "Maths for Computing"};
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COURSE_FILE))) {
            for (String course : initialCourses) {
                writer.write(course + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
public List<String> readCurrentCourses(String username) {
        List<String> courses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", 3); // Ensure we split only into 3 parts
                if (data[0].equals(username)) {
                    if (data.length > 2 && !data[2].isEmpty()) {
                        courses = new ArrayList<>(Arrays.asList(data[2].split(";")));
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courses;
    }
 public String addCourse(String username, String course) {
        List<String> courses = readCurrentCourses(username);
        if (courses.contains(course)) {
            return "Course already registered.";
        }
        courses.add(course);
        updateStudentCourses(username, courses);
        return "Course added successfully.";
    }
