package com.secuotp.model.generate;

/**
 * Copyright (c) 2011 IETF Trust and the persons identified as authors of the
 * code. All rights reserved. Redistribution and use in source and binary forms,
 * with or without modification, is permitted pursuant to, and subject to the
 * license terms contained in, the Simplified BSD License set forth in Section
 * 4.c of the IETF Trust’s Legal Provisions Relating to IETF Documents
 * (http://trustee.ietf.org/license-info).
 */
import java.io.UnsupportedEncodingException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an example implementation of the OATH TOTP algorithm. Visit
 * www.openauthentication.org for more information.
 *
 * @author Johan Rydell, PortWise, Inc.
 */
public class TOTP {
 private static String hmacSHA1(String key, String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if(key.length() > 64){
            key = encryptData(key, "SHA-1");
        }else if(key.length() < 64) {
            key = key + (0x00 * (64 - key.length()));
        }
        long oKeyPad = (0x5c * 64) ^ Long.parseLong(key);
        long iKeyPad = (0x36 * 64) ^ Long.parseLong(key);
        
        String oKeyString = Long.toHexString(oKeyPad).toUpperCase();
        String iKeyString = Long.toHexString(iKeyPad).toUpperCase();
        
        return encryptData(oKeyString + encryptData(iKeyString + message, "SHA-1"), "SHA-1");
        
    }
    
    private static String encryptData(String seed, String method) throws NoSuchAlgorithmException, UnsupportedEncodingException {
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
    
    public static String generateTOTP(String message, String time, int digits) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        int[] power = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
        String result = null;
        
        while(time.length() < 16){
            time = "0" + time;
        }
        
        String hmac = hmacSHA1(time, message);
        byte[] hmacByte = hmac.getBytes();
        int offset = hmacByte[hmacByte.length - 1] & 0xf;
        
        int binA = (hmacByte[offset] & 0x7f) << 24;
        int binB = (hmacByte[offset + 1] & 0xff) << 16;
        int binC = (hmacByte[offset + 2] & 0xff) << 8;
        int binD = hmacByte[offset + 3] & 0xff;
        
        int binary = binA | binB | binC | binD;
        String otp = "" + binary % power[digits];
        result = otp;
        
        while(result.length() < digits) {
            result = "0" + result;
        }
        
        return result;
    }

    private static String toHex(String arg) throws UnsupportedEncodingException {
        return String.format("%040x", new BigInteger(1, arg.getBytes("utf-8")));
    }

    public static String getOTP(String userSerial, String siteSerial, Calendar time, int digit) throws NoSuchAlgorithmException {
        try {
            // if userSerial = A && siteSerial = B && hmac(userSerial siteSerial SecuOTP) = C
            // then key = A-B-C
            String key = toHex(userSerial + "-" + siteSerial + "-" + TOTP.hmacSHA1(userSerial + " " + siteSerial + " " +"SecuOTP", "SecuOTP"));
            String otp = generateTOTP(key, "" + time.getTimeInMillis(), digit);

            return otp;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TOTP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
