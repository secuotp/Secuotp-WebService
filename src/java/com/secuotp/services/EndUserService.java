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
import com.secuotp.model.xml.XMLParser;
import com.secuotp.model.xml.XMLValidate;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
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
    public String getEndUser(@FormParam("request") String xml) throws MalformedURLException, ClassNotFoundException, SQLException{
        XMLValidate val = new XMLValidate(new URL(StringText.GET_END_USER_DATA_XSD));
        
        if(val.validate(xml, "U-01")){
            XMLParser parse = new XMLParser(xml);
            String domain = parse.getDataFromTag("domain", 0);
            String serial = parse.getDataFromTag("serial", 0);

            if (Site.authenService(domain, serial) && !Site.checkDisabled(domain)) {
                SiteUser user = SiteUser.getSiteUser(parse.getDataFromTag("username", 0), domain);
                
                if(user == null){
                    return XMLCreate.createResponseXML(301, "Generate One-Time Password", StringText.GET_END_USER_DATA_301).asXML();
                }
                
                XMLCreate
            }
            return "<a>Passed</a>";
        }
        return XMLCreate.createResponseXML(203, "Get End-User Data", StringText.ERROR_203).asXML();
    }
}
