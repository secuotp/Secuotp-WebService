/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model;

import java.util.ArrayList;

/**
 *
 * @author zenology
 */
public class XMLParameter {

    private ArrayList<String> keyList;
    private ArrayList<String> valueList;
    private int pointer = 0;

    public XMLParameter() {
        keyList = new ArrayList<>();
        valueList = new ArrayList<>();
    }

    /**
     * add Parameter Value
     *
     * @param key name of the Parameter
     * @param value value of the Parameter
     */
    public void add(String key, String value) {
        this.keyList.add(key);
        this.valueList.add(value);
    }

    public String[] pop() {
        try {
            String[] valueString = new String[2];
            valueString[0] = keyList.get(pointer);
            valueString[1] = valueList.get(pointer);
            pointer++;
            return valueString;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e.getCause());
        }
        return null;
    }
    
    public String[] peek(){
        try {
            String[] valueString = new String[2];
            valueString[0] = keyList.get(pointer);
            valueString[1] = valueList.get(pointer);
            return valueString;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e.getCause());
        }
        return null;
    }
    
    public boolean hasNext(){
        return pointer < keyList.size();
    }
    
    public void first(){
        pointer = 0;
    }
    
    public void last(){
        pointer = keyList.size();
    }
    
    public String getValue(String key){
        int p = 0;
        while(p < keyList.size()){
            if(keyList.get(p).equals(key)){
                return valueList.get(p);
            }else{
                p++;
            }
        }
        return null;
    }
    
    public void clear(){
        keyList = new ArrayList<>();
        valueList = new ArrayList<>();
        this.first();
    }
}
