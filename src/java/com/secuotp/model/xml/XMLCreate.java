/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.xml;

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
}
