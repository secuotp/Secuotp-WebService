/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.services;

import java.io.IOException;
import java.io.StringReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.dom4j.tree.DefaultDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Zenology
 */
@Path("/manage")
public class Manage {

    @POST
    @Path("/end-user")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String registerEndUser(@FormParam("request") String xml) throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document inputDoc = dBuilder.parse(new InputSource(new StringReader(xml)));
        inputDoc.normalize();

        NodeList list = inputDoc.getElementsByTagName("service");
        Element e = (Element) list.item(0);
        if (list.getLength() > 0) {
            if (e.getAttribute("sid").equals("S01")) {
                return null;
            }
        }
        return createXMLFailed(203).asXML();
    }

    private org.dom4j.Document createXMLFailed(int error) {
        org.dom4j.Document d = new DefaultDocument();
        if (error == 203) {
            org.dom4j.Element root = d.addElement("secuotp").addAttribute("status", "203");
            root.addElement("service").addText("Register End-User");
            root.addElement("message").addText("Your XML Input Sid Mismatch");
        }
        d.normalize();
        return d;
    }
}
