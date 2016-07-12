package com.ten.utils;

/**
 * @author Nita Karande
 * This class will contain reusable helper methods for the entire application
 */
public class Utils {
	/**
	 * Returns true if the input string is null or empty
	 * @param string
	 * @return
	 */
	public static boolean isEmptyOrNull(String string){
		return ((null == string) || ("".equals(string.trim())));
	}
	
    /**
     * Returns true if the input string is an integer
     * @param string
     * @return
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
