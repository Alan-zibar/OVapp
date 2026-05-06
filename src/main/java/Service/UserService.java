package Service;

import java.util.HashMap;
import java.util.Map;
import model.User;


public class UserService {

    private static final Map<String, User> usersByUsername = new HashMap<>();

    static {
        usersByUsername.put("admin", new User("Administrator", "admin", "admin@towhere.nl", "1234"));
    }

    public static boolean usernameExists(String username) {
        return usersByUsername.containsKey(username);
    }

    public static void registerUser(String fullName, String username, String email, String password) {
        User user = new User(fullName, username, email, password);
        usersByUsername.put(username, user);
    }

    public static boolean login(String username, String password) {
        User user = usersByUsername.get(username);

        if (user == null) {
            return false;
        }

        return user.getPassword().equals(password);
    }
}
