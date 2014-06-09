/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.services;

import com.secuotp.model.People;
import com.secuotp.model.Site;
import com.secuotp.model.SiteUser;
import com.secuotp.model.xml.XMLCreate;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    private String domain;
    private String serial;

    private Document doc;
    private NodeList list;

    @POST
    @Path("/end-user")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String registerEndUser(@FormParam("request") String xml) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(new InputSource(new StringReader(xml)));
        doc.normalize();

        list = doc.getElementsByTagName("service");
        Element e = (Element) list.item(0);

        if (list.getLength() > 0) {
            if (e.getAttribute("sid").equals("S01")) {
                getDomainSerial(list, "domain", "serial");

                if (domain != null && serial != null) {
                    if (Site.authenService(domain, serial)) {
                        String[] tagString = {"username", "email", "fname", "lname", "phone"};
                        SiteUser user = getXMLParameter(list, tagString);
                       
                    } else {
                        return XMLCreate.createXMLFailed(300, "Register End-User").asXML();
                    }
                }
            }
        }
        return XMLCreate.createXMLFailed(203, "Register End-User").asXML();
    }

    private void getDomainSerial(NodeList list, String domainTag, String serialTag) {
        list = doc.getElementsByTagName("domain");
        if (list.getLength() > 0) {
            domain = list.item(0).getTextContent();
        }

        list = doc.getElementsByTagName("serial");
        if (list.getLength() > 0) {
            serial = list.item(0).getTextContent();
        }
    }
    
    private SiteUser getXMLParameter(NodeList list, String[] tag){
        SiteUser user = new SiteUser();
        list = doc.getElementsByTagName(tag[0]);
        if(list.getLength() > 0){
            user.setUsername(list.item(0).getTextContent());
        }
        
        list = doc.getElementsByTagName(tag[1]);
        if(list.getLength() > 0){
            user.setEmail(list.item(0).getTextContent());
        }
        
        list = doc.getElementsByTagName(tag[2]);
        if(list.getLength() > 0){
            user.setFirstname(list.item(0).getTextContent());
        }
        
        list = doc.getElementsByTagName(tag[3]);
        if(list.getLength() > 0){
            user.setLastname(list.item(0).getTextContent());
        }
        
        list = doc.getElementsByTagName(tag[4]);
        if(list.getLength() > 0){
            user.setPhone(list.item(0).getTextContent());
        }
        
        return user;
    }
}
