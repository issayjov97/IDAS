/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author dzhohar
 */
import java.security.SecureRandom;

public class PasswordGenerator {

    private static SecureRandom random = new SecureRandom();

    /**
     * different dictionaries used
     */
    public static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    public static final String NUMERIC = "0123456789";
    public static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";

    /**
     * Method will generate random string based on the parameters
     *
     * @param len the length of the random string
     * @param dic the dictionary used to generate the password
     * @return the random password
     */
    public static String generatePassword(int len, String dic) {
        String result = "";
        for (int i = 0; i < (len/2); i++) {
            int index = random.nextInt(dic.length());
            result += dic.charAt(index);
            index = random.nextInt(NUMERIC.length());
            result += NUMERIC.charAt(index);
        }
        return result;
    }

}