package Service;

public class SessionService {

    private static boolean loggedIn = false;
    private static String username = "";

    public static void login(String user) {
        loggedIn = true;
        username = user;
    }

    public static void logout() {
        loggedIn = false;
        username = "";
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static String getUsername() {
        return username;
    }
}
