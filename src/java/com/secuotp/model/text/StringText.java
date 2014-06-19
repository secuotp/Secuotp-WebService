/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.text;

/**
 *
 * @author Zenology
 */
public class StringText {

    //======================================   ERROR   ===============================================
    public static final String ERROR_203 = "Your XML Input Mismatch: Please check XML Request Body";

    //=================================   REGISTER END USER   ========================================
    public static final String REGISTER_END_USER_XSD = "http://secuotp.sit.kmutt.ac.th/FilePost/File/Register%20End-User.xsd";
    public static final String REGISTER_END_USER_100 = "Register End-User Completed";
    public static final String REGISTER_END_USER_200 = "Failed to Register End-User: Unable Register End-User";
    public static final String REGISTER_END_USER_300 = "Failed to Register End-User: Not Allowed to Register End-User";

    //==================================   DISABLE END USER   =========================================
    public static final String DISABLE_END_USER_XSD = "http://secuotp.sit.kmutt.ac.th/FilePost/File/Disable%20End-User.xsd";
    public static final String DISABLE_END_USER_100 = "Disable End-User Completed";
    public static final String DISABLE_END_USER_200 = "Failed to Disable End-User: Unable Disable End-User";
    public static final String DISABLE_END_USER_300 = "Failed to Disable End-User: Not Allowed to Disable End-User";

    //==================================   GENERATE OTP   =========================================
    public static final String GENERATE_OTP_XSD = "http://secuotp.sit.kmutt.ac.th/FilePost/File/Generate%20One-Time%20Password.xsd";
    public static final String GENERATE_OTP_300 = "Failed to Generate One-Time Password: Not Allowed to Generate One-Time Password";

}
