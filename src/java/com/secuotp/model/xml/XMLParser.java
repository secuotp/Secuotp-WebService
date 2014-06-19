/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Zenology
 */
public class XMLParser {

    private Document doc;
    private NodeList list;
    private Element e;

    public XMLParser(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDataFromTag(String tagName, int numberItem) {
        list = doc.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            Node n = list.item(numberItem);
            return n.getTextContent();
        } else {
            return null;
        }
    }

    public String getAttibuteFromTag(String tagName, String attibuteName, int numberItem) {
        list = doc.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            e = (Element) list.item(numberItem);
            return e.getAttribute(attibuteName);
        }else{
            return null;
        }
            

    }
}
