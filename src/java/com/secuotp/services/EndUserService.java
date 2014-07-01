/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.secuotp.services;

import com.secuotp.model.Site;
import com.secuotp.model.EndUser;
import com.secuotp.model.XMLParameter;
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
                EndUser user = EndUser.getEndUser(parse.getDataFromTag("username", 0), domain);
                
                if(user == null){
                    return XMLCreate.createResponseXML(301, "Get End-User Data", StringText.GET_END_USER_DATA_301).asXML();
                }
                
                XMLParameter param = new XMLParameter();
                param.add("username", user.getUsername());
                param.add("email", user.getEmail());
                param.add("fname", user.getFirstname());
                param.add("lname", user.getLastname());
                param.add("phone", user.getPhone());
                if(parse.getDataFromTag("type", 0).equals("full")){
                    param.add("serial", user.getSerialNumber());
                    param.add("removal", user.getRemovalCode());
                    param.add("mobile", "" + user.getMobileMode());
                }
                
                return XMLCreate.createResponseXMLWithData("Get End-User Data", StringText.GET_END_USER_DATA_101, param).asXML();
            }
            return "<a>Passed</a>";
        }
        return XMLCreate.createResponseXML(203, "Get End-User Data", StringText.ERROR_203).asXML();
    }
}
