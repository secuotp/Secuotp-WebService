/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.generate;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author zenology
 */
public class SerialNumber {

    public static String generateSerialNumber(String seed) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String sha1 = encrypt(seed, "SHA-512");
        int count = 0;
        int a = 0;
        int b = 4;
        String text = "";

        for (int i = 0; i < 23; i++) {
            if ((count + 1) % 6 != 0) {
                int seedNum = Integer.parseInt(sha1.substring(a, b), 16);
                text = text + SerialQueue.count(((int) (Math.round(Math.random() * 36) ^ seedNum) + seedNum));
            } else {
                text = text + "-";
            }
            count++;
            a += 5;
            b += 5;
        }
        return text;
    }

    public static String generateRemovalCode(String seed) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String sha1 = encrypt(seed, "MD5");
        int count = 0;
        int a = 0;
        int b = 2;
        String text = "";

        for (int i = 0; i < 10; i++) {
            int seedNum = Integer.parseInt(sha1.substring(a, b), 16);
            text = text + SerialQueue.count(((int)(Math.round(Math.random() * 36) * seedNum) - (seedNum)));

            a += 3;
            b += 3;
        }
        return text;
    }
    
    public static String generateMigrationCode(String seed) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        if(seed.length() < 16) {
            seed = "0" + seed;
        }
        String sha1 = encrypt(seed, "SHA-1").toUpperCase();
        String md5 = encrypt(seed, "MD5").toUpperCase();
        
        BigInteger sha1Long = new BigInteger(sha1, 16);
        BigInteger md5Long = new BigInteger(md5, 16);
        
        BigInteger xorLong = sha1Long.shiftRight(10).xor((md5Long.shiftRight(10))).shiftLeft(24);
        
        String data = encrypt(xorLong.toString(16), "SHA-1");
        String response = "";
        for(int i = 0; i < 12; i++){
            response += data.charAt(i);
        }
        return response.toUpperCase();
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
