/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.services;

import com.nexmo.messaging.sdk.SmsSubmissionResult;
import com.secuotp.model.Site;
import com.secuotp.model.EndUser;
import com.secuotp.model.Migration;
import com.secuotp.model.XMLParameter;
import com.secuotp.model.connection.ConnectionAgent;
import com.secuotp.model.generate.SerialNumber;
import com.secuotp.model.generate.TOTPPattern;
import com.secuotp.model.messaging.SMSSender;
import com.secuotp.model.text.StringText;
import com.secuotp.model.xml.XMLCreate;
import com.secuotp.model.xml.XMLParser;
import com.secuotp.model.xml.XMLValidate;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
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

    public static final int NEXMO_SUCCESS = 0;

    @POST
    @Path("/generate")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String generate(@FormParam("request") String xml) throws SAXException, IOException, ParseException, SOAPException, SQLException, ClassNotFoundException, NoSuchAlgorithmException {
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
                String[] totpAndRemaining = TOTPPattern.generateActualTOTP(10, 0, user.getSerialNumber(), site.getSerialNumber(), site.getPatternName(), site.getLength(), site.getTimeZone());
                //End of OTP Generator

                String message = site.getSiteName() + "\nYour OTP: " + totpAndRemaining[0] + "\nPassword expired at\n" + totpAndRemaining[1];
                SmsSubmissionResult result = SMSSender.nexmoSMS("SecuOTP", user.getPhone(), message);

                if (result.getStatus() == NEXMO_SUCCESS) {
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
    public String authenOTP(@FormParam("request") String xml) throws MalformedURLException, ClassNotFoundException, SQLException, IOException, NoSuchAlgorithmException {
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
                    totpAndRemaining = TOTPPattern.generateActualTOTP(10, 0, user.getSerialNumber(), site.getSerialNumber(), site.getPatternName(), site.getLength(), site.getTimeZone());

                    String password = parse.getDataFromTag("password", 0);
                    if (password.equalsIgnoreCase(totpAndRemaining[0])) {
                        executeProcedure(Integer.parseInt(site.getSiteId()));

                        return XMLCreate.createResponseXML(100, "Authenticate One-Time Password", StringText.AUTHENTICATE_OTP_100).asXML();
                    } else {
                        return XMLCreate.createResponseXML(303, "Authenticate One-Time Password", StringText.AUTHENTICATE_OTP_303).asXML();
                    }
                } else {
                    //Wait for iOS Swift Lang Complete to Debug This Section;
                    totpAndRemaining = TOTPPattern.generateActualTOTP(0, 30, user.getSerialNumber(), site.getSerialNumber(), site.getPatternName(), site.getLength(), site.getTimeZone());

                    String password = parse.getDataFromTag("password", 0);
                    if (password.equalsIgnoreCase(totpAndRemaining[0])) {
                        executeProcedure(Integer.parseInt(site.getSiteId()));

                        XMLParameter x = new XMLParameter();
                        x.add("OTP", totpAndRemaining[0]);
                    
                        return XMLCreate.createResponseXML(100, "Authenticate One-Time Password", StringText.AUTHENTICATE_OTP_100).asXML();
                    } else {
                        return XMLCreate.createResponseXML(303, "Authenticate One-Time Password", StringText.AUTHENTICATE_OTP_303).asXML();
                    }
                }
            }
        }
        return XMLCreate.createResponseXML(203, "Authenticate One-Time Password", StringText.ERROR_203).asXML();
    }

    @POST
    @Path("/migrate")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String migrateService(@FormParam("request") String xml) throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException {
        File xmlFile = File.createTempFile("test", "xsd");
        FileWriter fw = new FileWriter(xmlFile);
        fw.write(StringText.MIGRATE_OTP_CHANNEL_XSD);
        fw.close();

        XMLValidate xmlVal = new XMLValidate(xmlFile.getAbsoluteFile());

        if (xmlVal.validate(xml, "O-01")) {
            XMLParser parse = new XMLParser(xml);
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);

            if (Site.authenService(domain, serial) && !Site.checkDisabled(domain)) {
                String username = parse.getDataFromTag("username", 0);

                Site site = Site.getSite(domain);
                EndUser user = EndUser.getEndUser(username, domain);

                if (user != null || site != null) {
                    if (!user.getMobileMode()) {
                        String migration = "";
                        do {
                            migration = SerialNumber.generateMigrationCode(serial + user.getSerialNumber() + "Secuotp");
                        } while (!Migration.setMigrationCode(site.getSiteId(), user.getSiteUserId(), migration));

                        XMLParameter param = new XMLParameter();
                        param.add("username", user.getUsername());
                        param.add("migration-code", migration);

                        return XMLCreate.createResponseXMLWithData("Migrate One-Time Password Channel", StringText.MIGRATE_OTP_CHANNEL_101, param).asXML();
                    } else {
                        if(EndUser.setEndUserSMSMode(user.getUsername(), false)) {
                            SmsSubmissionResult result = SMSSender.nexmoSMS("SecuOTP", user.getPhone(), site.getSiteName() + "\nYou Successfully Change OTP from Mobile Application to SMS");
                            if(result.getStatus() == NEXMO_SUCCESS) {
                                return XMLCreate.createResponseXML(100, "Migrate One-Time Password Channel", StringText.MIGRATE_OTP_CHANNEL_100).asXML();
                            }
                        }
                        return XMLCreate.createResponseXML(200, "Migrate One-Time Password Channel", StringText.MIGRATE_OTP_CHANNEL_200).asXML();
                    }
                }
                return XMLCreate.createResponseXML(301, "Migrate One-Time Password Channel", StringText.MIGRATE_OTP_CHANNEL_301).asXML();
            }
            return XMLCreate.createResponseXML(300, "Migrate One-Time Password Channel", StringText.MIGRATE_OTP_CHANNEL_300).asXML();
        }
        return XMLCreate.createResponseXML(203, "Migrate One-Time Password Channel", StringText.ERROR_203).asXML();
    }

    private static void executeProcedure(int siteId) throws ClassNotFoundException, SQLException {
        String sql = "CALL add_site_request_log(?)";
        Connection con = ConnectionAgent.getInstance();
        CallableStatement cs = con.prepareCall(sql);
        cs.setInt(1, siteId);
        cs.execute();
    }
}
