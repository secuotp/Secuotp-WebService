/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.xml;

import com.secuotp.model.XMLParameter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocument;

/**
 *
 * @author zenology
 */
public class XMLCreate {

    public static Document createResponseXML(int error, String service, String message) {
        Document d = new DefaultDocument();

        Element root = d.addElement("secuotp").addAttribute("status", "" + error);
        root.addElement("service").addText(service);
        root.addElement("message").addText(message);

        d.normalize();
        return d;
    }

    public static Document createResponseXMLWithData(String service, String message, XMLParameter param) {
        Document d = new DefaultDocument();

        Element root = d.addElement("secuotp").addAttribute("status", "101");
        root.addElement("service").addText(service);
        root.addElement("message").addText(message);
        Element responseNode = root.addElement("response");
        while (param.hasNext()) {
            String[] parameter = param.pop();
            responseNode.addElement(parameter[0]).setText(parameter[1]);
        }
        
        d.normalize();
        return d;
    }
}
