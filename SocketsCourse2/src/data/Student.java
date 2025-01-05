package data;

public class Student {
    public int id;
    private String firstName;
    private String lastName;
    private String school;
    private int semester;
    private int passedCourses;

    public Student(int id, String firstName, String lastName, String school, int semester, int passedCourses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.school = school;
        this.semester = semester;
        this.passedCourses = passedCourses;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSchool() {
        return school;
    }

    public int getSemester() {
        return semester;
    }

    public int getPassedCourses() {
        return passedCourses;
    }
}