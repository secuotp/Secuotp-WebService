/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.services;

import com.secuotp.model.connection.ConnectionAgent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
@Path("/database")
public class DatabaseService {

    @POST
    @Path("/app-name")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_XML)
    public String searchApplicationName(@FormParam("request") String keyword) throws ClassNotFoundException, SQLException {
        Connection con = ConnectionAgent.getInstance();
        String sql = "SELECT site_name FROM secuotp.site WHERE site_name LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, "%" + keyword + "%");

        ResultSet rs = ps.executeQuery();
        String xml = "<secuotp service=\"SearchApplicationName\">";
        while (rs.next()) {
            xml += "<app-name>" + rs.getString("site_name") + "</app-name>";
        }
        xml += "</secuotp>";

        return xml;

    }
    
    @POST
    @Path("/app-info")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_XML)
    public String getApplicationInfo(@FormParam("request") String keyword) throws ClassNotFoundException, SQLException{
        Connection con = ConnectionAgent.getInstance();
        String sql = "SELECT site_name, domain, serial_number, description FROM secuotp.site WHERE site_name = ?";
        
        PreparedStatement ps = con.prepareCall(sql);
        ps.setString(1, keyword);
        
        ResultSet rs = ps.executeQuery();
        
        String xml = "<secuotp service=\"getApplicationInfo\">";
        while(rs.next()){
            xml += "<info>";
            xml += "<name>" + rs.getString(1) + "</name>";
            xml += "<domain>" + rs.getString(2) + "</domain>";
            xml += "<serial>" + rs.getString(3) + "</serial>";
            xml += "<description>" + rs.getString(4) + "</description>";
            xml += "</info>";
        }
        
        xml += "</secuotp>";
        return xml;
    }
}