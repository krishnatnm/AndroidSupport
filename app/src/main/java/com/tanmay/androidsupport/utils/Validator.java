package com.tanmay.androidsupport.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TaNMay on 3/2/2016.
 */
public class Validator {

    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        if (password.length() >= 6) {
            return true;
        }
        return false;
    }

    public boolean isValidMobileNumber(String mobile_no) {
        return android.util.Patterns.PHONE.matcher(mobile_no).matches();
    }
}
