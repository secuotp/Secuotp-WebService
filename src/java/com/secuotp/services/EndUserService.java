/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.services;

import com.secuotp.model.Site;
import com.secuotp.model.EndUser;
import com.secuotp.model.XMLParameter;
import com.secuotp.model.connection.ConnectionAgent;
import com.secuotp.model.text.StringText;
import com.secuotp.model.xml.XMLCreate;
import com.secuotp.model.xml.XMLParser;
import com.secuotp.model.xml.XMLValidate;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author zenology
 */
@Path("/user")
public class EndUserService {

    @POST
    @Path("/end-user")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String getEndUser(@FormParam("request") String xml) throws MalformedURLException, ClassNotFoundException, SQLException, IOException {
        File xmlFile = File.createTempFile("test", "xsd");
        FileWriter fw = new FileWriter(xmlFile);
        fw.write(StringText.GET_END_USER_DATA_XSD);
        fw.close();
        
        XMLValidate val = new XMLValidate(xmlFile.getAbsoluteFile());
        if (val.validate(xml, "U-01")) {
            XMLParser parse = new XMLParser(xml);
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);

            if (Site.authenService(domain, serial) && !Site.checkDisabled(domain)) {
                EndUser user = EndUser.getEndUser(parse.getDataFromTag("username", 0), domain);

                if (user == null) {
                    return XMLCreate.createResponseXML(301, "Get End-User Data", StringText.GET_END_USER_DATA_301).asXML();
                }

                XMLParameter param = new XMLParameter();
                param.add("username", user.getUsername());
                param.add("email", user.getEmail());
                param.add("fname", user.getFirstname());
                param.add("lname", user.getLastname());
                param.add("phone", user.getPhone());
                if (parse.getDataFromTag("type", 0).equals("full")) {
                    param.add("serial", user.getSerialNumber());
                    param.add("removal", user.getRemovalCode());
                    param.add("mobile", "" + user.getMobileMode());
                }

                return XMLCreate.createResponseXMLWithData("Get End-User Data", StringText.GET_END_USER_DATA_101, param).asXML();
            }
            return XMLCreate.createResponseXML(300, "Get End-User Data", StringText.GET_END_USER_DATA_300).asXML();
        }
        return XMLCreate.createResponseXML(203, "Get End-User Data", StringText.ERROR_203).asXML();
    }

    @PUT
    @Path("/end-user")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String setEndUser(@FormParam("request") String xml) throws MalformedURLException, ClassNotFoundException, SQLException, IOException {
        File xmlFile = File.createTempFile("test", "xsd");
        FileWriter fw = new FileWriter(xmlFile);
        fw.write(StringText.SET_END_USER_DATA_XSD);
        fw.close();
        
        XMLValidate val = new XMLValidate(xmlFile.getAbsoluteFile());
        if (val.validate(xml, "U-02")) {
            XMLParser parse = new XMLParser(xml);
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);

            if (Site.authenService(domain, serial) && !Site.checkDisabled(domain)) {
                String username = parse.getDataFromTag("username", 0);
                EndUser user = EndUser.getEndUser(username, domain);
                if (user != null) {
                    Connection con = ConnectionAgent.getInstance();
                    PreparedStatement ps;
                    String[] updateParam = new String[5];
                    for (int i = 0; i < parse.getNumberItem("change"); i++) {
                        String param = parse.getNodeFromTag("change").item(i).getChildNodes().item(0).getTextContent();
                        String value = parse.getNodeFromTag("change").item(i).getChildNodes().item(1).getTextContent();
                        switch (param) {
                            case "username":
                                updateParam[0] = value;
                                break;
                            case "email":
                                updateParam[1] = value;
                                break;
                            case "fname":
                                updateParam[2] = value;
                                break;
                            case "lname":
                                updateParam[3] = value;
                                break;
                            case "phone":
                                updateParam[4] = value;
                                break;
                        }
                    }
                    EndUser.updateEndUser(user.getUsername(), Integer.parseInt(user.getSiteId()), updateParam);
                    return XMLCreate.createResponseXML(100, "Set End-User Data", StringText.SET_END_USER_DATA_100).asXML();
                } else {
                    return XMLCreate.createResponseXML(100, "Set End-User Data", StringText.SET_END_USER_DATA_301).asXML();
                }
            }
            return XMLCreate.createResponseXML(300, "Set End-User Data", StringText.SET_END_USER_DATA_300).asXML();
        }

        return XMLCreate.createResponseXML(203, "Set End-User Data", StringText.ERROR_203).asXML();
    }
    
}
