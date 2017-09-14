package mx.dev.francoandroidev.itunesdiscography.Utilities;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by franco on 21/06/17.
 * Helper class containing some useful
 * static methods for validating strings
 */

public final class StringUtilities {
    /**
     *
     * @param str
     * @return true if string is empty, false otherwise
     */
    public static boolean isFieldEmpty(String str) {
        return str.isEmpty();
    }

    /**
     *
     * @param str
     * @return true if has at least an uppercase letter, false otherwise
     */
    public static boolean hasUpperCase(String str){
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     *
     * @param str
     * @return true if has blank space, false otherwise
     */
    public static boolean hasBlankSpace(String str){
        Pattern pattern = Pattern.compile("[\\s]");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     *
     * @param str
     * @return true string contains another character distinct than letter or numbers, false otherwise
     */
    public static boolean hasNotAllowedCharacters(String str){
        Pattern pattern = Pattern.compile("[^\\w]");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     *
     * @param str
     * @return true if string is too long, false otherwise
     */
    public static boolean isTooShort(String str){
        return str.length() < 8;
    }

    /**
     * Verify if string has numbers and letters
     * @param password
     * @return
     */

    public static boolean isAlphanumeric(String password) {
    boolean hasDigits = false;
    boolean hasLetters = false;
    int length = password.length();
        for(int l = 0 ; l < length ; l++){
            if(Character.isDigit(password.charAt(l))){
                hasDigits = true;
            }
        }

        for(int l = 0 ; l < length ; l++){
            if(Character.isLetter(password.charAt(l))){
                hasLetters = true;
            }
        }

        Log.d("hasDigits, hasLetters",hasDigits + " , " + hasLetters + " , " +(hasDigits && hasLetters));

        return hasDigits && hasLetters;
    }
}