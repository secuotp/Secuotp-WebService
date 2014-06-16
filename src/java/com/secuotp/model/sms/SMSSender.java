/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.secuotp.model.sms;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 *
 * @author zenology
 */
public class SMSSender {
    private static final String KEY = "dnLjIPco1OCeOfnGFjI5dgLj8vvrhW";
    
    public static SOAPMessage sendSMS(String tel, String msg) throws SOAPException{
        SOAPConnectionFactory myFct = SOAPConnectionFactory.newInstance();
        SOAPConnection myCon = myFct.createConnection();

        MessageFactory myMsgFct = MessageFactory.newInstance();
        SOAPMessage message = myMsgFct.createMessage();

        SOAPPart mySPart = message.getSOAPPart();
        SOAPEnvelope myEnvp = mySPart.getEnvelope();
        
        SOAPBody body = myEnvp.getBody();
        
        Name myContent = myEnvp.createName("SendSMS", null, "http://www.kmutt.ac.th/");
        SOAPBodyElement mySymbol = body.addBodyElement(myContent);
        
        Name keyName = myEnvp.createName("key");
        SOAPElement keyElement = mySymbol.addChildElement(keyName);
        keyElement.addTextNode(KEY);
        
        Name mobileName = myEnvp.createName("mobileNumber");
        SOAPElement mobileElement = mySymbol.addChildElement(mobileName);
        mobileElement.addTextNode(tel);
        
        Name textName = myEnvp.createName("textMessage");
        SOAPElement textElement = mySymbol.addChildElement(textName);
        textElement.addTextNode(msg);
        
        message.saveChanges();
        
        SOAPMessage reply = myCon.call(message, "http://cronos.kmutt.ac.th/smswebservice/send.asmx");
        myCon.close();
        return reply;
    }
}
