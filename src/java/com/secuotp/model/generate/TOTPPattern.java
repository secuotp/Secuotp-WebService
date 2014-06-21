/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.generate;

import java.util.ArrayList;

/**
 *
 * @author zenology
 */
public class TOTPPattern {

    public static String toPattern(String totp, String pattern) {
        if (pattern.equalsIgnoreCase("Numeric")) {
            return totp;
        } else if (pattern.equalsIgnoreCase("Hexadecimal")) {
            return patternHex(totp);
        } else if (pattern.equalsIgnoreCase("Text")) {
            return patternText(totp);
        } else if (pattern.equalsIgnoreCase("Text + Numeric")) {
            return patternTextNum(totp);
        }
        return null;
    }

    private static String patternHex(String totp) {
        char[] patternText = new char[8];
        ArrayList<Character> list = getList(0);

        char[] hexChar = randPattern(totp);

        for (int i = 0; i < 8; i++) {
            int pointer = Character.codePointAt(hexChar, i);
            pointer = pointer % 16;
            patternText[i] = list.get(pointer);
        }

        return String.valueOf(patternText).toUpperCase();
    }

    private static String patternText(String totp) {
        char[] patternText = new char[8];
        ArrayList<Character> list = getList(1);

        char[] textChar = randPattern(totp);

        for (int i = 0; i < 8; i++) {
            int pointer = Character.codePointAt(textChar, i);
            pointer = pointer % 26;
            patternText[i] = list.get(pointer);
        }

        return String.valueOf(patternText).toUpperCase();
    }

    private static String patternTextNum(String totp) {
        char[] patternText = new char[8];
        ArrayList<Character> list = getList(2);

        char[] textNumChar = randPattern(totp);

        for (int i = 0; i < 8; i++) {
            int pointer = Character.codePointAt(textNumChar, i);
            pointer = pointer % 36;
            patternText[i] = list.get(pointer);
        }

        return String.valueOf(patternText).toUpperCase();
    }

    private static char[] randPattern(String totp) {
        if (totp.length() > 8) {
            totp = totp.substring(0, 8);
        } else if (totp.length() < 8) {
            for (int i = totp.length(); i < 8; i++) {
                totp = "0" + totp;
            }
        }

        char[] list = totp.toCharArray();
        if (totp.length() == 8) {
            for (int i = 0; i < list.length; i++) {
                if (i == 0) {
                    list[i] = (char) ((int) Math.floor(Integer.parseInt("" + list[i]) * 50 / 3));
                } else {
                    list[i] = (char) (((int) Math.floor(Integer.parseInt("" + list[i]) * 50 / 3)) + Character.codePointAt(list, i - 1));
                }
            }
        }

        return list;

    }

    private static ArrayList<Character> getList(int mode) {
        ArrayList<Character> list = new ArrayList<Character>();
        if (mode == 0) {
            for (int i = 0; i < 16; i++) {
                String hex = Integer.toHexString(i);
                list.add(hex.charAt(0));
            }

            return list;
        } else if (mode == 1) {
            for (int i = 0; i < 26; i++) {
                int ascii = i + 65;
                list.add((char) ascii);
            }

            return list;
        } else if (mode == 2) {
            int pointer = 0;
            for (int i = 0; i < 36; i++) {
                int ascii;
                if (i < 26) {
                    ascii = i + 65;
                } else {
                    ascii = pointer++ + 48;
                }
                list.add((char) ascii);
            }

            return list;
        }
        return null;
    }
}
