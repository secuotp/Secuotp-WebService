/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.generate;

/**
 *
 * @author Zenology
 */
public class CirQueue {

    private static String[] data = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static int pointer = 0;

    public static int size() {
        return data.length;
    }

    public static String count(int num) {
        pointer = 0;
        int count = 0;
        while (count < num) {
            if (pointer != (data.length - 1)) {
                pointer++;
            } else {
                pointer = 0;
            }
            count++;
        }
        return data[pointer];
    }

}
