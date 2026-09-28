public class Admin extends Person {
    private int adminId;
    private String username;

    public Admin(int adminId, String username, String name, String phone, String email) {
        super(name, phone, email);
        this.adminId = adminId;
        this.username = username;
    }

    public int getAdminId() { return adminId; }
    public String getUsername() { return username; }

    @Override
    public void displayDetails() {
        System.out.println("\n========== ADMIN PROFILE ==========");
        System.out.println("Name     : " + getName());
        System.out.println("Admin ID : " + adminId);
        System.out.println("Username : " + username);
        System.out.println("Phone    : " + getPhone());
        System.out.println("Email    : " + getEmail());
    }
}
