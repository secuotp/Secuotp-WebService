/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model.xml;

import com.secuotp.services.OtpService;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author zenology
 */
public class XMLValidate {

    private XMLCheck check;
    private URL schemaFile;

    public XMLValidate(URL schemaFile) {
        check = new XMLCheck();
        this.schemaFile = schemaFile;
    }

    public boolean validate(String xml, String serviceID) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);

            SchemaFactory schemaFactory
                    = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

            factory.setSchema(schemaFactory.newSchema(schemaFile));

            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new SimpleErrorHandler(check));
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            if (check.isPass()) {
                NodeList list = document.getElementsByTagName("service");
                Element e = (Element) list.item(0);
                String attr = e.getAttribute("sid");
                return attr.equals(serviceID);
            }
            return false;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLValidate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLValidate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLValidate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}

class SimpleErrorHandler implements ErrorHandler {

    private XMLCheck check;

    public SimpleErrorHandler(XMLCheck check) {
        this.check = check;
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        System.out.println("A");
        check.setPass(false);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        System.out.println("");
        check.setPass(false);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        System.out.println("C");
        check.setPass(false);
    }
}
