package ewision.sahan.utils;

/**
 *
 * @author Sahan
 */
public class Validation {

    public static boolean mobile(String mobile) {
        return !mobile.matches("^07[01245678]{1}[0-9]{7}$");
    }

    public static boolean email(String email) {
        return !email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    }

    public static boolean password(String password) {
        if (password.length() < 5 || password.length() > 10) {
            return true;
        }
        return false;
    }
}
