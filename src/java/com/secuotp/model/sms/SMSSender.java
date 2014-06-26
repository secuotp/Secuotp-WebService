/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.sms;

import com.secuotp.model.xml.XMLCreate;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public static String sendSMS(String tel, String msg) throws IOException {
        SOAPMessage reply = null;
        try {
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

            reply = myCon.call(message, "http://cronos.kmutt.ac.th/smswebservice/send.asmx");
            myCon.close();
            return readMessage(reply);
        } catch (SOAPException e) {
            return null;
        }
        
    }

    public static String readMessage(SOAPMessage message) throws SOAPException, IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        message.writeTo(os);

        final String encoding = (String) message.getProperty(SOAPMessage.CHARACTER_SET_ENCODING);
        String responseXml;
        if (encoding == null) {
            responseXml = new String(os.toByteArray());
        } else {
            responseXml = new String(os.toByteArray(), encoding);
        }

        return responseXml;
    }
}
