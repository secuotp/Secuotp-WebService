/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.generate;

/**
 *
 * @author zenology
 */
public class TOTPPattern {

    static char[] text = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    String[] textNum = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    static char[] hexText = {'F', '0', '1', 'D', '2', '3', 'A', '4', '5', 'E', '6', '7', 'B', '8', '9', 'C'};

    public static String toPattern(String totp, String pattern) {
        char[] patternText = null;
        if (pattern.equalsIgnoreCase("Numeric")) {
            return totp;
        } else if (pattern.equalsIgnoreCase("Hexadecimal")) {
            patternText = new char[8];
            String hex = totp;
            
            if (hex.length() > 8) {
                hex = hex.substring(0, 8);
            } else if (hex.length() < 8) {
                for (int i = hex.length(); i < 8; i++) {
                    hex = "0" + hex;
                }
            }
            
            char[] hexChar = totp.toCharArray();
            for(int i = 0; i< hex.length();i++){
                hexChar[i] = (char) (36 + ((int)Math.ceil(Integer.parseInt("" + hexChar[i]) * 50 / 3)));
            }
            
            for (int i = 0; i < hex.length(); i++) {
                patternText[i] = hexText[Integer.parseInt(hex.substring(i, i + 1))];
            }
            return String.valueOf(patternText).toUpperCase();

        } else if (pattern.equalsIgnoreCase("Text")) {
            patternText = new char[8];
            String hex = totp;
            
            if (hex.length() > 8) {
                hex = hex.substring(0, 8);
            } else if (hex.length() < 8) {
                for (int i = hex.length(); i < 8; i++) {
                    hex = "0" + hex;
                }
            }
            
            char[] hexChar = totp.toCharArray();
            int pointer = 0;
           
            for(int i = 0; i < (Integer.parseInt("" +hexChar[0]) * 50) ;i++){
                pointer++;
                if(pointer > text.length){
                    pointer = 0;
                }
            }
            hexChar[0] = text[pointer];
            
            hexChar[1] = text[((int)Math.ceil(Integer.parseInt("" +hexChar[1]) * 300 / 41))];
            hexChar[2] = text[((int)Math.floor(Integer.parseInt("" +hexChar[2]) * 100 / 3))];
            hexChar[3] = text[((int)Math.floor(Integer.parseInt("" +hexChar[3]) * 231 / 47))];
            hexChar[4] = text[((int)Math.ceil(Integer.parseInt("" +hexChar[4]) * 87 / 13))];
            hexChar[5] = text[((int)Math.ceil(Integer.parseInt("" +hexChar[5]) * 12 / ((int)Math.ceil(Integer.parseInt("" + hexChar[4])))))];
            hexChar[6] = text[((int)Math.ceil(Integer.parseInt("" +hexChar[5]) * 70 / ((int)Math.ceil(Integer.parseInt("" + hexChar[6])))))];
            hexChar[7] = text[((int)Math.ceil(Integer.parseInt("" +hexChar[7]) * 45 / ((int)Math.ceil(Integer.parseInt("" + hexChar[6])))))];
            
            return String.copyValueOf(hexChar);
        } else if (pattern.equalsIgnoreCase("Text + Numeric")) {

        }
        return String.valueOf(patternText);
    }

    public static void main(String[] args) {
        System.out.println(TOTPPattern.toPattern("4561887017", "Text"));
    }
}
