package hardwaremaster.com.old.util;

import androidx.annotation.Nullable;

public class TextUtils {

    public static boolean validateEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validatePassword(String password) {
        return password.length() > 5;
    }

    public static boolean validateMatchPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public static boolean isEmpty(@Nullable String text) {
        return text == null || text.isEmpty();
    }

}
