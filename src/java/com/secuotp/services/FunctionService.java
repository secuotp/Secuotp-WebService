/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.services;

import com.secuotp.model.EndUser;
import com.secuotp.model.connection.ConnectionAgent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
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
@Path("/function")
public class FunctionService {

    @POST
    @Path("/approve")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_XML)
    public String approveMigrate(@FormParam("request") String code) throws ClassNotFoundException, SQLException {
        Connection con = ConnectionAgent.getInstance();
        String sql = "CALL approve_migration(?)";

        CallableStatement cs = con.prepareCall(sql);
        cs.setString(1, code);

        ResultSet rs = cs.executeQuery();
        if (rs.next()) {
            if (!rs.getString(1).equals("Approved Failed")) {
                String xml = "<secuotp service=\"approveMigrate\" check=\"1\">";
                xml += "<site-name>" + rs.getString(1) + "</site-name>";
                xml += "<site-domain>" + rs.getString(2) + "</site-domain>";
                xml += "<site-serial>" + rs.getString(3) + "</site-serial>";
                xml += "<site-description>" + rs.getString(4) + "</site-description>";
                xml += "<user-serial>" + rs.getString(5) + "</user-serial>";
                xml += "<user-removal>" + rs.getString(6) + "</user-removal>";
                xml += "<otp-length>" + rs.getInt(7) + "</otp-length>";
                xml += "<otp-pattern>" + rs.getString(8) + "</otp-pattern>";
                xml += "</secuotp>";

                return xml;
            }
        }
        return "<secuotp service=\"approveMigrate\" check=\"0\"><message>Approve Failed</message></secuotp>";

    }
}
