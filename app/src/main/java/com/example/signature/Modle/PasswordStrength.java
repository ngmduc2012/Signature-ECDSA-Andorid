package com.example.signature.Modle;

import android.graphics.Color;

import com.example.signature.R;

public enum PasswordStrength {

    // we use some color in green tint =>
    //more secure is the password, more darker is the color associated
    WEAK(R.string.weak, Color.parseColor("#fb0101")),
    MEDIUM(R.string.medium, Color.parseColor("#ff8080")),
    STRONG(R.string.strong, Color.parseColor("#ffbb33")),
    VERY_STRONG(R.string.very_strong, Color.parseColor("#99cc00"));

    private static int MIN_LENGTH = 8;
    private static int MAX_LENGTH = 15;
    public int msg;
    public int color;

    PasswordStrength(int msg, int color) {
        this.msg = msg;
        this.color = color;
    }

    public static PasswordStrength calculate(String password) {
        int score = 0;
        // boolean indicating if password has an upper case
        boolean upper = false;
        // boolean indicating if password has a lower case
        boolean lower = false;
        // boolean indicating if password has at least one digit
        boolean digit = false;
        // boolean indicating if password has a leat one special char
        boolean specialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (!specialChar && !Character.isLetterOrDigit(c)) {
                score++;
                specialChar = true;
            } else {
                if (!digit && Character.isDigit(c)) {
                    score++;
                    digit = true;
                } else {
                    if (!upper || !lower) {
                        if (Character.isUpperCase(c)) {
                            upper = true;
                        } else {
                            lower = true;
                        }

                        if (upper && lower) {
                            score++;
                        }
                    }
                }
            }
        }

        int length = password.length();

        if (length > MAX_LENGTH) {
            score++;
        } else if (length < MIN_LENGTH) {
            score = 0;
        }

        // return enum following the score
        switch (score) {
            case 0:
                return WEAK;
            case 1:
                return MEDIUM;
            case 2:
                return STRONG;
            case 3:
                return VERY_STRONG;
            default:
        }

        return VERY_STRONG;
    }
}
