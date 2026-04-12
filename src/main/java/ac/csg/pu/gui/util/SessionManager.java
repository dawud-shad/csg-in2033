package ac.csg.pu.gui.util;

public class SessionManager {

    private static String currentUserEmail;
    private static String currentUserType;

    public static void login(String email, String userType) {
        currentUserEmail = email;
        currentUserType = userType;
    }

    public static void logout() {
        currentUserEmail = null;
        currentUserType = null;
    }

    public static String getCurrentUserEmail() { return currentUserEmail; }
    public static String getCurrentUserType() { return currentUserType; }

    public static boolean isLoggedIn() { return currentUserEmail != null; }
}