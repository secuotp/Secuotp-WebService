/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.secuotp.model;

import com.secuotp.model.connection.ConnectionAgent;
import com.secuotp.model.generate.SerialNumber;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zenology
 */
public class EndUser extends People {

    private String siteUserId;
    private String siteId;
    private String phone;
    private String serialNumber;
    private String removalCode;
    private boolean mobileMode;

    public String getSiteUserId() {
        return siteUserId;
    }

    public void setSiteUserId(String siteUserId) {
        this.siteUserId = siteUserId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getRemovalCode() {
        return removalCode;
    }

    public void setRemovalCode(String removalCode) {
        this.removalCode = removalCode;
    }

    public boolean getMobileMode() {
        return mobileMode;
    }

    public void setMobileMode(boolean mobileMode) {
        this.mobileMode = mobileMode;

    }

    public static EndUser getEndUser(String username, String domain) {
        try {
            Connection con = ConnectionAgent.getInstance();
            String sql = "SELECT * FROM end_user WHERE username = ? AND site_id = (SELECT site_id FROM site WHERE domain = ? LIMIT 0, 1)";
            PreparedStatement ps = con.prepareCall(sql);
            ps.setString(1, username);
            ps.setString(2, domain);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                EndUser user = new EndUser();
                user.setSiteUserId("" + rs.getInt(1));
                user.setSiteId("" + rs.getInt(2));
                user.setUsername(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setFirstname(rs.getString(5));
                user.setLastname(rs.getString(6));
                user.setPhone(rs.getString(7));
                user.setSerialNumber(rs.getString(8));
                user.setRemovalCode(rs.getString(9));
                user.setMobileMode(rs.getInt(10) > 0);

                return user;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EndUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static boolean addEndUser(EndUser user, String domainName, String serialNumber) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Connection con = ConnectionAgent.getInstance();
        String siteId = getSiteId(con, domainName, serialNumber);

        String userSerialNumber = SerialNumber.generateSerialNumber(user.getEmail());
        String removalCode = SerialNumber.generateRemovalCode(user.getFirstname() + user.getLastname());
        while (!(isNotDuplicateBySerial(userSerialNumber))) {
            userSerialNumber = SerialNumber.generateSerialNumber(user.getEmail());
        }
        if (isNotDuplicateByUsername(user.getUsername())) {
            String sql = "INSERT INTO end_user (site_id, username, email, firstname, lastname, phone_number, serial_number, removal_code, mobile_mode) VALUES (?,?,?,?,?,?,?,?,0)";
            PreparedStatement ps = con.prepareCall(sql);
            ps.setInt(1, Integer.parseInt(siteId));
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFirstname());
            ps.setString(5, user.getLastname());
            ps.setString(6, user.getPhone());
            ps.setString(7, userSerialNumber);
            ps.setString(8, removalCode);

            int row = ps.executeUpdate();
            return row > 0;
        }else{
            return false;
        }
        

    }

    private static String getPeopleId(Connection con, String email) throws SQLException {
        String sql = "SELECT people_id FROM people WHERE email = ?";
        PreparedStatement ps = con.prepareCall(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return "" + rs.getInt(1);
        }
        return null;
    }

    private static String getSiteId(Connection con, String domainName, String serialNumber) throws SQLException {
        String sql = "SELECT site_id FROM site WHERE domain = ? AND serial_number = ?";
        PreparedStatement ps = con.prepareCall(sql);
        ps.setString(1, domainName);
        ps.setString(2, serialNumber);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return "" + rs.getInt(1);
        }
        return null;
    }

    private static boolean isNotDuplicateBySerial(String serialNumber) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM end_user WHERE serial_number = ?";
        Connection con = ConnectionAgent.getInstance();
        PreparedStatement ps = con.prepareCall(sql);
        ps.setString(1, serialNumber);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) == 0;
        }
        return false;
    }

    private static boolean isNotDuplicateByUsername(String username) throws ClassNotFoundException, SQLException{
        String sql = "SELECT COUNT(*) FROM end_user WHERE username = ?";
        Connection con = ConnectionAgent.getInstance();
        PreparedStatement ps = con.prepareCall(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        
        if(rs.next()){
            return rs.getInt(1) == 0;
        }
        return false;
    }
    public static boolean disableEndUser(String username, String removalCode) {
        try {
            Connection con = ConnectionAgent.getInstance();
            String sql = "DELETE FROM end_user WHERE username = ? AND removal_code = ?";
            PreparedStatement ps = con.prepareCall(sql);
            ps.setString(1, username);
            ps.setString(2, removalCode);

            int row = ps.executeUpdate();
            return row > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(EndUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static void updateEndUser(String username, int siteId, String[] param) throws ClassNotFoundException, SQLException {
        Connection con = ConnectionAgent.getInstance();
        CallableStatement cs = con.prepareCall("CALL update_end_user(?,?,?,?,?,?,?)");
        cs.setString(1, username);
        cs.setInt(2, siteId);
        cs.setString(3, param[0]);
        cs.setString(4, param[1]);
        cs.setString(5, param[2]);
        cs.setString(6, param[3]);
        cs.setString(7, param[4]);
        cs.execute();
    }
}
