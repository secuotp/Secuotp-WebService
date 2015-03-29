/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model;

import com.secuotp.model.connection.ConnectionAgent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;


/**
 *
 * @author zenology
 */
public class Migration {
    public static  boolean setMigrationCode(String siteId, String userId, String code) throws ClassNotFoundException, SQLException{
        Connection con = ConnectionAgent.getInstance();
        
        String sql = "CALL insert_migration_code (?, ?, ?)";
        
        CallableStatement cs = con.prepareCall(sql);
        cs.setInt(1, Integer.parseInt(siteId));
        cs.setInt(2, Integer.parseInt(userId));
        cs.setString(3, code);
        
        return cs.executeUpdate() > 0;
    }
}
