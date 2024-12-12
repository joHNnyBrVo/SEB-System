package MainProgram;

public class Session {
    private static int adminID;
    private static int staffID;

    // Admin ID methods
    public static void setAdminID(int id) {
        adminID = id;
    }

    public static int getAdminID() {
        return adminID;
    }

    // Staff ID methods
    public static void setStaffID(int id) {
        staffID = id;
    }

    public static int getStaffID() {
        return staffID;
    }
}

