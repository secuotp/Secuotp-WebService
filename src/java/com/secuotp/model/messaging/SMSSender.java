/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.messaging;

import com.nexmo.messaging.sdk.NexmoSmsClient;
import com.nexmo.messaging.sdk.SmsSubmissionResult;
import com.nexmo.messaging.sdk.messages.TextMessage;
import java.io.IOException;

/**
 *
 * @author zenology
 */
public class SMSSender {

    private static final String API_KEY = "82c900f7";
    private static final String SECRET = "090b6820";
    
    public static SmsSubmissionResult nexmoSMS(String from, String to, String msg) throws IOException{
        NexmoSmsClient client;
        try {
            client = new NexmoSmsClient(API_KEY, SECRET);
        } catch (Exception ex) {
            System.err.println("Failed to instanciate a Nexmo Client");
            ex.printStackTrace();
            throw new RuntimeException("Failed to instanciate a Nexmo Client");
        }
        
        TextMessage message = new TextMessage(from, to, msg);
        
        SmsSubmissionResult[] results = null;
        try {
            results = client.submitMessage(message);
        } catch (Exception e) {
            System.err.println("Failed to communicate with the Nexmo Client");
            e.printStackTrace();
            throw new RuntimeException("Failed to communicate with the Nexmo Client");
        }
        
        if(results.length > 0){
            return results[0];
        } else {
            return null;
        }
        
    }
}
