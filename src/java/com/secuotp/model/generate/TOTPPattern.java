/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.generate;

import com.secuotp.model.time.NTPTime;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

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
        ArrayList<Character> list = new ArrayList<>();
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

    public static String toSize(String totp, int size, String mode) {
        int length = totp.length();
        if (size < length) {
            totp = totp.substring(0, size);
        } else {
            String add = null;
            if (mode.equals("Text")) {
                add = "A";
            } else {
                add = "0";
            }

            for (int i = length; i < size; i++) {
                totp = add + totp;
            }
        }
        return totp;
    }

    public static String[] generateActualTOTP(int delayMin, int delaySec, String userSerial, String siteStrial, String pattern, int length, String timeZone) throws NoSuchAlgorithmException {
        Calendar c = NTPTime.reformatTime(NTPTime.getNTPCalendar(), 0, delayMin, delaySec);

        String totp = TOTP.getOTP(userSerial, siteStrial, c, 8);
        totp = TOTPPattern.toPattern(totp, pattern);
        totp = TOTPPattern.toSize(totp, length, pattern);

        Calendar remain = Calendar.getInstance();
        remain.setTimeInMillis(c.getTimeInMillis() + (5 * 1000 * 60));
        DateFormat df = new SimpleDateFormat("HH:mm zz");
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
    
        String[] returnString = new String[2];
        returnString[0] = totp;
        returnString[1] = df.format(remain.getTime());
        return returnString;
    }
}
