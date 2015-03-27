/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model;

import com.secuotp.model.connection.ConnectionAgent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author zenology
 */
public class Migration {
    public static  boolean setMigrationCode(String siteId, String userId, String code) throws ClassNotFoundException, SQLException{
        Connection con = ConnectionAgent.getInstance();
        
        String chkSql = "SELECT count(*) FROM Migration where migration_code = ?";
        String sql = "INSERT INTO Migration (site_id, end_user_id, migration_code) VALUES (?, ?, ?)";
        
        PreparedStatement ps = con.prepareCall(chkSql);
        ps.setString(1, code);
        
        ResultSet rs = ps.executeQuery();
        if(!rs.next()){
            ps = con.prepareCall(sql);
            ps.setInt(1, Integer.parseInt(siteId));
            ps.setInt(2, Integer.parseInt(userId));
            ps.setString(3, code);
        
            int row = ps.executeUpdate();
            return row > 0;
        }
        return false;
    }
}
