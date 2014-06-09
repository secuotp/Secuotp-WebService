/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.generate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zenology
 */
public class SerialNumber {

    public static String generateSerialNumber(String seed) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String sha1 = encrypt(seed, "SHA-512");
        String sha2 = encrypt(sha1, "SHA-512");
        String sha3 = encrypt(sha1 + sha2, "SHA-512");
        String newSeed = sha1 + sha2 + sha3;

        List<Integer> li = new ArrayList();
        for (int i = 0; i < 16; i++) {
            int rand = (int) Math.round(Math.random() * 384);
            if (li.isEmpty()) {
                li.add(rand);
            } else {
                boolean test = true;
                for (int j = 0; j < li.size(); j++) {
                    if (li.get(j) == rand) {
                        test = false;
                    }
                }
                if (test == false) {
                    i--;
                } else {
                    li.add(rand);
                }
            }
        }

        char[] serial = new char[19];
        int count = 0;
        for (int i = 0; i < serial.length; i++) {
            if ((i+1) % 5 != 0) {
                serial[i] = newSeed.charAt(li.get(count++));
            } else {
                serial[i] = '-';
            }
        }
        return String.valueOf(serial).toUpperCase();
    }

    private static String encrypt(String seed, String method) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuilder hexString = new StringBuilder();
        MessageDigest stringDigest = MessageDigest.getInstance(method);
        byte[] buffer = stringDigest.digest(seed.getBytes("utf-8"));
        
        for (int i = 0; i < buffer.length; i++) {
            if ((0xff & buffer[i]) < 0x10) {
                hexString.append("0").append(Integer.toHexString((0xFF & buffer[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & buffer[i]));
            }
        }

        return hexString.toString();
    }
}
