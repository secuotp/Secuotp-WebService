/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.services;

import com.secuotp.model.Site;
import com.secuotp.model.EndUser;
import com.secuotp.model.connection.ConnectionAgent;
import com.secuotp.model.generate.TOTPPattern;
import com.secuotp.model.sms.SMSSender;
import com.secuotp.model.text.StringText;
import com.secuotp.model.xml.XMLCreate;
import com.secuotp.model.xml.XMLParser;
import com.secuotp.model.xml.XMLValidate;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.soap.SOAPException;
import org.xml.sax.SAXException;

/**
 *
 * @author zenology
 */
@Path("/otp")
public class OtpService {

    @POST
    @Path("/generate")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String generate(@FormParam("request") String xml) throws SAXException, IOException, ParseException, SOAPException, SQLException, ClassNotFoundException {
        File xmlFile = File.createTempFile("test", "xsd");
        FileWriter fw = new FileWriter(xmlFile);
        fw.write(StringText.GENERATE_OTP_XSD);
        fw.close();

        XMLValidate xmlVal = new XMLValidate(xmlFile.getAbsoluteFile());

        if (xmlVal.validate(xml, "G-01")) {
            XMLParser parse = new XMLParser(xml);
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);

            if (Site.authenService(domain, serial) && !Site.checkDisabled(domain)) {
                Site site = Site.getSite(domain);
                EndUser user = EndUser.getEndUser(parse.getDataFromTag("username", 0), domain);

                if (user == null) {
                    return XMLCreate.createResponseXML(301, "Generate One-Time Password", StringText.GENERATE_OTP_301_1).asXML();
                } else if (site == null) {
                    return XMLCreate.createResponseXML(301, "Generate One-Time Password", StringText.GENERATE_OTP_301_2).asXML();
                } else if (user.getMobileMode()) {
                    return XMLCreate.createResponseXML(302, "Generate One-Time Password", StringText.GENERATE_OTP_302).asXML();
                }

                //OTP Generator
                String[] totpAndRemaining = TOTPPattern.generateActualTOTP(5, user.getSerialNumber(), site.getSerialNumber(), site.getPatternName(), site.getLength(), site.getTimeZone());
                //End of OTP Generator

                String message = site.getSiteName() + "\nYour OTP: " + totpAndRemaining[0] + "\nPassword expired at\n" + totpAndRemaining[1];
                String response = SMSSender.sendSMS(user.getPhone(), message);

                if (response != null) {
                    Connection con = ConnectionAgent.getInstance();
                    CallableStatement cst = con.prepareCall("CALL add_site_sms_log(?)");
                    cst.setInt(1, Integer.parseInt(site.getSiteId()));
                    cst.execute();

                    return XMLCreate.createResponseXML(100, "Generate One-Time Password", StringText.GENERATE_OTP_100).asXML();
                } else {
                    return XMLCreate.createResponseXML(401, "Generate One-Time Password", StringText.GENERATE_OTP_401).asXML();
                }

            } else {
                return XMLCreate.createResponseXML(300, "Generate One-Time Password", StringText.GENERATE_OTP_300).asXML();
            }
        }

        return XMLCreate.createResponseXML(203, "Generate One-Time Password", StringText.ERROR_203).asXML();
    }

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String authenOTP(@FormParam("request") String xml) throws MalformedURLException, ClassNotFoundException, SQLException, IOException {
        File xmlFile = File.createTempFile("test", "xsd");
        FileWriter fw = new FileWriter(xmlFile);
        fw.write(StringText.AUTHENTICATE_OTP_XSD);
        fw.close();
        
        XMLValidate xmlVal = new XMLValidate(xmlFile.getAbsoluteFile());

        if (xmlVal.validate(xml, "A-01")) {
            XMLParser parse = new XMLParser(xml);
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);

            if (Site.authenService(domain, serial) && !Site.checkDisabled(domain)) {
                Site site = Site.getSite(domain);
                EndUser user = EndUser.getEndUser(parse.getDataFromTag("username", 0), domain);

                if (user == null) {
                    return XMLCreate.createResponseXML(301, "Generate One-Time Password", StringText.GENERATE_OTP_301_1).asXML();
                } else if (site == null) {
                    return XMLCreate.createResponseXML(301, "Generate One-Time Password", StringText.GENERATE_OTP_301_2).asXML();
                }

                String[] totpAndRemaining;
                if (!user.getMobileMode()) {
                    //OTP Generator
                    totpAndRemaining = TOTPPattern.generateActualTOTP(5, user.getSerialNumber(), site.getSerialNumber(), site.getPatternName(), site.getLength(), site.getTimeZone());

                    String password = parse.getDataFromTag("password", 0);
                    if (password.equalsIgnoreCase(totpAndRemaining[0])) {
                        executeProcedure(Integer.parseInt(site.getSiteId()));

                        return XMLCreate.createResponseXML(100, "Authenticate One-Time Password", StringText.AUTHENTICATE_OTP_100).asXML();
                    } else {
                        return XMLCreate.createResponseXML(303, "Authenticate One-Time Password", StringText.AUTHENTICATE_OTP_303).asXML();
                    }
                } else {
                    //Wait for iOS Swift Lang Complete to Debug This Section;
                    totpAndRemaining = TOTPPattern.generateActualTOTP(1, user.getSerialNumber(), site.getSerialNumber(), site.getPatternName(), site.getLength(), site.getTimeZone());

                    String password = parse.getDataFromTag("password", 0);
                    if (password.equalsIgnoreCase(totpAndRemaining[0])) {
                        executeProcedure(Integer.parseInt(site.getSiteId()));

                        return XMLCreate.createResponseXML(100, "Authenticate One-Time Password", StringText.AUTHENTICATE_OTP_100).asXML();
                    } else {
                        return XMLCreate.createResponseXML(303, "Authenticate One-Time Password", StringText.AUTHENTICATE_OTP_303).asXML();
                    }
                }
            }
        }
        return XMLCreate.createResponseXML(203, "Authenticate One-Time Password", StringText.ERROR_203).asXML();
    }

    private static void executeProcedure(int siteId) throws ClassNotFoundException, SQLException {
        String sql = "CALL add_site_request_log(?)";
        Connection con = ConnectionAgent.getInstance();
        CallableStatement cs = con.prepareCall(sql);
        cs.setInt(1, siteId);
        cs.execute();
    }
}
