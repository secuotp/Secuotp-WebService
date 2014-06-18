/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.services;

import com.secuotp.model.Site;
import com.secuotp.model.SiteUser;
import com.secuotp.model.text.StringText;
import com.secuotp.model.xml.XMLCreate;
import com.secuotp.model.xml.XMLParse;
import com.secuotp.model.xml.XMLValidate;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Zenology
 */
@Path("/manage")
public class Manage {

    @POST
    @Path("/register/end-user")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String registerEndUser(@FormParam("request") String xml) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException {
        XMLParse parse = new XMLParse(xml);
        
        if (parse.getAttibuteFromTag("service", "sid", 0).equals("M-01")) {
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);

            if (domain != null && serial != null) {
                if (Site.authenService(domain, serial)) {
                    SiteUser user = new SiteUser();
                    user.setUsername(parse.getDataFromTag("username", 0));
                    user.setEmail(parse.getDataFromTag("email", 0));
                    user.setFirstname(parse.getDataFromTag("fname", 0));
                    user.setLastname(parse.getDataFromTag("lname", 0));
                    user.setPhone(parse.getDataFromTag("phone", 0));

                    if (SiteUser.addSiteUser(user, domain, serial)) {
                        return XMLCreate.createResponseXML(100, "Register End-User", StringText.REGISTER_END_USER_100).asXML();
                    } else {
                        return XMLCreate.createResponseXML(200, "Register End-User", StringText.REGISTER_END_USER_200).asXML();
                    }
                } else {
                    return XMLCreate.createResponseXML(300, "Register End-User", StringText.REGISTER_END_USER_300).asXML();
                }
            }
        }
        return XMLCreate.createResponseXML(203, "Register End-User", StringText.ERROR_203).asXML();
    }

    @POST
    @Path("/disable/end-user")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String disableEndUser(@FormParam("request") String xml) {
        XMLParse parse = new XMLParse(xml);
        if (parse.getAttibuteFromTag("service", "sid", 0).equals("M-02")) {
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);
            if (domain != null && serial != null) {
                if (Site.authenService(domain, serial)) {
                    String username = parse.getDataFromTag("username", 0);
                    String removalCode = parse.getDataFromTag("code", 0);

                    if (SiteUser.disableEndUser(username, removalCode)) {
                        return XMLCreate.createResponseXML(100, "Disable End-User", StringText.DISABLE_END_USER_100).asXML();
                    } else {
                        return XMLCreate.createResponseXML(200, "Disable End-User", StringText.DISABLE_END_USER_200).asXML();
                    }
                } else {
                    return XMLCreate.createResponseXML(300, "Disable End-User", StringText.DISABLE_END_USER_300).asXML();
                }
            }
        }
        return XMLCreate.createResponseXML(203, "Disable End-User", StringText.ERROR_203).asXML();
    }

}
