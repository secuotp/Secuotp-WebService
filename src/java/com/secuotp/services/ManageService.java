/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.services;

import com.secuotp.model.Site;
import com.secuotp.model.EndUser;
import com.secuotp.model.text.StringText;
import com.secuotp.model.xml.XMLCreate;
import com.secuotp.model.xml.XMLParser;
import com.secuotp.model.xml.XMLValidate;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
public class ManageService {

    @POST
    @Path("/register/end-user")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String registerEndUser(@FormParam("request") String xml) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException {
        XMLValidate val = new XMLValidate(new URL(StringText.REGISTER_END_USER_XSD));
        if (val.validate(xml, "M-01")) {
            XMLParser parse = new XMLParser(xml);
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);

            if (Site.authenService(domain, serial) && !Site.checkDisabled(domain)) {
                EndUser user = createSiteUserXML(xml);
                if (EndUser.addEndUser(user, domain, serial)) {
                    return XMLCreate.createResponseXML(100, "Register End-User", StringText.REGISTER_END_USER_100).asXML();
                } else {
                    return XMLCreate.createResponseXML(200, "Register End-User", StringText.REGISTER_END_USER_200).asXML();
                }
            } else {
                return XMLCreate.createResponseXML(300, "Register End-User", StringText.REGISTER_END_USER_300).asXML();
            }
        }
        return XMLCreate.createResponseXML(203, "Register End-User", StringText.ERROR_203).asXML();
    }

    @POST
    @Path("/disable/end-user")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String disableEndUser(@FormParam("request") String xml) throws MalformedURLException, ClassNotFoundException, SQLException {
        XMLValidate val = new XMLValidate(new URL(StringText.DISABLE_END_USER_XSD));
        if (val.validate(xml, "M-02")) {
            XMLParser parse = new XMLParser(xml);
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);
            if (Site.authenService(domain, serial) && !Site.checkDisabled(domain)) {
                String username = parse.getDataFromTag("username", 0);
                String removalCode = parse.getDataFromTag("code", 0);

                if (EndUser.disableEndUser(username, removalCode)) {
                    return XMLCreate.createResponseXML(100, "Disable End-User", StringText.DISABLE_END_USER_100).asXML();
                } else {
                    return XMLCreate.createResponseXML(200, "Disable End-User", StringText.DISABLE_END_USER_200).asXML();
                }
            } else {
                return XMLCreate.createResponseXML(300, "Disable End-User", StringText.DISABLE_END_USER_300).asXML();
            }

        }
        return XMLCreate.createResponseXML(203, "Disable End-User", StringText.ERROR_203).asXML();
    }

    private EndUser createSiteUserXML(String xml) {
        XMLParser parse = new XMLParser(xml);
        EndUser siteUser = new EndUser();

        siteUser.setUsername(parse.getDataFromTag("username", 0));
        siteUser.setEmail(parse.getDataFromTag("email", 0));
        siteUser.setFirstname(parse.getDataFromTag("fname", 0));
        siteUser.setLastname(parse.getDataFromTag("lname", 0));
        siteUser.setPhone(parse.getDataFromTag("phone", 0));

        return siteUser;
    }
}
