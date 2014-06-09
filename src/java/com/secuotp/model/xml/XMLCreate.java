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
    public static Document createXMLFailed(int error, String service) {
        Document d = new DefaultDocument();
        if (error == 203) {
            Element root = d.addElement("secuotp").addAttribute("status", "" + error);
            root.addElement("service").addText(service);
            root.addElement("message").addText("Your XML Input Mismatch: Please check XML Request Body");
        } else if (error == 300) {
            Element root = d.addElement("secuotp").addAttribute("status", "" + error);
            root.addElement("service").addText(service);
            root.addElement("message").addText("Failed to Register End-User: Not Allowed to Register End-User");
        }
        d.normalize();
        return d;
    }
}