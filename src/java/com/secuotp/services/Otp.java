/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.services;

import com.secuotp.model.Site;
import com.secuotp.model.SiteUser;
import com.secuotp.model.text.StringText;
import com.secuotp.model.time.NtpClient;
import com.secuotp.model.time.NtpMessage;
import com.secuotp.model.xml.XMLCreate;
import com.secuotp.model.xml.XMLParser;
import com.secuotp.model.xml.XMLValidate;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.xml.sax.SAXException;

/**
 *
 * @author zenology
 */
@Path("/otp")
public class Otp {
    
    @POST
    @Path("/generate")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String generate(@FormParam("request") String xml) throws SAXException, IOException {        
        XMLValidate xmlVal = new XMLValidate(new URL(StringText.GENERATE_OTP_XSD));
        
        if(xmlVal.validate(xml, "G-01")){
            XMLParser parse = new XMLParser(xml);
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);
            
            if(Site.authenService(domain, serial)){
                Site site = Site.getSite(domain);
                SiteUser user = SiteUser.getSiteUser(parse.getDataFromTag("username", 0), domain);
                
                if(user == null){
                    return XMLCreate.createResponseXML(301, "Generate One-Time Password", StringText.GENERATE_OTP_301_1).asXML();
                } else if(site == null){
                    return XMLCreate.createResponseXML(301, "Generate One-Time Password", StringText.GENERATE_OTP_301_2).asXML();
                }else if(user.getMobileMode()){
                    return XMLCreate.createResponseXML(302, "Generate One-Time Password", StringText.GENERATE_OTP_302).asXML();
                }
                
                NtpMessage timeMsg = NtpClient.GetNTPTime();
                
                return "<time>"+ timeMsg.toString() +"</time>";
            }else{
                return XMLCreate.createResponseXML(300, "Generate One-Time Password", StringText.GENERATE_OTP_300).asXML();
            }
        }
        
        return XMLCreate.createResponseXML(203, "Generate One-Time Password", StringText.ERROR_203).asXML();
        
        /*if (parse.getAttibuteFromTag("service", "sid", 0).equals("G-01")) {
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);
            if (Site.authenService(domain, serial)) {

            } else {
                return XMLCreate.createResponseXML(300, "Generate One-Time Password", StringText.GENERATE_OTP_300).asXML();
            }
        }

        return XMLCreate.createResponseXML(203, "Generate One-Time Password", StringText.ERROR_203).asXML();
    */}
}
