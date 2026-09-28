public class Student extends Person {
    private String rollNo;
    private String branch;
    private int year;
    private String password;

    public Student(String rollNo, String name, String phone, String email,
                   String branch, int year, String password) {
        super(name, phone, email);
        this.rollNo = rollNo;
        this.branch = branch;
        this.year = year;
        this.password = password;
    }

    public String getRollNo() { return rollNo; }
    public String getBranch() { return branch; }
    public int getYear() { return year; }
    public String getPassword() { return password; }

    public void setNameValue(String name) { setName(name); }
    public void setPhoneValue(String phone) { setPhone(phone); }
    public void setEmailValue(String email) { setEmail(email); }
    public void setBranch(String branch) { this.branch = branch; }

    @Override
    public void displayDetails() {
        System.out.println("\n========== STUDENT PROFILE ==========");
        System.out.println("Roll Number : " + rollNo);
        System.out.println("Name        : " + getName());
        System.out.println("Phone       : " + getPhone());
        System.out.println("Email       : " + getEmail());
        System.out.println("Branch      : " + branch);
        System.out.println("Year        : " + year);
    }
}
